package com.sensegarden.sensegardenplaydev.models.user;

import java.io.Serializable;

public class PrimaryUser implements Serializable {
    private String id;
    private int age;
    private String command;
    private String created_by;
    private String current_flow;
    private String full_name;
    private String last_active;
    private String main_contact;
    private String photo;
    private String place_of_birth;
    private String qr_code;
    private String reg_number;
    private String sense_garden_id;
    private String status;
    private String type;

    public PrimaryUser(String id, int age, String command, String created_by, String current_flow, String full_name, String last_active, String main_contact, String photo, String place_of_birth, String qr_code, String reg_number, String sense_garden_id, String status, String type) {
        this.id = id;
        this.age = age;
        this.command = command;
        this.created_by = created_by;
        this.current_flow = current_flow;
        this.full_name = full_name;
        this.last_active = last_active;
        this.main_contact = main_contact;
        this.photo = photo;
        this.place_of_birth = place_of_birth;
        this.qr_code = qr_code;
        this.reg_number = reg_number;
        this.sense_garden_id = sense_garden_id;
        this.status = status;
        this.type = type;
    }

    public PrimaryUser() {
    }

    public String getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public String getCommand() {
        return command;
    }

    public String getCreated_by() {
        return created_by;
    }

    public String getCurrent_flow() {
        return current_flow;
    }

    public String getFull_name() {
        return full_name;
    }

    public String getLast_active() {
        return last_active;
    }

    public String getMain_contact() {
        return main_contact;
    }

    public String getPhoto() {
        return photo;
    }

    public String getPlace_of_birth() {
        return place_of_birth;
    }

    public String getQr_code() {
        return qr_code;
    }

    public String getReg_number() {
        return reg_number;
    }

    public String getSense_garden_id() {
        return sense_garden_id;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }
}
