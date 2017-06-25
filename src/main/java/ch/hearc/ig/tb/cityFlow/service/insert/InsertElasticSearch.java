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
package ch.hearc.ig.tb.cityFlow.service.insert;

import ch.hearc.ig.tb.cityFlow.business.Device;
import ch.hearc.ig.tb.cityFlow.business.Person;
import ch.hearc.ig.tb.cityFlow.business.Traject;
import ch.hearc.ig.tb.cityFlow.service.Services;
import ch.hearc.ig.tb.cityFlow.service.search.SearchElasticSearch;
import ch.hearc.ig.tb.cityFlow.utilitaire.Utilitaire;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.TimeValue;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.json.JSONObject;

/**
 *
 * @author dimitri.mella Classes used for Inserting Data in ElasticSearch
 */
public class InsertElasticSearch {

    private Utilitaire utils;
    private Random random;
    private SearchElasticSearch searchElastic;

    public InsertElasticSearch() {
        this.utils = Utilitaire.getInstance();
        this.random = new Random();
        this.searchElastic = new SearchElasticSearch();
    }

    /**
     * Method used for putting facts in ElasticSearch
     *
     * @param client the client for communication with ElasticSearch
     * @param numberFact the number of facts to put in
     * @param start the start Date
     * @param end then end Date
     * @param deviceName the deviceName for which the facts will be generate
     * @param formatter the dateFormat
     * @param insertDate the date of the insert
     */
    public void putDataRealFacts(TransportClient client, Integer numberFact, String start, String end, String deviceName, SimpleDateFormat formatter, String insertDate) {
        List<Integer> keySets;
        Integer randomKey;
        Device device = utils.getDevices().get(deviceName); // Getting the device in the list
        BulkRequestBuilder bulkRequest = client.prepareBulk(); // Prepare the client to receive a bulk Insert
        HashMap<Integer, Person> personToPut = new HashMap<>(); // Prepare the person to Put in ElasticSearch
        personToPut.putAll(utils.getPeople());
        for (int i = 0; i < numberFact; i++) {
            keySets = new ArrayList<Integer>(personToPut.keySet());
            randomKey = keySets.get(random.nextInt(keySets.size()));
            Person person = personToPut.remove(randomKey);
            try {
                Date firstSeen = utils.randomDate(start, end);
                Date lastSeen = utils.randomDate(formatter.format(firstSeen), end);
                long duration = (lastSeen.getTime() - firstSeen.getTime());
                bulkRequest.add(client.prepareIndex("cityflow", "facts").setSource(jsonBuilder()
                        .startObject()
                        .field("macAdresse", person.getMacAdresse())
                        .field("manufacturer", person.getManufacturer())
                        .field("gender", person.getGender())
                        .field("age", person.getAge())
                        .field("firstSeen", formatter.format(firstSeen))
                        .field("lastSeen", formatter.format(lastSeen))
                        .field("duration", duration)
                        .field("lastSignal", random.nextInt(40))
                        .field("minSignal", random.nextInt(40))
                        .field("maxSignal", random.nextInt(40))
                        .field("locomotion", person.getLocomotion())
                        .startObject("seenBy")
                        .field("deviceId", device.getId())
                        .field("deviceName", device.getName())
                        .startObject("location")
                        .field("lat", device.getLatitude().toString())
                        .field("lon", device.getLongitdue().toString())
                        .endObject()
                        .endObject()
                        .field("insertDate", insertDate)
                        .field("timeEvent", formatter.format(firstSeen))
                        .endObject()));

            } catch (IOException ex) {
                Logger.getLogger(Services.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        BulkResponse bulkResponse = bulkRequest.execute().actionGet();
    }

    /**
     * Method used for putting trajectoryFacts in ElasticSearch
     *
     * @param client the transportClient which will be used
     * @param dateLastInput the date of the last Input
     * @param formatter the format of the date
     * @param insertDate the date of the insert
     * @param services la classe de Service
     */
    public void putDataTrajectoryFacts(TransportClient client, String dateLastInput, SimpleDateFormat formatter, String insertDate, Services services) {
        boolean inserting = false;
        client.admin().indices().prepareRefresh("cityflow").get();
        BulkRequestBuilder bulkRequest = client.prepareBulk(); // Prepare the client to receive a bulk Request
        SearchResponse numberOfFactsPerMacAdresse = this.searchElastic.getNumberOfFactsPerMacAdresse(dateLastInput, client);
        while (true) {

            if (numberOfFactsPerMacAdresse.getAggregations() == null) {
                // If Aggregats are null it means that we can stop to generate traject
                // System.out.println("STOPPING AGGS");
                break;
            }

            Terms macAdresses = numberOfFactsPerMacAdresse.getAggregations().get("macAdresses");
            System.out.println(macAdresses.getDocCountError());
            System.out.println(macAdresses.getSumOfOtherDocCounts());
            System.out.println(macAdresses.getBuckets().size());
            // For each entry
            for (Terms.Bucket entry : macAdresses.getBuckets()) {
                if (entry.getDocCount() > 1) {
                    System.out.println("Term " + entry.getKey());      // Term
                    System.out.println("Count " + entry.getDocCount()); // Doc count
                }

                // Getting all the hits for this person
                SearchResponse factsForAPerson = this.searchElastic.getAllFactsForAPerson(entry.getKey().toString(), dateLastInput, client);

                while (true) {
                    //Break condition: No hits are returned
                    if (factsForAPerson.getHits().getHits().length == 0) {
                        // System.out.println("STOPPING");
                        break;
                    }
                    // otherwise generating trajectory
                    for (int j = 0; j < entry.getDocCount() - 1; j++) {
                        try {
                            Traject traject = new Traject();
                            JSONObject from = new JSONObject(factsForAPerson.getHits().getHits()[j].getSource());
                            JSONObject to = new JSONObject(factsForAPerson.getHits().getHits()[j + 1].getSource());
                            // System.out.println("MacAdresseWithTraject : "+ from.getString("macAdresse"));
                            traject.setFrom(utils.getDevices().get(from.getJSONObject("seenBy").getString("deviceName")));
                            traject.setTo(utils.getDevices().get(to.getJSONObject("seenBy").getString("deviceName")));
                            traject.setPerson(new Person(from.getString("macAdresse"), from.getString("manufacturer"), from.getString("gender"), from.getInt("age"), from.getString("locomotion")));
                            traject.setFromFirstSeen(formatter.parse(from.getString("firstSeen")));
                            traject.setFromLastSeen(formatter.parse(from.getString("lastSeen")));
                            traject.setToFirstSeen(formatter.parse(to.getString("firstSeen")));
                            traject.setToLastSeen(formatter.parse(to.getString("lastSeen")));

                            bulkRequest.add(client.prepareIndex("cityflow", "traject").setSource(jsonBuilder()
                                    .startObject()
                                        .field("macAdresse", traject.getPerson().getMacAdresse())
                                        .field("manufacturer", traject.getPerson().getManufacturer())
                                        .field("gender", traject.getPerson().getGender())
                                        .field("age", traject.getPerson().getAge())
                                        .startObject("from")
                                            .field("id", traject.getFrom().getId())
                                            .field("name", traject.getFrom().getName())
                                            .field("firstSeen", formatter.format(traject.getFromFirstSeen()))
                                            .field("lastSeen", formatter.format(traject.getFromLastSeen()))
                                            .startObject("location")
                                                .field("lat", traject.getFrom().getLatitude())
                                                .field("lon", traject.getFrom().getLongitdue())
                                            .endObject()
                                        .endObject()
                                        .startObject("to")
                                            .field("id", traject.getTo().getId())
                                            .field("name", traject.getTo().getName())
                                            .field("firstSeen", formatter.format(traject.getToFirstSeen()))
                                            .field("lastSeen", formatter.format(traject.getToLastSeen()))
                                            .startObject("location")
                                                .field("lat", traject.getTo().getLatitude())
                                                .field("lon", traject.getTo().getLongitdue())
                                            .endObject()
                                        .endObject()
                                    .field("insertDate", insertDate)
                                    .field("duration", traject.getToFirstSeen().getTime() - traject.getFromLastSeen().getTime())
                                    .field("timeEvent", formatter.format(traject.getFromFirstSeen()))
                                    .endObject()));

                            inserting = true;
//                            client.close();
//                            services.generateClient();
//                            client = services.getClient();
                        } catch (ParseException | IOException ex) {
                            Logger.getLogger(Services.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    factsForAPerson = client.prepareSearchScroll(factsForAPerson.getScrollId()).setScroll(new TimeValue(6000000)).execute().actionGet();
                }
            }
            numberOfFactsPerMacAdresse = client.prepareSearchScroll(numberOfFactsPerMacAdresse.getScrollId()).setScroll(new TimeValue(6000000)).execute().actionGet();
        }
        if (inserting) {
            BulkResponse bulkResponse = bulkRequest.execute().actionGet();
        }
    }
}
