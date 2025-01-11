package com.sxy.snote.elasticSearch.repo;

import com.sxy.snote.elasticSearch.document.NoteDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NoteDocRepo extends ElasticsearchRepository<NoteDoc, UUID> {
}