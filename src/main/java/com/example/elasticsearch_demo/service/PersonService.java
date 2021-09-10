package com.example.elasticsearch_demo.service;

import com.example.elasticsearch_demo.document.PersonDocument;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PersonService {
    private RestHighLevelClient restHighLevelClient;

    private ObjectMapper objectMapper;

    private final static String INDEX ="person";

    @Autowired
    public PersonService(RestHighLevelClient restHighLevelClient,ObjectMapper objectMapper){
        this.restHighLevelClient=restHighLevelClient;
        this.objectMapper=objectMapper;
    }

    public String createPersonDocument(PersonDocument document) throws Exception{
        document.setPersonId(UUID.randomUUID().toString());
        IndexRequest request = new IndexRequest(INDEX);
        request.id(document.getPersonId());
        request.source(convertPersonDocumentToMap(document), XContentType.JSON);
        IndexResponse indexResponse = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        return indexResponse.getResult().name();
    }
    public PersonDocument findById(String id) throws Exception {

        GetRequest getRequest = new GetRequest(INDEX,id);

        GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        Map<String, Object> resultMap = getResponse.getSource();

        return convertMapToPersonDocument(resultMap);

    }
    public String updatePersonDocument(PersonDocument document) throws Exception {

        PersonDocument resultDocument = findById(document.getPersonId());

        UpdateRequest updateRequest = new UpdateRequest(
                INDEX,
                resultDocument.getPersonId());

        updateRequest.doc(convertPersonDocumentToMap(document));
        UpdateResponse updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);

        return updateResponse
                .getResult()
                .name();

    }

    private Map<String, Object> convertPersonDocumentToMap(PersonDocument document) {
        return objectMapper.convertValue(document, Map.class);
    }
    private PersonDocument convertMapToPersonDocument(Map<String, Object> map){
        return objectMapper.convertValue(map,PersonDocument.class);
    }


    public List<PersonDocument> findAll() throws Exception {
        SearchRequest searchRequest = buildSearchRequest(INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse =
                restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        return getSearchResult(searchResponse);
    }

    public List<PersonDocument> findPersonDocumentByName(String name) throws Exception{


        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(INDEX);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        MatchQueryBuilder matchQueryBuilder = QueryBuilders
                .matchQuery("name",name)
                .operator(Operator.AND);

        searchSourceBuilder.query(matchQueryBuilder);

        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse =
                restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        return getSearchResult(searchResponse);

    }
    public String deletePersonDocumentDocument(String id) throws Exception {

        DeleteRequest deleteRequest = new DeleteRequest(INDEX, id);
        DeleteResponse response = restHighLevelClient.delete(deleteRequest,RequestOptions.DEFAULT);

        return response
                .getResult()
                .name();

    }

    private List<PersonDocument> getSearchResult(SearchResponse response) {

        SearchHit[] searchHit = response.getHits().getHits();

        List<PersonDocument> personDocuments = new ArrayList<>();

        for (SearchHit hit : searchHit){
            personDocuments
                    .add(objectMapper
                            .convertValue(hit
                                    .getSourceAsMap(), PersonDocument.class));
        }

        return personDocuments;
    }
    private SearchRequest buildSearchRequest(String index) {

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(index);

        return searchRequest;
    }
}
