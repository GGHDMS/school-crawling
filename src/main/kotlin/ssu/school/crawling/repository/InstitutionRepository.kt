package ssu.school.crawling.repository

import org.springframework.data.jpa.repository.JpaRepository
import ssu.school.crawling.domain.funsysten.entity.Institution

interface InstitutionRepository : JpaRepository<Institution, Long>{

    fun findByInstitutionAndDepartmentAndSmallDepartment(
        institution: String,
        department: String,
        smallDepartment: String,
    ): Institution?

}
