/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hearc.ig.tb.cityFlow.application;

import ch.hearc.ig.tb.cityFlow.service.Services;

/**
 *
 * @author Dimitri
 */
public class Application {

    public static void main(String[] args) {
        
        Services services = new Services();
        services.setOptions();
        services.setSettings();
        System.out.println("Search Guard Inserting Data v5");
        services.generateStaticData(10000);
        // 25.05.2017
//        services.generateDataFacts(5000, "2017-05-25T08:30:00", "2017-05-25T09:00:00", "PasserelleMillenaire", false);
//        services.generateDataFacts(3400, "2017-05-25T09:10:00", "2017-05-25T09:50:00", "McDo", false);
//        services.generateDataFacts(9500, "2017-05-25T10:00:00", "2017-05-25T11:00:00", "OFS", false);
//        services.generateDataFacts(1500, "2017-05-25T11:30:00", "2017-05-25T12:00:00", "PosteGare", false);
//        services.generateDataFacts(2000, "2017-05-25T12:30:00", "2017-05-25T14:00:00", "McDo", false);
//        services.generateDataFacts(2500, "2017-05-25T14:30:00", "2017-05-25T15:00:00", "HEG", false);
//        services.generateDataFacts(9000, "2017-05-25T16:00:00", "2017-05-25T18:00:00", "OFS", false);
//        services.generateDataFacts(9500, "2017-05-25T18:30:00", "2017-05-25T22:00:00", "PosteGare", true);
//        // 26.05.2017
//        services.generateDataFacts(3400, "2017-05-26T07:30:00", "2017-05-26T08:30:00", "McDo", false);
//        services.generateDataFacts(1000, "2017-05-26T08:40:00", "2017-05-26T10:00:00", "BancomatBCN", false);
//        services.generateDataFacts(4000, "2017-05-26T10:30:00", "2017-05-26T12:00:00", "HEG", false);
//        services.generateDataFacts(2000, "2017-05-26T12:30:00", "2017-05-26T14:00:00", "PasserelleMillenaire", false);
//        services.generateDataFacts(3450, "2017-05-26T14:30:00", "2017-05-26T16:00:00", "OFS", false);
//        services.generateDataFacts(4000, "2017-05-26T16:30:00", "2017-05-26T18:30:00", "HEG", false);
//        services.generateDataFacts(5000, "2017-05-26T18:45:00", "2017-05-26T20:30:00", "PosteGare", false);
//        services.generateDataFacts(2550, "2017-05-26T20:45:00", "2017-05-26T21:30:00", "BancomatBCN", false);
//        services.generateDataFacts(2500, "2017-05-26T21:45:00", "2017-05-26T23:30:00", "HEG", false);
//        services.generateDataFacts(3500, "2017-05-26T23:45:00", "2017-05-27T00:30:00", "PosteGare", true);
        
        // 01.06.2017
        services.generateDataFacts(5000, "2017-06-01T08:30:00", "2017-06-01T09:00:00", "Coop", false);
        services.generateDataFacts(3400, "2017-06-01T09:10:00", "2017-06-01T09:50:00", "McDo", false);
        services.generateDataFacts(9500, "2017-06-01T10:00:00", "2017-06-01T11:00:00", "OFS", false);
        services.generateDataFacts(1500, "2017-06-01T11:30:00", "2017-06-01T12:00:00", "Coop", false);
        services.generateDataFacts(2000, "2017-06-01T12:30:00", "2017-06-01T14:00:00", "PosteGare", false);
        services.generateDataFacts(2500, "2017-06-01T14:30:00", "2017-06-01T15:00:00", "HEG", false);
        services.generateDataFacts(9000, "2017-06-01T16:00:00", "2017-06-01T18:00:00", "OFS", false);
        services.generateDataFacts(9500, "2017-06-01T18:30:00", "2017-06-01T22:00:00", "McDo", true);
        // 02.06.2017
        services.generateDataFacts(3400, "2017-06-02T07:30:00", "2017-06-02T08:30:00", "McDo", false);
        services.generateDataFacts(1000, "2017-06-02T08:40:00", "2017-06-02T10:00:00", "OFS", false);
        services.generateDataFacts(4000, "2017-06-02T10:30:00", "2017-06-02T12:00:00", "HEG", false);
        services.generateDataFacts(2000, "2017-06-02T12:30:00", "2017-06-02T14:00:00", "PasserelleMillenaire", false);
        services.generateDataFacts(3450, "2017-06-02T14:30:00", "2017-06-02T16:00:00", "OFS", false);
        services.generateDataFacts(4000, "2017-06-02T16:30:00", "2017-06-02T18:30:00", "BancomatBCN", false);
        services.generateDataFacts(5000, "2017-06-02T18:45:00", "2017-06-02T20:30:00", "Coop", false);
        services.generateDataFacts(2550, "2017-06-02T20:45:00", "2017-06-02T21:30:00", "BancomatBCN", false);
        services.generateDataFacts(2500, "2017-06-02T21:45:00", "2017-06-02T23:30:00", "OFS", false);
        services.generateDataFacts(3500, "2017-06-02T23:45:00", "2017-06-03T00:30:00", "Coop", true);
        
//        services.generateStaticData(100);
//        // 25.05.2017
//        services.generateDataFacts(20, "2017-05-25T08:30:00", "2017-05-25T09:00:00", "PasserelleMillenaire", false);
//        services.generateDataFacts(30, "2017-05-25T09:10:00", "2017-05-25T09:50:00", "HEG", false);
//        services.generateDataFacts(25, "2017-05-25T10:00:00", "2017-05-25T11:00:00", "McDo", false);
//        services.generateDataFacts(80, "2017-05-25T11:30:00", "2017-05-25T12:00:00", "HEG", false);
//        services.generateDataFacts(90, "2017-05-25T12:30:00", "2017-05-25T14:00:00", "PasserelleMillenaire", false);
//        services.generateDataFacts(95, "2017-05-25T14:30:00", "2017-05-25T15:00:00", "HEG", false);
//        services.generateDataFacts(45, "2017-05-25T16:00:00", "2017-05-25T18:00:00", "HEG", false);
//        // services.generateDataFacts(60, "2017-05-25T18:30:00", "2017-05-25T22:00:00", "PosteGare", true);
//        // 26.05.2017
//        services.generateDataFacts(50, "2017-05-26T07:30:00", "2017-05-26T08:30:00", "PasserelleMillenaire", false);
//        services.generateDataFacts(56, "2017-05-26T08:40:00", "2017-05-26T10:00:00", "BancomatBCN", false);
//        services.generateDataFacts(34, "2017-05-26T10:30:00", "2017-05-26T12:00:00", "HEG", false);
//        services.generateDataFacts(65, "2017-05-26T12:30:00", "2017-05-26T14:00:00", "OFS", false);
//        services.generateDataFacts(23, "2017-05-26T14:30:00", "2017-05-26T16:00:00", "OFS", false);
//        services.generateDataFacts(67, "2017-05-26T16:30:00", "2017-05-26T18:30:00", "HEG", false);
//        services.generateDataFacts(78, "2017-05-26T18:45:00", "2017-05-26T20:30:00", "McDo", false);
//        services.generateDataFacts(56, "2017-05-26T20:45:00", "2017-05-26T21:30:00", "PasserelleMillenaire", false);
//        services.generateDataFacts(76, "2017-05-26T21:45:00", "2017-05-26T23:30:00", "McDo", false);
//        // services.generateDataFacts(87, "2017-05-26T23:45:00", "2017-05-27T00:30:00", "PasserelleMillenaire", true);
    }

}
