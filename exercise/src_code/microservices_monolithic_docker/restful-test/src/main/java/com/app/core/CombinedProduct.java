package com.app.core;

public class CombinedProduct {

    public String id;
    public String name;
    public Double price;
    public String productId;
    public Double sku;
    public Double amountAvailable;

    public CombinedProduct() {}

    public CombinedProduct(String id, String name, double price, String productId
    , double sku, double amountAvailable) {
        super();
        this.id = id;
        this.name = name;
        this.price = price;
        this.productId = productId;
        this.sku = sku;
        this.amountAvailable = amountAvailable;
    }

    @Override
    public String toString() {
        return "id[" + id + "] " +
                "name[" + name + "] " +
                "price[" + price + "] " +
                "productId[" + productId + "] " +
                "sku[" + sku + "] " +
                "amountAvailable[" + amountAvailable + "] "
                ;
    }

}
