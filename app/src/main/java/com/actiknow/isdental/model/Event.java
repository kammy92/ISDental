package com.actiknow.isdental.model;

/**
 * Created by l on 27/04/2017.
 */

public class Event {
    int id;
    String program_name,doctor_name,date,time, location;

    public Event (int id, String program_name, String doctor_name, String date, String time, String location) {
        this.id=id;
        this.program_name=program_name;
        this.doctor_name=doctor_name;
        this.date=date;
        this.time=time;
        this.location = location;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProgram_name() {
        return program_name;
    }

    public void setProgram_name(String program_name) {
        this.program_name = program_name;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    
    public String getLocation () {
        return location;
    }
    
    public void setLocation (String location) {
        this.location = location;
    }
}
