/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hearc.ig.tb.cityFlow.utilitaire;

import ch.hearc.ig.tb.cityFlow.business.Device;
import ch.hearc.ig.tb.cityFlow.business.Person;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dimitri
 * Classe Utilitaire utilisée pour générer des datas et stockées des données
 */
public class Utilitaire {
    
    private Map<String,Device> devices;
    private Map<Integer, Person> people;
    private List<String> manufacturers;
    private List<String> genders;
    private List<String> locomotions;
    private Random random;
    private Calendar calendar;
    private DateFormat formatter;
    private static Utilitaire instance;

    /**
     * Constructor
     */
    private Utilitaire() {
        this.devices = new HashMap<>();
        this.people = new HashMap<>();
        this.manufacturers = new ArrayList<>(Arrays.asList("Apple","Huawei","Sony","Samsung","Nokia","LG"));
        this.genders = new ArrayList<>(Arrays.asList("Homme","Femme"));
        this.locomotions = new ArrayList<>(Arrays.asList("Pied","Voiture","Vélo","Camion"));
        this.random = new Random();
        this.calendar = Calendar.getInstance();
        formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    }
    
    public static Utilitaire getInstance(){
        if(instance == null){
            instance = new Utilitaire();
        }       
        return instance;     
    }

    public List<String> getLocomotions() {
        return locomotions;
    }

    public Map<String, Device> getDevices() {
        return devices;
    }

    public Map<Integer, Person> getPeople() {
        return people;
    }

    public List<String> getManufacturers() {
        return manufacturers;
    }

    public List<String> getGenders() {
        return genders;
    }
    /**
     * Generate devices for inputing random Data
     */
    public void generateDevice(){
        this.devices.put("Coop", new Device(1,"Coop",new BigDecimal("46.996848"), new BigDecimal("6.936697")));
        this.devices.put("HEG", new Device(2,"HEG",new BigDecimal("46.998502"), new BigDecimal("6.941391")));
        this.devices.put("PasserelleMillenaire", new Device(3,"PasserelleMillenaire",new BigDecimal("46.998979"), new BigDecimal("6.943006")));
        this.devices.put("McDo", new Device(4,"McDo",new BigDecimal("46.996368"), new BigDecimal("6.935604")));
        this.devices.put("PosteGare", new Device(5,"PosteGare",new BigDecimal("46.996394"), new BigDecimal("6.936849")));
        this.devices.put("OFS", new Device(6,"OFS",new BigDecimal("46.997027"), new BigDecimal("6.937938")));
        this.devices.put("BancomatBCN", new Device(7,"BancomatBCN",new BigDecimal("46.996398"), new BigDecimal("6.936318")));
    }
    /**
     * Generate random Person
     * @param numberOfPeople number of Person to generate
     */
    public void generatePerson(Integer numberOfPeople){
        for(int i = 0; i < numberOfPeople; i++){
            this.people.put(i, new Person(randomMacAdresse(),this.manufacturers.get(random.nextInt(manufacturers.size())),this.genders.get(random.nextInt(genders.size())),random.nextInt(80),locomotions.get(random.nextInt(locomotions.size()))));
        }
    }
    /**
     * Method for getting a random UUID
     * @return a random UUID
     */
    public String randomMacAdresse(){
        return UUID.randomUUID().toString();
    }
    
    /**
     * Method for return a random Date between two dates ranges
     * @param start the date to start
     * @param end the date to end
     * @return a random date between two dates ranges
     */
    public Date randomDate(String start, String end){
        try {
            calendar.setTime(formatter.parse(start));
            Long value1 = calendar.getTimeInMillis();
            calendar.setTime(formatter.parse(end));
            Long value2 = calendar.getTimeInMillis();
            
            long value3 = (long)(value1 + Math.random()*(value2 - value1));
            calendar.setTimeInMillis(value3);
            // System.out.println(formatter.format(calendar.getTime()));
            
        } catch (ParseException ex) {
            Logger.getLogger(Utilitaire.class.getName()).log(Level.SEVERE, null, ex);
        }
        return calendar.getTime();
    }
}
