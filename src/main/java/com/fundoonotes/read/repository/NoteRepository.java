package com.fundoonotes.read.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Repository;

@Repository
public class NoteRepository {

	private static final Logger LOGGER = Logger.getLogger(NoteRepository.class);

	private RestHighLevelClient restHighLevelClient;

	public NoteRepository(RestHighLevelClient restHighLevelClient) {
		this.restHighLevelClient = restHighLevelClient;
	}

	public List<Map<String, Object>> getNotesByUserId(String index, String type, String fieldName, String value) {
		LOGGER.info("GET NOTES BY USER ID REPO");
		List<Map<String, Object>> userNotes = new ArrayList<Map<String, Object>>();
		try {
			SearchRequest searchRequest = new SearchRequest(index);
			searchRequest.types(type);
			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
			searchSourceBuilder.query(QueryBuilders.termQuery(fieldName, value));
			searchRequest.source(searchSourceBuilder);
			SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
			SearchHit[] hits = searchResponse.getHits().getHits();
			for (SearchHit note : hits) {
				userNotes.add(note.getSourceAsMap());
				LOGGER.info(note.getSourceAsMap());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return userNotes;
	}

	public List<Map<String, Object>> getNotesByState(String index, String type, String field, String user_id) {
		LOGGER.info("GET TRASH/ARCHIVE NOTES BY USER ID REPO");
		List<Map<String, Object>> userNotes = new ArrayList<Map<String, Object>>();
		try {
			SearchRequest searchRequest = new SearchRequest(index);
			searchRequest.types(type);
			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
			searchSourceBuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("user_id", user_id))
					.must(QueryBuilders.termQuery(field, true)));
			searchRequest.source(searchSourceBuilder);
			SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
			SearchHit[] hits = searchResponse.getHits().getHits();
			for (SearchHit note : hits) {
				userNotes.add(note.getSourceAsMap());
				LOGGER.info(note.getSourceAsMap());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return userNotes;
	}
}
