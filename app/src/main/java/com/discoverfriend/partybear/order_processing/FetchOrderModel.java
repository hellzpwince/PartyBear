package com.discoverfriend.partybear.order_processing;

/**
 * Created by mukesh on 02/02/17.
 */

public class FetchOrderModel {
    private String date;
    private String orderstatus;
    private String paymentstatus;
    private Long total;
    private String uid;
    private String ordername;

    public String getPaymentmethod() {
        return paymentmethod;
    }

    public FetchOrderModel(String date, String orderstatus, String paymentstatus, Long total, String uid, String ordername, String paymentmethod) {
        this.date = date;
        this.orderstatus = orderstatus;
        this.paymentstatus = paymentstatus;
        this.total = total;
        this.uid = uid;
        this.ordername = ordername;
        this.paymentmethod = paymentmethod;
    }

    public String getOrdername() {

        return ordername;
    }

    public void setOrdername(String ordername) {
        this.ordername = ordername;
    }

    public void setPaymentmethod(String paymentmethod) {
        this.paymentmethod = paymentmethod;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    private String paymentmethod;

    public FetchOrderModel() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(String orderstatus) {
        this.orderstatus = orderstatus;
    }

    public String getPaymentstatus() {
        return paymentstatus;
    }

    public void setPaymentstatus(String paymentstatus) {
        this.paymentstatus = paymentstatus;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
