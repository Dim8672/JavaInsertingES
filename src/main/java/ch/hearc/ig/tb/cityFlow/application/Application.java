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
        services.generateDataFacts(5000, "2017-05-25T08:30:00", "2017-05-25T09:00:00", "PosteGare", false);
        services.generateDataFacts(3400, "2017-05-25T09:10:00", "2017-05-25T09:50:00", "HEG", false);
        services.generateDataFacts(9500, "2017-05-25T10:00:00", "2017-05-25T11:00:00", "Coop", false);
        services.generateDataFacts(1500, "2017-05-25T11:30:00", "2017-05-25T12:00:00", "OFS", false);
        services.generateDataFacts(2000, "2017-05-25T12:30:00", "2017-05-25T14:00:00", "PasserelleMillenaire", false);
        services.generateDataFacts(2500, "2017-05-25T14:30:00", "2017-05-25T15:00:00", "Coop", false);
        services.generateDataFacts(9000, "2017-05-25T16:00:00", "2017-05-25T18:00:00", "HEG", false);
        services.generateDataFacts(9500, "2017-05-25T18:30:00", "2017-05-25T22:00:00", "PosteGare", true);
        // 26.05.2017
//        services.generateDataFacts(25000, "2017-05-26T07:30:00", "2017-05-26T08:30:00", "McDo", false);
//        services.generateDataFacts(10000, "2017-05-26T08:40:00", "2017-05-26T10:00:00", "HEG", false);
//        services.generateDataFacts(40000, "2017-05-26T10:30:00", "2017-05-26T12:00:00", "PasserelleMillenaire", false);
//        services.generateDataFacts(10000, "2017-05-26T12:30:00", "2017-05-26T14:00:00", "HEG", false);
//        services.generateDataFacts(34500, "2017-05-26T14:30:00", "2017-05-26T16:00:00", "BancomatBCN", false);
//        services.generateDataFacts(4000, "2017-05-26T16:30:00", "2017-05-26T18:30:00", "BancomatBCN", false);
//        services.generateDataFacts(5000, "2017-05-26T18:45:00", "2017-05-26T20:30:00", "McDo", false);
//        services.generateDataFacts(25500, "2017-05-26T20:45:00", "2017-05-26T21:30:00", "Coop", false);
//        services.generateDataFacts(25000, "2017-05-26T21:45:00", "2017-05-26T23:30:00", "PasserelleMillenaire", false);
//        services.generateDataFacts(35000, "2017-05-26T23:45:00", "2017-05-27T00:30:00", "HEG", true);
    }

}
