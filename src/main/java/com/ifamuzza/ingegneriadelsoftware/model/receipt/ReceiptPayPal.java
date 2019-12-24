package com.ifamuzza.ingegneriadelsoftware.model.receipt;

import javax.persistence.Basic;
import javax.persistence.Entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ifamuzza.ingegneriadelsoftware.utils.JsonUtils;

@Entity
public class ReceiptPayPal extends ReceiptMethod {

  @Basic(optional = false) private String payPalAccessToken;

  public ReceiptPayPal() {
    super();
  }

  public ReceiptPayPal(JsonNode data) {
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
  public String validate() {
    String basic = super.validate();
    if (basic != null) {
      return basic;
    }

    if (payPalAccessToken == null) {
      return "payPalAccessToken";
    }

    return null;
  }

  @Override
  public Boolean pay() {
    return false;
  }

  public String getPayPalAccessToken() { return payPalAccessToken; }
  public void setPayPalAccessToken(String payPalAccessToken) { this.payPalAccessToken = payPalAccessToken; }

}
