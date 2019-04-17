package com.app.core;

import org.json.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;
import java.util.*;

public class RetailController {

    public static int dbProduct = 1;
    public static int dbStock = 2;
    public static String redis_host = "redis";
    public static int redis_port = 6379;
    public static Jedis redis = null;

    static {
        connectToRedis(redis_host, redis_port);
    }

    public static Jedis connectToRedis(String host, int port) {
        redis = new Jedis(host, port);
        while (true) {
            try {
                redis.select(dbProduct);
                Set<String> keys = redis.keys("*");
                System.out.println("user after keys");
                System.out.println(keys);

                redis.select(dbStock);
                keys = redis.keys("*");
                System.out.println("product after keys");
                System.out.println(keys);
                break;
            } catch (JedisConnectionException e) {
                System.err.println("Waiting for redis retail controller");
            }
        }
        System.err.println("Connected to redis");
        return redis;
    }

    public RetailController() {}

    public static List<Stock> getAllStocks() {
        List<Stock> stocks = new ArrayList<>();
        redis.select(dbStock);

        Set<String> keys = redis.keys("*");
        for (String key : keys) {
            Stock stock = getStock(key);
            stocks.add(stock);
        }
        return stocks;
    }
    public static Stock getStock(String productId) {
        Stock stock = new Stock();
        redis.select(dbStock);
        try {
            JSONObject json = new JSONObject(redis.get(productId));
            stock.productId = json.getString("productId");
            stock.sku = json.getDouble("sku");
            stock.amountAvailable = json.getDouble("amountAvailable");
        } catch (Exception ex) {
            System.err.println("Error in getting user: " + productId);
        }
        return stock;
    }
    public static String setStock(String productId, double sku, double amountAvailable) {
        String res = null;

        JSONObject json = new JSONObject();
        json.put("productId", productId );
        json.put("sku", sku);
        json.put("amountAvailable", amountAvailable );

        redis.select(dbStock);
        try {
            res = redis.set(productId, json.toString());
        } catch (Exception ex) {
            System.err.println("Error in setting stock: " + productId);
        }
        return res;
    }

    public static List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        redis.select(dbProduct);

        Set<String> keys = redis.keys("*");
        for (String key : keys) {
            Product product = getProduct(key);
            products.add(product);
        }
        return products;
    }
    public static Product getProduct(String id) {
        Product product = new Product();
        redis.select(dbProduct);

        try {
            JSONObject json = new JSONObject(redis.get(id));
            product.id = json.getString("id");
            product.name = json.getString("name");
            product.price = json.getDouble("price");
        } catch (Exception ex) {
            System.err.println("Error in getting product: " + id);
        }
        return product;
    }
    public static String setProduct(String id, String name, double price) {
        String res = null;

        JSONObject json = new JSONObject();
        json.put("id", id );
        json.put("name", name);
        json.put("price", price );

        redis.select(dbProduct);
        try {
            res = redis.set(id, json.toString());
        } catch (Exception ex) {
            System.err.println("Error in setting product: " + id);
        }
        return res;
    }

    public static CombinedProduct combinedProduct(String productId, String id) {

        Stock stock = getStock(productId);
        Product product = getProduct(id);

        // output json
        CombinedProduct combinedProduct = new CombinedProduct();
        combinedProduct.productId = stock.productId;
        combinedProduct.sku = stock.sku;
        combinedProduct.amountAvailable = stock.amountAvailable;
        combinedProduct.id = product.id;
        combinedProduct.price = product.price;
        combinedProduct.name = product.name;

        return combinedProduct;
    }

    public static void main(String[] args) {
        RetailController rc = new RetailController();
        String stockid = "1";
        String productid = "1";

        System.out.println("\n\t Get One User");
        Stock q1 = rc.getStock(stockid);
        System.out.println(q1);

//        System.out.println("\n\t Get All Users");
//        List<JSONObject> jsons = rc.getAllUsers();
//        for (JSONObject json : jsons) {
//            System.out.println(json);
//        }

        System.out.println("\n\t Get One Product");
        Product q2 = rc.getProduct(productid);
        System.out.println(q2);

//        System.out.println("\n\t Get All Products");
//        List<JSONObject> products = rc.getAllProducts();
//        for (JSONObject product : products) {
//            System.out.println(product);
//        }

        CombinedProduct res = rc.combinedProduct(stockid, productid);
        System.out.println(res);

    }

}
