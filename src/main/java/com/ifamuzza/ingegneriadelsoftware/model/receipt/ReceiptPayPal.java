package com.ifamuzza.ingegneriadelsoftware.model.receipt;

import java.util.List;

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
  public List<String> validate() {
    List<String> reasons = super.validate();

    if (payPalAccessToken == null) {
      reasons.add("payPalAccessToken");
    }

    return reasons;
  }

  @Override
  public Boolean pay() {
    return false;
  }

  public String getPayPalAccessToken() { return payPalAccessToken; }
  public void setPayPalAccessToken(String payPalAccessToken) { this.payPalAccessToken = payPalAccessToken; }

}
