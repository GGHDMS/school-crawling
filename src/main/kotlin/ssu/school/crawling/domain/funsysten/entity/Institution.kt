package ssu.school.crawling.domain.funsysten.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Institution(
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @field:Column
    val institution: String,

    @field:Column
    val department: String,

    @field:Column
    val smallDepartment: String,
)

