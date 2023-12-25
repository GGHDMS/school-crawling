package ssu.school.crawling.repository

import org.springframework.data.jpa.repository.JpaRepository
import ssu.school.crawling.domain.notice.entity.Category

interface CategoryRepository : JpaRepository<Category, Long> {

    fun findByDescription(description: String): Category?
}
