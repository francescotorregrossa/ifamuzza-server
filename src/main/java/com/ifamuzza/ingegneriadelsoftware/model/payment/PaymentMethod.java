package com.ifamuzza.ingegneriadelsoftware.model.payment;

public interface PaymentMethod extends Method {

  public Boolean pay(float total);

}
