package com.techpalle.poc;

/**
 * Created by skillgun on 1/4/2016.
 */
public class PaypalData {
    private String amount,currency_code,shipping,subtotal,tax,
    short_description,bn_code,item_name,item_price,
    currency,sku,state,paymentId,createTime,transactionID;

    //palle specific data
    private String course, course_id, email, palle_order_id;

    public String getPalle_order_id() {
        return palle_order_id;
    }

    public void setPalle_order_id(String palle_order_id) {
        this.palle_order_id = palle_order_id;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public PaypalData(String amount, String currency_code, String shipping, String subtotal, String tax, String short_description, String bn_code, String item_name, String item_price, String currency, String sku, String state, String paymentId, String createTime, String transactionID, String course_name, String course_id, String email, String palle_order_id) {
        this.amount = amount;
        this.currency_code = currency_code;
        this.shipping = shipping;
        this.subtotal = subtotal;
        this.tax = tax;
        this.short_description = short_description;
        this.bn_code = bn_code;
        this.item_name = item_name;
        this.item_price = item_price;
        this.currency = currency;
        this.sku = sku;
        this.state = state;
        this.paymentId = paymentId;
        this.createTime = createTime;
        this.transactionID = transactionID;
        //palle specific
        this.course = course_name;
        this.course_id = course_id;
        this.email = email;
        this.palle_order_id = palle_order_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency_code() {
        return currency_code;
    }

    public void setCurrency_code(String currency_code) {
        this.currency_code = currency_code;
    }

    public String getShipping() {
        return shipping;
    }

    public void setShipping(String shipping) {
        this.shipping = shipping;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    public String getBn_code() {
        return bn_code;
    }

    public void setBn_code(String bn_code) {
        this.bn_code = bn_code;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }
}
