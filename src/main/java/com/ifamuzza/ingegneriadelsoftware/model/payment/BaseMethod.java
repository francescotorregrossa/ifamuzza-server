package com.ifamuzza.ingegneriadelsoftware.model.payment;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ifamuzza.ingegneriadelsoftware.model.JsonPrivateSerialization;
import com.ifamuzza.ingegneriadelsoftware.model.Validable;
import com.ifamuzza.ingegneriadelsoftware.utils.JsonUtils;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class BaseMethod implements Validable, JsonPrivateSerialization {

  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  public Integer id;

  @Basic(optional = false) public String holder;
  @Basic(optional = false) public String address;

  public BaseMethod() { }
  
  public BaseMethod(JsonNode data) {
    setHolder(JsonUtils.getString(data, "holder"));
    setAddress(JsonUtils.getString(data, "address"));
  }

  @Override
  public ObjectNode serialize() {
    ObjectMapper mapper = new ObjectMapper();
    ObjectNode node = mapper.createObjectNode();
    node.put("holder", getHolder());
    node.put("address", getAddress());
    return node;
  }
  
  @Override
  public List<String> validate() {
    List<String> reasons = new ArrayList<>();

    if (holder == null) {
      reasons.add("holder");
    }
    else {
      Pattern holderPattern = Pattern.compile("^([a-zA-Z ']){2,200}$");
      if (!holderPattern.matcher(holder).matches()) {
        reasons.add("holder");
      }
    }

    if (address == null) {
      reasons.add("address");
    }
    else {
      Pattern addressPattern = Pattern.compile("^([a-zA-Z0-9 ,']){2,200}$");
      if (!addressPattern.matcher(address).matches()) {
        reasons.add("address");
      }
    }

    return reasons;
  }


  public Boolean pay(float total) {
    throw new UnsupportedOperationException("This method does not support payment.");
  }

  public Boolean receive(float total) {
    throw new UnsupportedOperationException("This method does not support receipt.");
  }

  public Integer getId() { return id; }
  public void setId(Integer id) { this.id = id; }

  public String getHolder() { return holder; }
  public void setHolder(String holder) { this.holder = holder.trim(); }

  public String getAddress() { return address; }
  public void setAddress(String address) { this.address = address.trim(); }

}
