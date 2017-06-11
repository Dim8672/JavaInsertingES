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
package ch.hearc.ig.tb.cityFlow.business;

import java.util.Date;

/**
 *
 * @author dimitri.mella
 * Classe Business représentant un trajet effectué entre deux appareil par une personne
 */
public class Traject {
    
    private Device from;
    private Device to;
    private Person person;
    private Date fromFirstSeen;
    private Date fromLastSeen;
    private Date toFirstSeen;
    private Date toLastSeen;

    public Traject(Device from, Device to, Person person) {
        this.from = from;
        this.to = to;
        this.person = person;
    }

    public Traject(Device from, Device to, Person person, Date fromFirstSeen, Date fromLastSeen, Date toFirstSeen, Date toLastSeen) {
        this.from = from;
        this.to = to;
        this.person = person;
        this.fromFirstSeen = fromFirstSeen;
        this.fromLastSeen = fromLastSeen;
        this.toFirstSeen = toFirstSeen;
        this.toLastSeen = toLastSeen;
    }

    public Date getFromFirstSeen() {
        return fromFirstSeen;
    }

    public void setFromFirstSeen(Date fromFirstSeen) {
        this.fromFirstSeen = fromFirstSeen;
    }

    public Date getFromLastSeen() {
        return fromLastSeen;
    }

    public void setFromLastSeen(Date fromLastSeen) {
        this.fromLastSeen = fromLastSeen;
    }

    public Date getToFirstSeen() {
        return toFirstSeen;
    }

    public void setToFirstSeen(Date toFirstSeen) {
        this.toFirstSeen = toFirstSeen;
    }

    public Date getToLastSeen() {
        return toLastSeen;
    }

    public void setToLastSeen(Date toLastSeen) {
        this.toLastSeen = toLastSeen;
    }

    public Traject() {
    }

    public Traject(Device from, Device to) {
        this.from = from;
        this.to = to;
    }

    public Device getFrom() {
        return from;
    }

    public void setFrom(Device from) {
        this.from = from;
    }

    public Device getTo() {
        return to;
    }

    public void setTo(Device to) {
        this.to = to;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
    
    
}
