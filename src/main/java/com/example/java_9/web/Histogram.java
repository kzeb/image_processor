package com.example.java_9.web;

import java.util.HashMap;
import java.util.Map;

public class Histogram {
    private Map<String, String> rhist = new HashMap<>();
    private Map<String, String> ghist = new HashMap<>();
    private Map<String, String> bhist = new HashMap<>();

    public Histogram() {
    }

    public Map<String, String> getRhist() {
        return rhist;
    }

    public void setRhist(Map<String, String> rhist) {
        this.rhist = rhist;
    }

    public Map<String, String> getGhist() {
        return ghist;
    }

    public void setGhist(Map<String, String> ghist) {
        this.ghist = ghist;
    }

    public Map<String, String> getBhist() {
        return bhist;
    }

    public void setBhist(Map<String, String> bhist) {
        this.bhist = bhist;
    }
}
