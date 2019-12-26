package com.ifamuzza.ingegneriadelsoftware.model.payment;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ifamuzza.ingegneriadelsoftware.utils.IbanTest;
import com.ifamuzza.ingegneriadelsoftware.utils.JsonUtils;

@Entity
public class BankTransfer extends BaseMethod {

  @Basic(optional = false) private String iban;

  public BankTransfer() {
    super();
  }

  public BankTransfer(JsonNode data) {
    super(data);
    setIBAN(JsonUtils.getString(data, "iban"));
  }

  @Override
  public ObjectNode serialize() {
    ObjectNode node = super.serialize();
    node.put("iban", getIBAN());
    return node;
  }

  @Override
  public List<String> validate() {
    List<String> reasons = super.validate();

    if (iban == null) {
      reasons.add("iban");
    }
    else {
      if (!IbanTest.ibanTest(iban)) {
        reasons.add("iban");
      }
    }

    return reasons;    
  }

  @Override
  public Boolean receive(float total) {
    return false;
  }

  public String getIBAN() { return iban; }
  public void setIBAN(String iban) { this.iban = iban.trim(); }

}
