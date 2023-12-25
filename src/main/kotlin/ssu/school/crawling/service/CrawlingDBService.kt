package ssu.school.crawling.service

import org.slf4j.LoggerFactory
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Recover
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate
import ssu.school.crawling.domain.notice.entity.Category
import ssu.school.crawling.domain.notice.entity.Department
import ssu.school.crawling.domain.notice.entity.Notice
import ssu.school.crawling.domain.notice.Status
import ssu.school.crawling.repository.CategoryRepository
import ssu.school.crawling.repository.DepartmentRepository
import ssu.school.crawling.repository.NoticeRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class CrawlingDBService(
    private val noticeRepository: NoticeRepository,
    private val categoryRepository: CategoryRepository,
    private val departmentRepository: DepartmentRepository,
    private val transactionTemplate: TransactionTemplate,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Retryable(
        value = [Exception::class],
        maxAttempts = 3,
        backoff = Backoff(delay = 1000, multiplier = 2.0),
    )
    fun saveNoticeWithRetry(
        date: String,
        status: String,
        category: String,
        title: String,
        content: String,
        url: String,
        department: String,
        views: Int,
    ) {
        try {
            // Notice 저장 부분만 트랜잭션으로 묶음
            transactionTemplate.execute {
                val findCategory = categoryRepository.findByDescription(category)
                    ?: categoryRepository.save(Category(description = category))

                val findDepartment = departmentRepository.findByDescription(department)
                    ?: departmentRepository.save(Department(description = department))

                noticeRepository.save(
                    Notice(
                        date = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy.MM.dd")),
                        status = Status.fromDescription(status),
                        category = findCategory,
                        title = title,
                        content = content,
                        url = url,
                        department = findDepartment,
                        views = views,
                    )
                )
            }
        } catch (e: Exception) {
            logger.error("Error processing notice: ${e.message}")
            throw e // 다시 던져서 리트라이 메커니즘에 의해 처리되도록 함
        }
    }

    // 리트라이가 실패할 경우 실행되는 메소드
    @Recover
    fun recover(e: Exception) {
        // 실패 처리 로직을 여기에 추가
        logger.error("Recovery logic after maximum attempts. Error: ${e.message}")
    }
}
