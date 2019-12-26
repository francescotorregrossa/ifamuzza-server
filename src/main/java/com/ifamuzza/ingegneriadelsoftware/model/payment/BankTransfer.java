package com.ifamuzza.ingegneriadelsoftware.model.payment;

import java.util.List;
import java.util.regex.Pattern;

import javax.persistence.Basic;
import javax.persistence.Entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ifamuzza.ingegneriadelsoftware.utils.JsonUtils;
import com.ifamuzza.ingegneriadelsoftware.utils.Luhn;

@Entity
public class BankTransfer extends BaseMethod implements ReceiptMethod {

  @Basic(optional = false) private String number;
  @Basic(optional = false) private String ccv;
  @Basic(optional = false) private String expDate;

  public BankTransfer() {
    super();
  }

  public BankTransfer(JsonNode data) {
    super(data);
    setNumber(JsonUtils.getString(data, "number"));
    setCCV(JsonUtils.getString(data, "ccv"));
    setExpDate(JsonUtils.getString(data, "expDate"));
  }

  @Override
  public ObjectNode serialize() {
    ObjectNode node = super.serialize();
    node.put("number", getNumber());
    node.put("ccv", getCCV());
    node.put("expDate", getExpDate());
    return node;
  }

  @Override
  public List<String> validate() {
    List<String> reasons = super.validate();

    if (number == null) {
      reasons.add("number");
    }
    else {
      String regex = "^(?:(?<visa>4[0-9]{12}(?:[0-9]{3})?)|" +
      "(?<mastercard>5[1-5][0-9]{14})|" +
      "(?<discover>6(?:011|5[0-9]{2})[0-9]{12})|" +
      "(?<amex>3[47][0-9]{13})|" +
      "(?<diners>3(?:0[0-5]|[68][0-9])?[0-9]{11})|" +
      "(?<jcb>(?:2131|1800|35[0-9]{3})[0-9]{11}))$";

      Pattern pattern = Pattern.compile(regex);
      if (!pattern.matcher(number).matches()) {
        reasons.add("number");
      }
      else if (!Luhn.Check(number)) {
        reasons.add("number");
      }
    }

    if (ccv == null) {
      reasons.add("ccv");
    }
    else {
      Pattern ccvPattern = Pattern.compile("^[0-9]{3,4}$");
      if (!ccvPattern.matcher(ccv).matches()) {
        reasons.add("ccv");
      }
    }

    if (expDate == null) {
      reasons.add("expDate");
    }
    else {
      Pattern expDatePattern = Pattern.compile("^(0[1-9]|1[0-2])\\/([0-9]{4}|[0-9]{2})$");
      if (!expDatePattern.matcher(expDate).matches()) {
        reasons.add("expDate");
      }
    }

    return reasons;    
  }

  @Override
  public Boolean receive(float total) {
    return false;
  }

  public String getNumber() { return number; }
  public void setNumber(String number) { this.number = number.trim().replaceAll("-", ""); }

  public String getCCV() { return ccv; }
  public void setCCV(String ccv) { this.ccv = ccv.trim(); }

  public String getExpDate() { return expDate; }
  public void setExpDate(String expDate) { this.expDate = expDate.trim(); }

}
