package ssu.school.crawling.domain.notice.elastic

import jakarta.persistence.Id
import org.springframework.data.elasticsearch.annotations.DateFormat
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import java.time.LocalDate

@Document(indexName = "notice")
class NoticeEl(
    @Id
    val id: String? = null,

    @Field(name = "date", type= FieldType.Date, format = [DateFormat.date])
    val date: LocalDate,
    var status: String,
    val category: String,
    var title: String,
    var content: String,
    var url: String,
    val department: String,
    var views: Int,
)
