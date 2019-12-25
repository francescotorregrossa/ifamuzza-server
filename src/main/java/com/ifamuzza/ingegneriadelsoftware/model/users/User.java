package com.ifamuzza.ingegneriadelsoftware.model.users;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ifamuzza.ingegneriadelsoftware.model.JsonPrivateSerialization;
import com.ifamuzza.ingegneriadelsoftware.model.JsonPublicSerialization;
import com.ifamuzza.ingegneriadelsoftware.model.Validable;
import com.ifamuzza.ingegneriadelsoftware.utils.JsonUtils;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class User implements Validable, JsonPrivateSerialization, JsonPublicSerialization {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @Basic(optional = false) private String email;
  @Basic(optional = false) private String hashedPassword;
  @Basic(optional = true) private String accessToken;

  private String password;

  public User() { }
  public User(JsonNode data) {
    setEmail(JsonUtils.getString(data, "email"));
    setPassword(JsonUtils.getString(data, "password"));
  }

  @Override
  public ObjectNode serialize() {
    ObjectMapper mapper = new ObjectMapper();
    ObjectNode node = mapper.createObjectNode();
    node.put("email", getEmail());
    return node;
  }

  @Override
  public ObjectNode publicSerialize() {
    ObjectMapper mapper = new ObjectMapper();
    ObjectNode node = mapper.createObjectNode();
    return node;
  }
  
  
  @Override
  public List<String> validate() {

    List<String> reasons = new ArrayList<>();
    if (email == null) {
      reasons.add("email");
    }
    else {
      email = email.trim();
      // check email correctness
      Pattern emailPattern = Pattern.compile("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
      if (!emailPattern.matcher(email).matches()) {
        reasons.add("email");
      }
    }

    if (password == null) {
      reasons.add("password");
    }
    else {
      password = password.trim();
      // check password strength
      Pattern passwordPattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
      if (!passwordPattern.matcher(password).matches()) {
        reasons.add("password");
      }
    }

    return reasons;

  }

  public Integer getId() { return id; }
  public void setId(Integer id) { this.id = id; }

  public String getAccessToken() { return accessToken; }
  public void setAccessToken(String accessToken) { this.accessToken = accessToken == null ? null : accessToken; }

  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email == null ? null : email.trim(); }

  public void setPassword(String password) {

    if (password == null) {
      return;
    }

    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest(password.trim().getBytes(StandardCharsets.UTF_8));
      this.password = password;
      this.hashedPassword = new String(hash, StandardCharsets.UTF_8);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
  }
  public String getHashedPassword() { return hashedPassword; }

}
