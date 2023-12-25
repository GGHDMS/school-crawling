package ssu.school.crawling.repository

import org.springframework.data.jpa.repository.JpaRepository
import ssu.school.crawling.domain.notice.entity.Department

interface DepartmentRepository : JpaRepository<Department, Long> {

    fun findByDescription(description: String): Department?
}
