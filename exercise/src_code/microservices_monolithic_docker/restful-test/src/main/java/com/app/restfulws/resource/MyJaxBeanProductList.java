package com.app.restfulws.resource;

import com.app.core.Product;
import java.util.List;

public class MyJaxBeanProductList {

    private String statusCode;
    private String statusMsg;
    private List<Product> result;

    public MyJaxBeanProductList(){}

    public MyJaxBeanProductList(String statusCode, String statusMsg, List<Product> result){
        this.statusCode = statusCode;
        this.statusMsg = statusMsg;
        this.result = result;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    public List<Product> getResult() {
        return result;
    }

    public void setModels(List<Product> result) {
        this.result = result;
    }

}
