package com.gym.dhc.model;

public class Receipt_Model {

    private String id;
    private String payment_id;
    private String pack_id;
    private String pack_name;
    private String pack_price;
    private String added_date;
    private String payment_type;
    private String personal_traning;
    private String traning_price;
    private String total;


    public Receipt_Model(String id, String payment_id, String pack_id, String pack_name, String pack_price,
                         String added_date,String payment_type,String personal_traning,String traning_price,String total) {
        this.id = id;
        this.payment_id = payment_id;
        this.pack_id = pack_id;
        this.pack_name = pack_name;
        this.pack_price = pack_price;
        this.added_date = added_date;
        this.payment_type= payment_type;
        this.personal_traning=personal_traning;
        this.traning_price = traning_price;
        this.total= total;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public String getPack_id() {
        return pack_id;
    }

    public void setPack_id(String pack_id) {
        this.pack_id = pack_id;
    }

    public String getPack_name() {
        return pack_name;
    }

    public void setPack_name(String pack_name) {
        this.pack_name = pack_name;
    }

    public String getPack_price() {
        return pack_price;
    }

    public void setPack_price(String pack_price) {
        this.pack_price = pack_price;
    }

    public String getAdded_date() {
        return added_date;
    }

    public void setAdded_date(String added_date) {
        this.added_date = added_date;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getTraning_price() {
        return traning_price;
    }

    public void setTraning_price(String traning_price) {
        this.traning_price = traning_price;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPersonal_traning() {
        return personal_traning;
    }

    public void setPersonal_traning(String personal_traning) {
        this.personal_traning = personal_traning;
    }
}
