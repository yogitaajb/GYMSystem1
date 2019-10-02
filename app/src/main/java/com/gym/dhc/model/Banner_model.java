package com.gym.dhc.model;

public class Banner_model {

    private String id;
    private String image_url;


    public Banner_model(String id, String image_url){
        this.id = id;
        this.image_url = image_url;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
