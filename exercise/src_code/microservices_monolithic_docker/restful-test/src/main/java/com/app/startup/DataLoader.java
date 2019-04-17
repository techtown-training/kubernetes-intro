package com.app.startup;

import org.json.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DataLoader {

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
                System.err.println("Waiting for redis dataloader");
            }
        }
        System.err.println("Connected to redis");
        return redis;
    }

    public static Map<String, JSONObject> getProductData() {
        System.out.println("Getting Product Data...");
        Map<String, JSONObject> list = new HashMap<>();

        String text = "1,'\"Widget\", 1.2\n" +
                "2,'\"Sprocket\", 4.10\n" +
                "3,'\"Anvil\", 45.50\n" +
                "4,'\"Cogs\", 180.0\n" +
                "5,'\"Multitool\", 154.10\n";

        String[] records = text.split("\n");
        for (String record : records) {
            JSONObject json = new JSONObject();
            String[] parts = record.split(",");

            String product_id = parts[0].trim();
            String product_name = parts[1].trim();
            double product_price = Double.parseDouble(parts[2].trim());

            json.put("id", product_id );
            json.put("name", product_name);
            json.put("price", product_price );
            list.put(product_id, json);
        }
        return list;
    }

    public static Map<String, JSONObject> getStockData() {
        System.out.println("Getting User Data...");
        Map<String, JSONObject> list = new HashMap<>();

        String text = "1, 12345678, 5\n" +
                "2, 34567890, 2\n" +
                "3, 54326745, 999\n" +
                "4, 93847614, 0\n" +
                "5, 11856388, 1\n";

        String[] records = text.split("\n");
        for (String record : records) {
            JSONObject json = new JSONObject();
            String[] parts = record.split(",");

            String productId = parts[0].trim();
            double sku = Double.parseDouble(parts[1].trim());
            double amountAvailable = Double.parseDouble(parts[2].trim());
            json.put("productId", productId );
            json.put("sku", sku);
            json.put("amountAvailable", amountAvailable );
            list.put(productId, json);
        }
        return list;
    }

    public static void uploadData(Map<String, JSONObject> data, int dbNum) {
        System.out.println("Uploading User and Product Data...");
        redis.select(dbNum);
        for (String key : data.keySet()) {
            String val = data.get(key).toString();
            redis.set(key, val);
        }
    }

    public static void run() {
        System.out.println("Static Data Loader to Redis....");

        Map<String, JSONObject> stockData = getStockData();
        uploadData(stockData, dbStock);

        Map<String, JSONObject> productData = getProductData();
        uploadData(productData, dbProduct);
    }

    public static void main(String[] args) {
        run();

        redis.select(dbStock);
        Set<String> keys = redis.keys("*");
        System.out.println("user after keys");
        System.out.println(keys);

        redis.select(dbProduct);
        keys = redis.keys("*");
        System.out.println("product after keys");
        System.out.println(keys);

    }


}
