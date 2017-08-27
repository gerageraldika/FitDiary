package com.carpediemsolution.fitdiary.model;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Юлия on 03.03.2017.
 */

public class Reminder {

    private Date date;
    private String reminding;
    private UUID uuid;
    private int repeatedReminderFlag = 0;
    private boolean counter;

    public  Reminder(){
        this(UUID.randomUUID());
    }

    public Reminder (UUID id){
        uuid = id;
    }

    public Date getDate() {
        return date;
    }

    public String getReminding() {
        return reminding;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setReminding(String reminding) {
        this.reminding = reminding;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public int getFlag() {return repeatedReminderFlag;}

    public void setFlag(int flag) {this.repeatedReminderFlag = flag;}

    public boolean isCounter() {
        return counter;
    }

    public void setCounter(boolean counter) {
        this.counter = counter;
    }
}
