package com.ifamuzza.ingegneriadelsoftware.model.payment;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Entity
public class Invalid extends BaseMethod implements PaymentMethod, ReceiptMethod {

  public Invalid() {
    super();
  }

  public Invalid(JsonNode data) {
    super(data);
  }

  @Override
  public ObjectNode serialize() {
    ObjectMapper mapper = new ObjectMapper();
    ObjectNode node = mapper.createObjectNode();
    return node;
  }

  @Override
  public List<String> validate() {
    List<String> reasons = new ArrayList<>();
    reasons.add("type");
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

}
