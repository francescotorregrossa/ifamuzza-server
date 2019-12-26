package com.ifamuzza.ingegneriadelsoftware.model.payment;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ifamuzza.ingegneriadelsoftware.utils.JsonUtils;

@Entity
public class PayPal extends BaseMethod {

  @Basic(optional = false) private String payPalAccessToken;

  public PayPal() {
    super();
  }

  public PayPal(JsonNode data) {
    super(data);
    setPayPalAccessToken(JsonUtils.getString(data, "payPalAccessToken"));
  }

  @Override
  public ObjectNode serialize() {
    ObjectNode node = super.serialize();
    node.put("payPalAccessToken", getPayPalAccessToken());
    return node;
  }

  @Override
  public List<String> validate() {
    List<String> reasons = super.validate();

    if (payPalAccessToken == null) {
      reasons.add("payPalAccessToken");
    }

    return reasons;
  }

  @Override
  public Boolean pay(float total) {
    return false;
  }

  @Override
  public Boolean receive(float total) {
    return false;
  }

  public String getPayPalAccessToken() { return payPalAccessToken; }
  public void setPayPalAccessToken(String payPalAccessToken) { this.payPalAccessToken = payPalAccessToken; }

}
