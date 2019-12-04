package com.tetraval.mochashivendor.chashivendor.model;

public class ChashiCategoryModel {

    String c_uid;
    String c_name;

    public ChashiCategoryModel(String c_uid, String c_name) {
        this.c_uid = c_uid;
        this.c_name = c_name;
    }

    public ChashiCategoryModel(){

    }

    public String getC_uid() {
        return c_uid;
    }

    public String getC_name() {
        return c_name;
    }
}
