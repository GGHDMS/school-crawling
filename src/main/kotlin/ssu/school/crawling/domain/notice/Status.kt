package ssu.school.crawling.domain.notice

enum class Status(val description: String) {
    NONE(""),
    IN_PROGRESS("진행"),
    COMPLETED("완료");

    companion object {
        fun fromDescription(description: String): Status {
            return entries.firstOrNull { it.description == description } ?: NONE
        }
    }
}
