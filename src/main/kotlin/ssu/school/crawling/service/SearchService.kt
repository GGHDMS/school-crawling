package ssu.school.crawling.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ssu.school.crawling.domain.funsysten.entity.FunSystem
import ssu.school.crawling.domain.notice.entity.Notice
import ssu.school.crawling.repository.FunSystemRepository
import ssu.school.crawling.repository.NoticeRepository

@Service
@Transactional(readOnly = true)
class SearchService(
    private val noticeRepository: NoticeRepository,
    private val funSystemRepository: FunSystemRepository,
) {

    fun searchNotice(query: String): List<Notice> {
        return noticeRepository.findByTitleOrContentContaining(query)
    }

    fun searchFunSystem(query: String): List<FunSystem> {
        return funSystemRepository.findByTitleOrContentContaining(query)
    }
}
