/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hearc.ig.tb.cityFlow.business;

import java.math.BigDecimal;

/**
 *
 * @author Dimitri
 * Classe Business représentant un appareil collectant les données.
 */
public class Device {
    private Integer id;
    private String name;
    private BigDecimal latitude;
    private BigDecimal longitdue;

    public Device(Integer id, String name, BigDecimal latitude, BigDecimal longitdue) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitdue = longitdue;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitdue() {
        return longitdue;
    }

    public void setLongitdue(BigDecimal longitdue) {
        this.longitdue = longitdue;
    }
    
    
}
