package ssu.school.crawling.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import ssu.school.crawling.domain.funsysten.entity.FunSystem
import ssu.school.crawling.domain.notice.entity.Notice
import ssu.school.crawling.service.CrawlingWebService
import ssu.school.crawling.service.SearchService

@RestController
class CrawlingController(
    private val crawlingWebService: CrawlingWebService,
    private val searchService: SearchService,
) {

    @GetMapping("/search/notices")
    fun searchNotice(@RequestParam query: String): List<Notice> {
        return searchService.searchNotice(query)
    }

    @GetMapping("/search/fun-systems")
    fun searchFunSystem(@RequestParam query: String): List<FunSystem> {
        return searchService.searchFunSystem(query)
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/crawling/notices")
    suspend fun crawling(): ResponseEntity<String> {
        return try {
            crawlingWebService.crawlingNotice()
            ResponseEntity.ok("Crawling started successfully.")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Crawling failed: ${e.message}")
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/crawling/fun-systems")
    suspend fun crawlingFunSystem(): ResponseEntity<String> {
        return try {
            crawlingWebService.crawlingFunSystem()
            ResponseEntity.ok("Crawling started successfully.")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Crawling failed: ${e.message}")
        }
    }
}
