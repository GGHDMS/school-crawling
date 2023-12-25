package ssu.school.crawling.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import ssu.school.crawling.domain.notice.entity.Notice

interface NoticeRepository : JpaRepository<Notice, Long> {

    fun findByTitleContaining(title: String): List<Notice>

    fun findByContentContaining(content: String): List<Notice>

    // title 또는 content에 대한 like 쿼리
    @Query("SELECT n FROM Notice n WHERE n.title LIKE %:query% OR n.content LIKE %:query%")
    fun findByTitleOrContentContaining(@Param("query") query: String): List<Notice>
}
