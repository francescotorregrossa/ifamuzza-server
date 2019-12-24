package com.ifamuzza.ingegneriadelsoftware.model.receipt;

import java.util.regex.Pattern;

import javax.persistence.Basic;
import javax.persistence.Entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ifamuzza.ingegneriadelsoftware.utils.JsonUtils;
import com.ifamuzza.ingegneriadelsoftware.utils.Luhn;

@Entity
public class ReceiptCreditCard extends ReceiptMethod {

  @Basic(optional = false) private String number;

  public ReceiptCreditCard() {
    super();
  }

  public ReceiptCreditCard(JsonNode data) {
    super(data);
    setNumber(JsonUtils.getString(data, "number"));
  }

  @Override
  public ObjectNode serialize() {
    ObjectNode node = super.serialize();
    node.put("number", getNumber());
    return node;
  }

  @Override
  public String validate() {
    String basic = super.validate();
    if (basic != null) {
      return basic;
    }

    if (number == null) {
      return "number";
    }

    String regex = "^(?:(?<visa>4[0-9]{12}(?:[0-9]{3})?)|" +
        "(?<mastercard>5[1-5][0-9]{14})|" +
        "(?<discover>6(?:011|5[0-9]{2})[0-9]{12})|" +
        "(?<amex>3[47][0-9]{13})|" +
        "(?<diners>3(?:0[0-5]|[68][0-9])?[0-9]{11})|" +
        "(?<jcb>(?:2131|1800|35[0-9]{3})[0-9]{11}))$";
 
    Pattern pattern = Pattern.compile(regex);
    if (!pattern.matcher(number).matches()) {
      return "number";
    }

    if (!Luhn.Check(number)) {
      return "number";
    }

    return null;
    
  }

  @Override
  public Boolean pay() {
    return false;
  }

  public String getNumber() { return number; }
  public void setNumber(String number) { this.number = number; }

}
