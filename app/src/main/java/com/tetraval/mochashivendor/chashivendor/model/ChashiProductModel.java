package com.tetraval.mochashivendor.chashivendor.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class ChashiProductModel {

    @ServerTimestamp
    private Date p_timestamp;
    private String p_uid;
    private String p_image1;
    private String p_image2;
    private String p_image3;
    private String p_image4;
    private String p_category;
    private String p_hquantity;
    private String p_bquantity;
    private String p_unit;
    private String p_rate;
    private String p_homedelivery;
    private String p_chashi_uid;
    private String p_chashi_name;
    private String p_chashi_photo;
    private String p_chashi_rating;
    private String p_chashi_address;
    private String p_delivery_status;
    private String p_lat;
    private String p_long;
    private String p_received_qty;


    public ChashiProductModel() {
    }

    public ChashiProductModel(Date p_timestamp, String p_uid, String p_image1, String p_image2, String p_image3, String p_image4, String p_category, String p_hquantity, String p_bquantity, String p_unit, String p_rate, String p_homedelivery, String p_chashi_uid, String p_chashi_name, String p_chashi_photo, String p_chashi_rating, String p_chashi_address, String p_delivery_status, String p_lat, String p_long, String p_received_qty) {
        this.p_timestamp = p_timestamp;
        this.p_uid = p_uid;
        this.p_image1 = p_image1;
        this.p_image2 = p_image2;
        this.p_image3 = p_image3;
        this.p_image4 = p_image4;
        this.p_category = p_category;
        this.p_hquantity = p_hquantity;
        this.p_bquantity = p_bquantity;
        this.p_unit = p_unit;
        this.p_rate = p_rate;
        this.p_homedelivery = p_homedelivery;
        this.p_chashi_uid = p_chashi_uid;
        this.p_chashi_name = p_chashi_name;
        this.p_chashi_photo = p_chashi_photo;
        this.p_chashi_rating = p_chashi_rating;
        this.p_chashi_address = p_chashi_address;
        this.p_delivery_status = p_delivery_status;
        this.p_lat = p_lat;
        this.p_long = p_long;
        this.p_received_qty = p_received_qty;
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

    public String getP_image1() {
        return p_image1;
    }

    public void setP_image1(String p_image1) {
        this.p_image1 = p_image1;
    }

    public String getP_image2() {
        return p_image2;
    }

    public void setP_image2(String p_image2) {
        this.p_image2 = p_image2;
    }

    public String getP_image3() {
        return p_image3;
    }

    public void setP_image3(String p_image3) {
        this.p_image3 = p_image3;
    }

    public String getP_image4() {
        return p_image4;
    }

    public void setP_image4(String p_image4) {
        this.p_image4 = p_image4;
    }

    public String getP_category() {
        return p_category;
    }

    public void setP_category(String p_category) {
        this.p_category = p_category;
    }

    public String getP_hquantity() {
        return p_hquantity;
    }

    public void setP_hquantity(String p_hquantity) {
        this.p_hquantity = p_hquantity;
    }

    public String getP_bquantity() {
        return p_bquantity;
    }

    public void setP_bquantity(String p_bquantity) {
        this.p_bquantity = p_bquantity;
    }

    public String getP_unit() {
        return p_unit;
    }

    public void setP_unit(String p_unit) {
        this.p_unit = p_unit;
    }

    public String getP_rate() {
        return p_rate;
    }

    public void setP_rate(String p_rate) {
        this.p_rate = p_rate;
    }

    public String getP_homedelivery() {
        return p_homedelivery;
    }

    public void setP_homedelivery(String p_homedelivery) {
        this.p_homedelivery = p_homedelivery;
    }

    public String getP_chashi_uid() {
        return p_chashi_uid;
    }

    public void setP_chashi_uid(String p_chashi_uid) {
        this.p_chashi_uid = p_chashi_uid;
    }

    public String getP_chashi_name() {
        return p_chashi_name;
    }

    public void setP_chashi_name(String p_chashi_name) {
        this.p_chashi_name = p_chashi_name;
    }

    public String getP_chashi_photo() {
        return p_chashi_photo;
    }

    public void setP_chashi_photo(String p_chashi_photo) {
        this.p_chashi_photo = p_chashi_photo;
    }

    public String getP_chashi_rating() {
        return p_chashi_rating;
    }

    public void setP_chashi_rating(String p_chashi_rating) {
        this.p_chashi_rating = p_chashi_rating;
    }

    public String getP_chashi_address() {
        return p_chashi_address;
    }

    public void setP_chashi_address(String p_chashi_address) {
        this.p_chashi_address = p_chashi_address;
    }

    public String getP_delivery_status() {
        return p_delivery_status;
    }

    public void setP_delivery_status(String p_delivery_status) {
        this.p_delivery_status = p_delivery_status;
    }

    public String getP_lat() {
        return p_lat;
    }

    public void setP_lat(String p_lat) {
        this.p_lat = p_lat;
    }

    public String getP_long() {
        return p_long;
    }

    public void setP_long(String p_long) {
        this.p_long = p_long;
    }

    public String getP_received_qty() {
        return p_received_qty;
    }

    public void setP_received_qty(String p_received_qty) {
        this.p_received_qty = p_received_qty;
    }
}
