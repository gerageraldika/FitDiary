package com.carpediemsolution.fitdiary.model;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Юлия on 05.03.2017.
 */

public class ReminderCounter {

    private Date date;
    private UUID uuid;
    private int counterFlag = 0;

    public ReminderCounter(UUID id) {
        uuid = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public int getCounterFlag() {
        return counterFlag;
    }

    public void setCounterFlag(int counterFlag) {
        this.counterFlag = counterFlag;
    }
}
