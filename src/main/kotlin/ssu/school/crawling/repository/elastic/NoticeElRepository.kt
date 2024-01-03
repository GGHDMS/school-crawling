package ssu.school.crawling.repository.elastic

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import ssu.school.crawling.domain.notice.elastic.NoticeEl

interface NoticeElRepository : ElasticsearchRepository<NoticeEl, Long>
