package com.discoverfriend.partybear.Product;

/**
 * Created by mukesh on 11/02/17.
 */

public class ProductModel {
    private String available, deliveryid, description, name, imageurl, discount, producttype;
    private int baseprice, price, qty;

    public ProductModel() {
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getDeliveryid() {
        return deliveryid;
    }

    public void setDeliveryid(String deliveryid) {
        this.deliveryid = deliveryid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getProducttype() {
        return producttype;
    }

    public void setProducttype(String producttype) {
        this.producttype = producttype;
    }

    public long getBaseprice() {
        return baseprice;
    }

    public void setBaseprice(int baseprice) {
        this.baseprice = baseprice;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public long getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public ProductModel(String available, String deliveryid, String description, String name, String imageurl, String discount, String producttype, int baseprice, int price, int qty) {
        this.available = available;
        this.deliveryid = deliveryid;
        this.description = description;
        this.name = name;
        this.imageurl = imageurl;
        this.discount = discount;
        this.producttype = producttype;
        this.baseprice = baseprice;
        this.price = price;
        this.qty = qty;
    }
}
