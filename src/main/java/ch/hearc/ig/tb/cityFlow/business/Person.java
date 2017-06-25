/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hearc.ig.tb.cityFlow.business;

/**
 *
 * @author Dimitri
 * Classes business représentant une personne enregistrée par un appareil
 */
public class Person {
    private String macAdresse;
    private String manufacturer;
    private String locomotion;
    private String gender;
    private Integer age;

    public Person(String macAdresse, String manufacturer, String gender, Integer age, String locomotion) {
        this.macAdresse = macAdresse;
        this.manufacturer = manufacturer;
        this.gender = gender;
        this.age = age;
        this.locomotion = locomotion;
    }

    public String getLocomotion() {
        return locomotion;
    }

    public void setLocomotion(String locomotion) {
        this.locomotion = locomotion;
    }

    public String getMacAdresse() {
        return macAdresse;
    }

    public void setMacAdresse(String macAdresse) {
        this.macAdresse = macAdresse;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
    
    
}
