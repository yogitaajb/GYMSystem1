package com.gym.dhc.model;

public class Packages_model {

    private String id;
    private String pack_name;
    private String price;
    private String description;
private String duration;

    public Packages_model(String id, String pack_name, String price,String description,String duration){
        this.id = id;
        this.pack_name = pack_name;
        this.price = price;
        this.description = description;
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPack_name() {
        return pack_name;
    }

    public void setPack_name(String pack_name) {
        this.pack_name = pack_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
