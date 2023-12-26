package ssu.school.crawling.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import ssu.school.crawling.domain.funsysten.entity.FunSystem

interface FunSystemRepository : JpaRepository<FunSystem, Long> {

    @Query("SELECT f FROM FunSystem f WHERE f.title LIKE %:query% OR f.content LIKE %:query%")
    fun findByTitleOrContentContaining(@Param("query") query: String): List<FunSystem>
}
