package ssu.school.crawling

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SchoolCrawlingApplication

fun main(args: Array<String>) {
    runApplication<SchoolCrawlingApplication>(*args)
}
