package ssu.school.crawling.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.stereotype.Service

@Service
class CrawlingWebService(
    private val crawlingDBService: CrawlingDBService,
) {
    suspend fun crawlingNotice() {
        val jobs = mutableListOf<Deferred<Unit>>()

        for (pageNumber in 1..631) {
            val baseUrl = "https://scatch.ssu.ac.kr/공지사항/page/$pageNumber"
            val job: Deferred<Unit> = CoroutineScope(Dispatchers.IO).async {
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

                    val body = Jsoup.connect(url).get().select("div.bg-white p")
                    val paragraphs = body.select("p")

                    val stringBuilder = StringBuilder()
                    for (paragraph in paragraphs) {
                        stringBuilder.append(paragraph.text()).append("\n")
                    }

                    val content: String = stringBuilder.toString().trim()

                    crawlingDBService.saveNoticeWithRetry(
                        date,
                        status,
                        category,
                        title,
                        content,
                        url,
                        department,
                        views
                    )
                }
            }
            jobs.add(job)
        }

        CoroutineScope(Dispatchers.IO).async {
            jobs.awaitAll()
        }
    }
}
