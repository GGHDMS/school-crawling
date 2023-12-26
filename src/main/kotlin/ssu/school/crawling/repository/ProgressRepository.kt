package ssu.school.crawling.repository

import org.springframework.data.jpa.repository.JpaRepository
import ssu.school.crawling.domain.funsysten.entity.Progress

interface ProgressRepository : JpaRepository<Progress, Long> {
    fun findByDescription(description: String): Progress?
}
