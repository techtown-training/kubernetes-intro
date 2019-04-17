package com.app.restfulws.resource;

import com.app.core.CombinedProduct;

/**
 *
 * @author Shekhar.Agrawal
 */
public class MyJaxBeanCombinedProduct {

  private String statusCode;
  private String statusMsg;
  private CombinedProduct result;

  public MyJaxBeanCombinedProduct(){}

  public MyJaxBeanCombinedProduct(String statusCode, String statusMsg, CombinedProduct result){
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
  
  public CombinedProduct getResult() {
    return result;
  }

  public void setModels(CombinedProduct result) {
    this.result = result;
  }
  
}
