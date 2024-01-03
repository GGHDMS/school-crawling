package ssu.school.crawling.domain.funsysten.entity

import jakarta.persistence.Id
import org.springframework.data.elasticsearch.annotations.Document

@Document(indexName = "funSystem")
class FunSystemEL(
    @Id
    private val id: Long,
)

