package com.app.restfulws.resource;

/**
 *
 * @author Shekhar.Agrawal
 */
public class MyJaxBeanString {
  private String statusCode;
  private String statusMsg;
  private String result;

  public MyJaxBeanString(){}

  public MyJaxBeanString(String statusCode, String statusMsg, String result){
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
  
  public String getResult() {
    return result;
  }

  public void setModels(String result) {
    this.result = result;
  }
  
}
