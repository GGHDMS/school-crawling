package ssu.school.crawling.domain.funsysten.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import ssu.school.crawling.domain.funsysten.Type
import java.time.LocalDateTime

@Entity
class FunSystem(
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @field:Column
    var title: String,

    @field:Column(columnDefinition = "TEXT")
    var content: String,

    @field:Column
    var applicationBegin: LocalDateTime,

    @field:Column
    var applicationEnd: LocalDateTime,

    @field:Column
    var operationBegin: LocalDateTime,

    @field:Column
    var operationEnd: LocalDateTime,

    @field:ManyToOne
    @field:JoinColumn(name = "institution_id")
    var institution: Institution,

    @field:ManyToOne
    @field:JoinColumn(name = "progress_id")
    var progress: Progress,

    @field:Enumerated(EnumType.STRING)
    var type: Type,

    @field:Column
    var label: String,

    @field:Column
    var url: String,

    @field:Column(name = "views")
    var views: Int,

    )

