package com.app.restfulws.resource;

import com.app.core.Product;
import com.app.core.RetailController;
import com.app.core.Stock;
import com.app.core.CombinedProduct;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Shekhar.Agrawal
 */

@Path("retailDesign")
public class RetailAction {
    
    public RetailAction() {}

    // http://localhost:8080/sample-monolithic-1.0/rest/retailDesign/getallstocks
    @GET
    @Path("/getallstocks")
    @Produces("application/json")
    public MyJaxBeanStockList getAllUsers(@Context UriInfo ui) {
        MyJaxBeanStockList mb = null;
        List<Stock> stocks = RetailController.getAllStocks();
        mb = new MyJaxBeanStockList("200", "OK", stocks);
        return mb;
    }

    // http://localhost:8080/sample-monolithic-1.0/rest/retailDesign/getallproducts
    @GET
    @Path("/getallproducts")
    @Produces("application/json")
    public MyJaxBeanProductList getAllProducts(@Context UriInfo ui) {
        MyJaxBeanProductList mb = null;
        List<Product> products = RetailController.getAllProducts();
        mb = new MyJaxBeanProductList("200", "OK", products);
        return mb;
    }

    // http://localhost:8080/sample-monolithic-1.0/rest/retailDesign/getstock?productId=1
    @GET
    @Path("/getstock")
    @Produces("application/json")
    public MyJaxBeanStockList getUser(@Context UriInfo ui) {
        MyJaxBeanStockList mb = null;
        List<Stock> stocks = new ArrayList<>();

        MultivaluedMap<String,String> queryParams = ui.getQueryParameters();

        //Check for the Mandatory Parameters else return HTTP 412
        if( queryParams.containsKey("productId") ) {
            String productId = queryParams.getFirst("productId");
            Stock stock = RetailController.getStock(productId);
            stocks.add(stock);
            mb = new MyJaxBeanStockList("200", "OK", stocks);
        } else {
            // pre condition failed
            mb = new MyJaxBeanStockList("412","Mandatory Parameter 'userid' Missing", stocks);
        }
        return mb;
    }

    // http://localhost:8080/sample-monolithic-1.0/rest/retailDesign/getproduct?id=2
    @GET
    @Path("/getproduct")
    @Produces("application/json")
    public MyJaxBeanProductList getProduct(@Context UriInfo ui) {
        MyJaxBeanProductList mb = null;
        List<Product> products = new ArrayList<>();

        MultivaluedMap<String,String> queryParams = ui.getQueryParameters();

        //Check for the Mandatory Parameters else return HTTP 412
        if( queryParams.containsKey("id") ) {
            String productid = queryParams.getFirst("id");
            Product product = RetailController.getProduct(productid);
            products.add(product);
            mb = new MyJaxBeanProductList("200", "OK", products);
        } else {
            // pre condition failed
            mb = new MyJaxBeanProductList("412","Mandatory Parameter 'productid' Missing", products);
        }
        return mb;
    }

    // http://localhost:8080/sample-monolithic-1.0/rest/retailDesign/combinedproduct?productId=1&id=1
    @GET
    @Path("/combinedproduct")
    @Produces("application/json")
    public MyJaxBeanCombinedProduct userBuy(@Context UriInfo ui) {

        MyJaxBeanCombinedProduct mb = null;
        CombinedProduct result = new CombinedProduct();

        MultivaluedMap<String,String> queryParams = ui.getQueryParameters();

        //Check for the Mandatory Parameters else return HTTP 412
        if( queryParams.containsKey("productId") && queryParams.containsKey("id") ) {
            String productId = queryParams.getFirst("productId");
            String id = queryParams.getFirst("id");

            result = RetailController.combinedProduct(productId, id);
            mb = new MyJaxBeanCombinedProduct("200" , "OK" , result);
        } else {
            // pre condition failed
            mb = new MyJaxBeanCombinedProduct("412","Mandatory Parameters Missing", result);
        }
        return mb;
    }

}
