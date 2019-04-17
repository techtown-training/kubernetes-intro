package com.app.restfulws.resource;

import com.app.core.Stock;

import java.util.List;

/**
 *
 * @author Shekhar.Agrawal
 */
public class MyJaxBeanStockList {

  private String statusCode;
  private String statusMsg;
  private List<Stock> result;

  public MyJaxBeanStockList(){}

  public MyJaxBeanStockList(String statusCode, String statusMsg, List<Stock> result){
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
  
  public List<Stock> getResult() {
    return result;
  }

  public void setModels(List<Stock> result) {
    this.result = result;
  }
  
}
