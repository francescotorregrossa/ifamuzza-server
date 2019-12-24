package com.ifamuzza.ingegneriadelsoftware.utils;

import com.fasterxml.jackson.databind.JsonNode;

public class JsonUtils {

  public static String getString(JsonNode node, String key) {
    JsonNode data = node.get(key);
    if (data != null) {
      return data.asText(null);
    }
    return null;
  }

  public static Integer getInt(JsonNode node, String key) {
    JsonNode data = node.get(key);
    if (data != null) {
      return data.asInt(0);
    }
    return null;
  }
  
}