package com.ifamuzza.ingegneriadelsoftware;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ifamuzza.ingegneriadelsoftware.model.payment.CreditCard;
import com.ifamuzza.ingegneriadelsoftware.model.users.Customer;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class ApiHandlerTests {

	@Autowired
	MockMvc mock;

	@Test
	void customerLoginTests() throws Exception {
		positiveCustomerLoginTest();
		negativeCustomerLoginTest();
	}

	void positiveCustomerLoginTest() throws Exception {

		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		node.put("email", "prova@gmail.com");
		node.put("password", "Sb1noTTo%HaSbinnato");
		String jsonRequest = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(node);
		
		Customer c = new Customer();
		c.setEmail("prova@gmail.com");
		c.setFirstName("Francesco");
		c.setLastName("Torregrossa");

		CreditCard cr = new CreditCard();
		cr.setHolder("Francesco Torregrossa");
		cr.setAddress("Via Villa San Giovanni, 24");
		cr.setCCV("000");
		cr.setNumber("4333535221610574");
		cr.setExpDate("10/23");		
		c.setPaymentMethod(cr);

		String response = this.mock.perform(post("/api/customerLogin")
				.contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
				.andDo(print()).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		JsonNode responseJson = mapper.readTree(response);
		JsonNode expectedJson = c.serialize();
		assertEquals(expectedJson, responseJson);

	}

	void negativeCustomerLoginTest() throws Exception {
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		node.put("email", "prova@gmail.com");
		node.put("password", "abc");
		String json = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(node);

		this.mock.perform(post("/api/customerLogin")
				.contentType(MediaType.APPLICATION_JSON).content(json))
				.andDo(print()).andExpect(status().isBadRequest())
        .andExpect(content().string(""));

	}

}
