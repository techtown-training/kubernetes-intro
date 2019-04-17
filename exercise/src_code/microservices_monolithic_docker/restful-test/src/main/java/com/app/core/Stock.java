package com.app.core;

public class Stock {

    public String productId;
    public Double sku;
    public Double amountAvailable;

    public Stock() {}

    public Stock(String productId, double sku, double amountAvailable) {
        super();
        this.productId = productId;
        this.sku = sku;
        this.amountAvailable = amountAvailable;
    }

    @Override
    public String toString() {
        return "productId[" + productId + "] sku[" + sku + "] amountAvailable[" + amountAvailable + "]";
    }

}
