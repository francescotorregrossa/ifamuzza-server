package com.ifamuzza.ingegneriadelsoftware.model.payment;

import com.ifamuzza.ingegneriadelsoftware.model.JsonPrivateSerialization;
import com.ifamuzza.ingegneriadelsoftware.model.Validable;

public interface Method extends Validable, JsonPrivateSerialization {

  public Integer getId();
  public void setId(Integer id);

  public String getHolder();
  public void setHolder(String holder);

  public String getAddress();
  public void setAddress(String address);

}
