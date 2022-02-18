package com.manacher.hammer.models;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private String userId;
    private String dpUrl;
    private String gender;
    private String country;
    private int age;
    private int chips;

    private List<Integer> ownBottles = new ArrayList<>();

    public User() {
    }

    public List<Integer> getOwnBottles() {
        return ownBottles;
    }

    public void setOwnBottles(List<Integer> ownBottles) {
        this.ownBottles = ownBottles;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getChips() {
        return chips;
    }

    public void setChips(int chips) {
        this.chips = chips;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDpUrl() {
        return dpUrl;
    }

    public void setDpUrl(String dpUrl) {
        this.dpUrl = dpUrl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
