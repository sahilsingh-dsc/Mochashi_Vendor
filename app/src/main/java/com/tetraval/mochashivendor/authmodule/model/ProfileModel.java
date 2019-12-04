package com.tetraval.mochashivendor.authmodule.model;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;


@IgnoreExtraProperties
public class ProfileModel {

    @ServerTimestamp
    Date p_timestamp;
    String p_uid;
    String p_image;
    String p_fname;
    String p_lname;
    String p_email;
    String p_address;
    double p_lat;
    double p_long;
    String p_active;

    public ProfileModel(Date p_timestamp, String p_uid, String p_image, String p_fname, String p_lname, String p_email, String p_address, double p_lat, double p_long, String p_active) {
        this.p_timestamp = p_timestamp;
        this.p_uid = p_uid;
        this.p_image = p_image;
        this.p_fname = p_fname;
        this.p_lname = p_lname;
        this.p_email = p_email;
        this.p_address = p_address;
        this.p_lat = p_lat;
        this.p_long = p_long;
        this.p_active = p_active;
    }

    public ProfileModel(){
    }

    public Date getP_timestamp() {
        return p_timestamp;
    }

    public void setP_timestamp(Date p_timestamp) {
        this.p_timestamp = p_timestamp;
    }

    public String getP_uid() {
        return p_uid;
    }

    public void setP_uid(String p_uid) {
        this.p_uid = p_uid;
    }

    public String getP_image() {
        return p_image;
    }

    public void setP_image(String p_image) {
        this.p_image = p_image;
    }

    public String getP_fname() {
        return p_fname;
    }

    public void setP_fname(String p_fname) {
        this.p_fname = p_fname;
    }

    public String getP_lname() {
        return p_lname;
    }

    public void setP_lname(String p_lname) {
        this.p_lname = p_lname;
    }

    public String getP_email() {
        return p_email;
    }

    public void setP_email(String p_email) {
        this.p_email = p_email;
    }

    public String getP_address() {
        return p_address;
    }

    public void setP_address(String p_address) {
        this.p_address = p_address;
    }

    public double getP_lat() {
        return p_lat;
    }

    public void setP_lat(double p_lat) {
        this.p_lat = p_lat;
    }

    public double getP_long() {
        return p_long;
    }

    public void setP_long(double p_long) {
        this.p_long = p_long;
    }

    public String getP_active() {
        return p_active;
    }

    public void setP_active(String p_active) {
        this.p_active = p_active;
    }
}
