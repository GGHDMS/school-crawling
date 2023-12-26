package ssu.school.crawling.domain.funsysten

enum class Type(private val description: String) {
    NONE(""), PERSONAL("개인"), TEAM("팀");

    companion object {
        fun fromDescription(description: String): Type{
            return entries.firstOrNull { it.description == description } ?: NONE
        }
    }
}
