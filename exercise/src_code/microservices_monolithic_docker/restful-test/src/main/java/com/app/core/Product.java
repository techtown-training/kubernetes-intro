package com.app.core;

public class Product {

    public String id;
    public String name;
    public Double price;

    public Product() {}

    public Product(String _id, String _name, double _price) {
        this.id = _id;
        this.name = _name;
        this.price = _price;
    }

    @Override
    public String toString() {
        return "productId[" + id + "] productName[" + name + "] with price[" + price + "]";
    }


}
