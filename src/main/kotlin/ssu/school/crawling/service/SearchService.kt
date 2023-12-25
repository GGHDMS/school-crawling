package ssu.school.crawling.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ssu.school.crawling.domain.notice.entity.Notice
import ssu.school.crawling.repository.NoticeRepository

@Service
@Transactional(readOnly = true)
class SearchService(
    private val noticeRepository: NoticeRepository,
) {

    fun search(query: String): List<Notice> {
        return noticeRepository.findByTitleOrContentContaining(query)
    }
}
