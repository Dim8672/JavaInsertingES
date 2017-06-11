/*
 * Copyright 2017 dimitri.mella.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.hearc.ig.tb.cityFlow.service.search;

import ch.hearc.ig.tb.cityFlow.service.Services;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

/**
 *
 * @author dimitri.mella
 * Classes used for Searching in ElasticSearch
 */
public class SearchElasticSearch {

    public SearchElasticSearch() {
    }
    
    /**
     * Method used for knowing if it's the first time that data are inserting in ElasticSearch
     * @param client for communication with ES
     * @return a boolean indicates if it's the first time that data are inserting in ElasticSearch
     */
    public boolean factsEmtpy(TransportClient client){
        // Request for knowing if hits get returned or not
        SearchResponse response = client.prepareSearch("cityflow")
                .setTypes("facts")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.matchAllQuery()) // Query
                .setFrom(0).setSize(1).setExplain(true)
                .get();
        
        return response.getHits().getHits().length == 0;
    }
    
    /**
     * Method for knowing when the last import took place
     * @param client the client for communication with ES
     * @param formatter the format of the date
     * @return a date representing the last import
     */
    public Date getLastImportDate(TransportClient client, SimpleDateFormat formatter) {
        SearchResponse response = client.prepareSearch("cityflow")
                .setTypes("facts")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.matchAllQuery()) // Query
                .addSort(SortBuilders.fieldSort("insertDate").order(SortOrder.DESC))
                .setFrom(0).setSize(1).setExplain(true)
                .get();
        try {
            return formatter.parse(response.getHits().getAt(0).getSource().get("insertDate").toString());
        } catch (ParseException ex) {
            Logger.getLogger(Services.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Method used for getting the number of facts per person in ElasticSearch
     * @param lastInputDate the last input from which the search will begin
     * @param client the client which will be used to communicate with ES
     * @return a SearchResponse with the hits
     */
    public SearchResponse getNumberOfFactsPerMacAdresse(String lastInputDate, TransportClient client){
        return client.prepareSearch("cityflow")
                .setTypes("facts")
                .setQuery(QueryBuilders.rangeQuery("insertDate").gt(lastInputDate))
                .setScroll(new TimeValue(6000000))
                .addAggregation(AggregationBuilders.terms("macAdresses").field("macAdresse").size(Integer.MAX_VALUE))
                .setSize(10000).execute().actionGet();
    }
    
    /**
     * Method used for getting all the facts for a person after the last input date
     * @param macAdresse the identifying of the person
     * @param lastInputDate the last input date
     * @param client the client which will be used to communicate with ES
     * @return a SearchResponse with the hits
     */
    public SearchResponse getAllFactsForAPerson(String macAdresse, String lastInputDate, TransportClient client){
        return client.prepareSearch("cityflow")
                        .setTypes("facts")
                        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                        .setScroll(new TimeValue(6000000))
                        .setQuery(QueryBuilders.termQuery("macAdresse", macAdresse)) // Query
                        .setPostFilter(QueryBuilders.rangeQuery("insertDate").gt(lastInputDate))
                        .addSort(SortBuilders.fieldSort("firstSeen").order(SortOrder.ASC))
                        .setSize(100).execute().actionGet();
    }
}
