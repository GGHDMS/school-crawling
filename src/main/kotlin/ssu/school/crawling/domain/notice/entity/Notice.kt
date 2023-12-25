package ssu.school.crawling.domain.notice.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import ssu.school.crawling.domain.notice.Status
import java.time.LocalDate

@Entity
class Notice(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @field:Column(name = "date")
    val date: LocalDate,

    @field:Column(name = "status")
    @field:Enumerated(EnumType.STRING)
    var status: Status,

    @field:ManyToOne
    @field:JoinColumn(name = "category_id")
    val category: Category,

    @field:Column(name = "title")
    var title: String,

    @field:Column(columnDefinition = "TEXT", name = "content")
    var content: String,

    @field:Column(columnDefinition = "TEXT")
    var url: String,

    @field:ManyToOne
    @field:JoinColumn(name = "department_id")
    val department: Department,

    @field:Column(name = "views")
    var views: Int,
) {
    override fun toString(): String {
        return "Notice(date='$date', status='$status', category='$category', title='$title', url='$url', department='$department', views=$views)"
    }
}

