package ssu.school.crawling.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class CrawlingWebService(
    private val crawlingDBService: CrawlingDBService,
) {
    suspend fun crawlingNotice() {
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

                    crawlingDBService.saveNotice(
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

        coroutineScope.async {
            jobs.awaitAll()
        }
    }

    suspend fun crawlingFunSystem() {
        val jobs = mutableListOf<Deferred<Unit>>()
        val coroutineScope = CoroutineScope(Dispatchers.IO)
        // 283
        for (pageNumber in 1..283) {
            val job: Deferred<Unit> = coroutineScope.async {
                println(pageNumber)
                val baseUrl = "https://fun.ssu.ac.kr/ko/program/all/list/all/$pageNumber"
                val document: Document = Jsoup.connect(baseUrl).get()
                val select = document.select("ul.columns-4")

                select.select("li").forEach {
                    val url = "https://fun.ssu.ac.kr" + it.selectFirst("a")?.attr("href")
                    val content = it.selectFirst(".content")!!
                    val views = content.selectFirst(".hit")?.text()?.split(" ")?.firstOrNull()?.toIntOrNull() ?: 0
                    val institution = content.select(".department .institution").text() ?: ""
                    val department = content.select(".department .department").text() ?: ""
                    val smallDepartment =
                        content.select(".department .small_department").text().substringAfter("-").trim()

                    val title = content.selectFirst(".title")?.text() ?: ""

                    val dateTimes = getDateTimes(content.select("small.thema_point_color.topic ~ small time"))

                    val bottom = it.selectFirst(".bottom")!!
                    val type = bottom.selectFirst(".info_signin .type")?.text() ?: ""
                    val progressClass =
                        bottom.selectFirst(".progress")?.classNames()?.let { progress -> progress - "progress" }
                            ?.joinToString(" ") ?: ""
                    val label = bottom.selectFirst("label")?.text() ?: ""

                    val body: Document = Jsoup.connect(url).get()
                    val stringBuilder = StringBuilder()
                    stringBuilder.append(body.selectFirst(".text")?.text()?.trim()).append("\n")

                    val paragraphs = body.select("div .description p")

                    for (paragraph in paragraphs) {
                        val trimmedText = paragraph.text().replace("\\s+".toRegex(), " ").trim()
                        if (trimmedText.isNotEmpty()) {
                            stringBuilder.append(trimmedText).append("\n")
                        }
                    }

                    crawlingDBService.saveFunSystem(
                        title,
                        stringBuilder.toString(),
                        dateTimes,
                        institution,
                        department,
                        smallDepartment,
                        progressClass,
                        type,
                        label,
                        url,
                        views
                    )
                }
            }
            jobs.add(job)
        }

        coroutineScope.async {
            jobs.awaitAll()
        }
    }

    fun getDateTimes(elements: Elements): MutableList<Pair<LocalDateTime, LocalDateTime>> {
        val dates = mutableListOf<Pair<LocalDateTime, LocalDateTime>>()

        elements.chunked(2).forEach { timeElements ->
            val startElement = timeElements.first()
            val endElement = timeElements.last()

            var startDateTimeText = startElement.text()
            var endDateTimeText = endElement.text()

            val startDateFormat = startElement.attr("data-format")

            if (startDateFormat == "Y.m.d(D)") {
                startDateTimeText = "$startDateTimeText 00:00"
                endDateTimeText = "$endDateTimeText 23:59"
            } else if (startDateFormat == "Y.m.d(D) H:i") {
                val startDate = startDateTimeText.split(" ")[0]
                endDateTimeText = "$startDate $endDateTimeText"
            }

            val startDateTime = parseToLocalDateTime(startDateTimeText)
            val endDateTime = parseToLocalDateTime(endDateTimeText)

            dates.add(Pair(startDateTime, endDateTime))
        }

        return dates
    }

    fun parseToLocalDateTime(dateTimeText: String): LocalDateTime {
        return LocalDateTime.parse(dateTimeText, DateTimeFormatter.ofPattern("yyyy.MM.dd(EEE) HH:mm"))
    }
}
