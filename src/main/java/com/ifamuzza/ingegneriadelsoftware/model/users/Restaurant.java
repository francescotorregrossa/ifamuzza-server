package com.ifamuzza.ingegneriadelsoftware.model.users;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import com.byteowls.jopencage.model.JOpenCageResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ifamuzza.ingegneriadelsoftware.model.receipt.ReceiptCreditCard;
import com.ifamuzza.ingegneriadelsoftware.model.receipt.ReceiptMethod;
import com.ifamuzza.ingegneriadelsoftware.model.receipt.ReceiptPayPal;
import com.ifamuzza.ingegneriadelsoftware.utils.Geocoder;
import com.ifamuzza.ingegneriadelsoftware.utils.JsonUtils;


@Entity
public class Restaurant extends User {

  @Basic(optional = false) private String name;
  @Basic(optional = false) private String address;
  @Basic(optional = false) private String[] openingTime;
  @Basic(optional = false) private String phone;
  @Basic(optional = false) private Integer downPayment;

  @OneToOne
  @Basic(optional = false) private ReceiptMethod receiptMethod;

  @Basic(optional = false) private Double latitude;
  @Basic(optional = false) private Double longitude;

  public Restaurant() {
    super();
  }

  public Restaurant(JsonNode data) {
    super(data);
    JsonNode receiptMethod = data.get("receiptMethod");
    setName(JsonUtils.getString(data, "name"));
    setAddress(JsonUtils.getString(data, "address"));
    
    JsonNode openingTime = data.get("openingTime");
    if (openingTime != null && openingTime.isArray()) {
      ArrayList<String> times = new ArrayList<>();
      for (final JsonNode time: openingTime) {
        String t = time.asText(null);
        if (t != null) {
          times.add(t);
        }
      }
      setOpeningTime(times.toArray(new String[times.size()]));
    }

    setPhone(JsonUtils.getString(data, "phone"));
    setDownPayment(JsonUtils.getInt(data, "downPayment"));
    
    if (receiptMethod != null) {
      String type = JsonUtils.getString(receiptMethod, "type");
      switch (type) {
        case "creditcard":
          setReceiptMethod(new ReceiptCreditCard(receiptMethod));
          break;
        case "paypal":
          setReceiptMethod(new ReceiptPayPal(receiptMethod));
          break;
        default:
          break;
      }
    }
  }

  @Override
  public ObjectNode serialize() {
    ObjectMapper mapper = new ObjectMapper();
    ArrayNode openingTime = mapper.createArrayNode();
    for (String time: getOpeningTime()) {
      openingTime.add(time);
    }
    ObjectNode node = super.serialize();
    node.put("name", getName());
    node.put("address", getAddress());
    node.set("openingTime", openingTime);
    node.put("phone", getPhone());
    node.put("downPayment", getDownPayment());
    node.set("receiptMethod", getReceiptMethod().serialize());
    return node;
  }

  public ObjectNode publicSerialize() {
    ObjectMapper mapper = new ObjectMapper();
    ArrayNode openingTime = mapper.createArrayNode();
    for (String time: getOpeningTime()) {
      openingTime.add(time);
    }
    ObjectNode node = mapper.createObjectNode();
    node.put("id", getId());
    node.put("name", getName());
    node.put("address", getAddress());
    node.put("latitude", getLatitude());
    node.put("longitude", getLongitude());
    node.set("openingTime", openingTime);
    node.put("phone", getPhone());
    node.put("downPayment", getDownPayment());
    return node;
  }

  @Override
  public List<String> validate() {
    List<String> reasons = super.validate();

    if (name == null) {
      reasons.add("name");
    }
    else {
      Pattern namePattern = Pattern.compile("^([a-zA-Z ]){2,200}$");
      if (!namePattern.matcher(name).matches()) {
        reasons.add("name");
      }
    }

    if (address == null || latitude == null || longitude == null) {
      reasons.add("address");
    }
    else {
      Pattern addressPattern = Pattern.compile("^([a-zA-Z0-9 ,]){2,200}$");
      if (!addressPattern.matcher(address).matches()) {
        reasons.add("address");
      }
    }

    if (openingTime == null || (openingTime.length == 0 || openingTime.length > 7)) {
      reasons.add("openingTime");
    }
    else {  
      Pattern openingTimePattern = Pattern.compile("^((mon)|(tue)|(wed)|(thu)|(fri)|(sat)|(sun)) (([01]?[0-9]|2[0-3]):[0-5][0-9]-([01]?[0-9]|2[0-3]):[0-5][0-9])$");
      for (String ot: openingTime) {
        if (!openingTimePattern.matcher(ot).matches()) {
          reasons.add("openingTime");
        }
      }
    }

    if (phone == null) {
      reasons.add("phone");
    }
    else {
      Pattern phonePattern = Pattern.compile("^\\+(?:[0-9] ?){6,14}[0-9]$");
      if (!phonePattern.matcher(phone).matches()) {
        reasons.add("phone");
      }
    }


    if (downPayment == null) {
      reasons.add("downPayment");
    }
    else {
      if (downPayment < 0 || downPayment > 100) {
        reasons.add("downPayment");
      }
    }

    if (receiptMethod == null) {
      reasons.add("receiptMethod");
    }
    else {
      List<String> receiptValidation = receiptMethod.validate();
      reasons.addAll(receiptValidation);
    }

    return reasons;
  }

  public String getName() { return name; }
  public void setName(String name) { this.name = name == null ? null : name.trim(); }

  public String getAddress() { return address; }

  public void setAddress(String address) { 
    if (address != null) {

      this.address = address.trim();
      JOpenCageResult result = Geocoder.forward(this.address);
      if (result != null) {
        this.latitude = result.getGeometry().getLat();
        this.longitude = result.getGeometry().getLng();
      }
    }
    else {
      this.latitude = null;
      this.longitude = null;
      this.address = null;
    }
  }

  public String[] getOpeningTime() { return openingTime; }
  public void setOpeningTime(String[] openingTime) { this.openingTime = openingTime == null ? null : openingTime; }

  public String getPhone() { return phone; }
  public void setPhone(String phone) { this.phone = phone == null ? null : phone.trim(); }

  public Integer getDownPayment() { return downPayment; }
  public void setDownPayment(Integer downPayment) { this.downPayment = downPayment == null ? null : downPayment; }

  public ReceiptMethod getReceiptMethod() { return receiptMethod; }
  public void setReceiptMethod(ReceiptMethod receiptMethod) { this.receiptMethod = receiptMethod == null ? null : receiptMethod; }

  public Double getLatitude() { return latitude; }
  public Double getLongitude() { return longitude; }

}
