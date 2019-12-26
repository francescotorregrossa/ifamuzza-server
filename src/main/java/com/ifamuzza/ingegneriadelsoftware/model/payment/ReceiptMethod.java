package com.ifamuzza.ingegneriadelsoftware.model.payment;

public interface ReceiptMethod extends Method {

  public Boolean receive(float total);
  
}
