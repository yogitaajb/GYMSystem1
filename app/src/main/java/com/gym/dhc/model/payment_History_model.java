package com.gym.dhc.model;

public class payment_History_model {

    private String id;
    private String payment_id;
    private String paid_price;
    private String pack_id;
    private String pack_name;
    private String pack_price;
    private String datetime;
    private String status;
    private String payment_type;
    private String trainer_name;


    public payment_History_model(String id, String payment_id, String paid_price,String pack_id, String pack_name,
                                 String pack_price, String datetime,String status,String payment_type,String trainer_name) {
        this.id = id;
        this.payment_id = payment_id;
        this.paid_price = paid_price;
        this.pack_id = pack_id;
        this.pack_name = pack_name;
        this.pack_price = pack_price;
        this.datetime = datetime;
        this.status=status;
        this.payment_type=payment_type;
        this.trainer_name=trainer_name;


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

    public String getPaid_price() {
        return paid_price;
    }

    public void setPaid_price(String paid_price) {
        this.paid_price = paid_price;
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

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getTrainer_name() {
        return trainer_name;
    }

    public void setTrainer_name(String trainer_name) {
        this.trainer_name = trainer_name;
    }
}
