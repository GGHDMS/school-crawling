package ssu.school.crawling.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.stereotype.Service
import ssu.school.crawling.domain.notice.elastic.NoticeEl
import ssu.school.crawling.repository.elastic.NoticeElRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class CrawlingElService(
    private val noticeElRepository: NoticeElRepository,
) {

    suspend fun crawlingNoticeEl() {
        val jobs = mutableListOf<Deferred<Unit>>()
        val coroutineScope = CoroutineScope(Dispatchers.IO)

        for (pageNumber in 1..631) {
            val job: Deferred<Unit> = coroutineScope.async {
                val baseUrl = "https://scatch.ssu.ac.kr/공지사항/page/$pageNumber"
                println(pageNumber)
                val document: Document = Jsoup.connect(baseUrl).get()
                document.select("ul.notice-lists li:not(.notice_head)").forEach {
                    val date = it.selectFirst(".notice_col1 .text-info")?.text() ?: ""
                    val status = it.selectFirst(".notice_col2 .tag")?.text() ?: ""
                    val category = it.selectFirst(".notice_col3 a .label")?.text() ?: ""
                    val title = it.selectFirst(".notice_col3 a .d-inline-blcok.m-pt-5")?.text() ?: ""
                    val url = it.selectFirst(".notice_col3 a")?.attr("href") ?: ""
                    val department = it.selectFirst(".notice_col4")?.text() ?: ""
                    val views = it.selectFirst(".notice_col5")?.text()?.toIntOrNull() ?: 0

                    val paragraphs = Jsoup.connect(url).get().select("div.bg-white p")

                    val stringBuilder = StringBuilder()
                    for (paragraph in paragraphs) {
                        val trimmedText = paragraph.text().replace("\\s+".toRegex(), " ").trim()
                        if (trimmedText.isNotEmpty()) {
                            stringBuilder.append(trimmedText).append("\n")
                        }
                    }

                    val content: String = stringBuilder.toString().trim()

                    noticeElRepository.save(
                        NoticeEl(
                        date = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy.MM.dd")),
                        status = status,
                        category = category,
                        title = title,
                        content = content,
                        url = url,
                        department = department,
                        views = views
                    )
                    )
                }
            }
            jobs.add(job)
        }

        coroutineScope.async {
            jobs.awaitAll()
        }
    }
}
