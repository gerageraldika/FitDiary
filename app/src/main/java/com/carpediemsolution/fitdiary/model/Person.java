package com.carpediemsolution.fitdiary.model;


/**
 * Created by Юлия on 22.02.2017.
 */

public class Person {

    private String personName;
    private String personHeight;
    private String personWeight;

    private static Person person;

    private Person() {
    }

    public static Person get() {
        if (person == null) {
            person = new Person();
        }
        return person;
    }

    public String getPersonName() {
        return personName;
    }

    public String getPersonHeight() {
        return personHeight;
    }

    public String getPersonWeight() {
        return personWeight;
    }


    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public void setPersonHeight(String personHeight) {
        this.personHeight = personHeight;
    }

    public void setPersonWeight(String personWeight) {
        this.personWeight = personWeight;
    }
}
