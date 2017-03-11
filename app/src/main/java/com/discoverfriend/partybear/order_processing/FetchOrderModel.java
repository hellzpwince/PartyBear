package com.discoverfriend.partybear.order_processing;

/**
 * Created by mukesh on 02/02/17.
 */

public class FetchOrderModel {
    private String date, coupon_code, order_status, deliveryaddress, ordername, paymentid, paymentstatus, uid;
    private Long total;

    public FetchOrderModel(String date, String coupon_code, String order_status, String deliveryaddress, String ordername, String paymentid, String paymentstatus, String uid, Long total) {
        this.date = date;
        this.coupon_code = coupon_code;
        this.order_status = order_status;
        this.deliveryaddress = deliveryaddress;
        this.ordername = ordername;
        this.paymentid = paymentid;
        this.paymentstatus = paymentstatus;
        this.uid = uid;
        this.total = total;
    }

    public FetchOrderModel() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCoupon_code() {
        return coupon_code;
    }

    public void setCoupon_code(String coupon_code) {
        this.coupon_code = coupon_code;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getDeliveryaddress() {
        return deliveryaddress;
    }

    public void setDeliveryaddress(String deliveryaddress) {
        this.deliveryaddress = deliveryaddress;
    }

    public String getOrdername() {
        return ordername;
    }

    public void setOrdername(String ordername) {
        this.ordername = ordername;
    }

    public String getPaymentid() {
        return paymentid;
    }

    public void setPaymentid(String paymentid) {
        this.paymentid = paymentid;
    }

    public String getPaymentstatus() {
        return paymentstatus;
    }

    public void setPaymentstatus(String paymentstatus) {
        this.paymentstatus = paymentstatus;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
