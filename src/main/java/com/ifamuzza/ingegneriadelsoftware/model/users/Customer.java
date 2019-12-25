package com.ifamuzza.ingegneriadelsoftware.model.users;

import java.util.List;
import java.util.regex.Pattern;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ifamuzza.ingegneriadelsoftware.model.payment.PaymentCreditCard;
import com.ifamuzza.ingegneriadelsoftware.model.payment.PaymentMethod;
import com.ifamuzza.ingegneriadelsoftware.model.payment.PaymentPayPal;
import com.ifamuzza.ingegneriadelsoftware.utils.JsonUtils;

@Entity
public class Customer extends User {

  @Basic(optional = true) private String firstName;
  @Basic(optional = true) private String lastName;

  @Basic(optional = true) private String phone;
  @Basic(optional = true) private String address;
  @Basic(optional = true) private String allergies;

  @OneToOne
  @Basic(optional = true) private PaymentMethod paymentMethod;

  public Customer() {
    super();
  }

  public Customer(JsonNode data) {
    super(data);
    JsonNode paymentMethod = data.get("paymentMethod");
    setFirstName(JsonUtils.getString(data, "firstName"));
    setLasttName(JsonUtils.getString(data, "lastName"));
    setPhone(JsonUtils.getString(data, "phone"));
    setAddress(JsonUtils.getString(data, "address"));
    setAllergies(JsonUtils.getString(data, "allergies"));
    if (paymentMethod != null) {
      String type = paymentMethod.get("type").asText(null);
      switch (type) {
        case "creditcard":
          setPaymentMethod(new PaymentCreditCard(paymentMethod));
          break;
        case "paypal":
          setPaymentMethod(new PaymentPayPal(paymentMethod));
          break;
        default:
          break;
      }
    }
  }

  @Override
  public ObjectNode serialize() {
    ObjectNode node = super.serialize();
    if (getFirstName() != null) {
      node.put("firstName", getFirstName());
    }
    if (getLasttName() != null) {
      node.put("lastName", getLasttName());
    }
    if (getPhone() != null) {
      node.put("phone", getPhone());
    }
    if (getAddress() != null) {
      node.put("address", getAddress());
    }
    if (getAllergies() != null) {
      node.put("allergies", getAllergies());
    }
    if (getPaymentMethod() != null) {
      node.set("paymentMethod", getPaymentMethod().serialize());
    }
    return node;
  }

  @Override
  public List<String> validate() {
    List<String> reasons = super.validate();
    
    Pattern namePattern = Pattern.compile("^([a-zA-Z ]){2,100}$");
    if (firstName != null && !namePattern.matcher(firstName).matches()) {
      reasons.add("firstName");
    }
    if (lastName != null && !namePattern.matcher(lastName).matches()) {
      reasons.add("lastName");
    }

    Pattern phonePattern = Pattern.compile("^\\+(?:[0-9] ?){6,14}[0-9]$");
    if (phone != null && !phonePattern.matcher(phone).matches()) {
      reasons.add("phone");
    }

    Pattern addressPattern = Pattern.compile("^([a-zA-Z0-9 ,]){2,200}$");
    if (address != null && !addressPattern.matcher(address).matches()) {
      reasons.add("address");
    }

    Pattern allergiesPattern = Pattern.compile("^([a-zA-Z0-9 ,]){2,200}$");
    if (allergies != null && !allergiesPattern.matcher(allergies).matches()) {
      reasons.add("allergies");
    }

    if (paymentMethod != null) {
      List<String> paymentValidation = paymentMethod.validate();
      reasons.addAll(paymentValidation);
    }

    return reasons;
  }

  public String getFirstName() { return firstName; }
  public void setFirstName(String firstName) { this.firstName = firstName == null ? null : firstName.trim(); }

  public String getLasttName() { return lastName; }
  public void setLasttName(String lastName) { this.lastName = lastName == null ? null : lastName.trim(); }

  public String getPhone() { return phone; }
  public void setPhone(String phone) { this.phone = phone == null ? null :  phone.trim(); }

  public String getAddress() { return address; }
  public void setAddress(String address) { this.address = address == null ? null :  address.trim(); }

  public String getAllergies() { return allergies; }
  public void setAllergies(String allergies) { this.allergies = allergies == null ? null :  allergies.trim(); }

  public PaymentMethod getPaymentMethod() { return paymentMethod; }
  public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod == null ? null :  paymentMethod; }

}
