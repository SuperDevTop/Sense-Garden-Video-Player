package com.sensegarden.sensegardenplaydev.models.user;

public class Caregiver {
    private final String email;
    private final String full_name;
    private final String id;
    private final String last_active;
    private final String mobile_number;
    private final String photo;
    private final String qr_code;
    private final String reg_number;
    private final String sense_garden_id;
    private final String start;
    private final String status;
    private final String type;

    public Caregiver(String email, String fullName, String id, String lastActive, String mobileNumber, String photo, String qrCode, String regNumber, String senseGardenId, String start, String status, String type) {
        this.email = email;
        this.full_name = fullName;
        this.id = id;
        this.last_active = lastActive;
        this.mobile_number = mobileNumber;
        this.photo = photo;
        this.qr_code = qrCode;
        this.reg_number = regNumber;
        this.sense_garden_id = senseGardenId;
        this.start = start;
        this.status = status;
        this.type = type;
    }

    public Caregiver() {
        email = "";
        full_name = "";
        id = "";
        last_active = "";
        mobile_number = "";
        photo = "";
        qr_code = "";
        reg_number = "";
        sense_garden_id = "";
        start = "";
        status = "";
        type = "";
    }

    public String getEmail() {
        return email;
    }

    public String getFull_name() {
        return full_name;
    }

    public String getId() {
        return id;
    }

    public String getLast_active() {
        return last_active;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public String getPhoto() {
        return photo;
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

    public String getStart() {
        return start;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }
}
