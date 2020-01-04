package com.ifamuzza.ingegneriadelsoftware;

import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.byteowls.jopencage.model.JOpenCageResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.ifamuzza.ingegneriadelsoftware.model.users.Customer;
import com.ifamuzza.ingegneriadelsoftware.model.users.Restaurant;
import com.ifamuzza.ingegneriadelsoftware.model.users.User;
import com.ifamuzza.ingegneriadelsoftware.model.payment.BaseMethod;
import com.ifamuzza.ingegneriadelsoftware.repository.UserRepository;
import com.ifamuzza.ingegneriadelsoftware.utils.Geocoder;
import com.ifamuzza.ingegneriadelsoftware.repository.BaseMethodRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RestController
@RequestMapping(value = "/api")
public class ApiHandler {
	
	@Autowired private UserRepository userRepository;
	@Autowired private BaseMethodRepository methodRepository;


	@GetMapping(value = "/search", produces = "application/json")
	public String search(Model model, HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "address", required = true) String address) {
		
		// String accessToken = request.getHeader("accessToken");

		JOpenCageResult result = Geocoder.forward(address);
		if (result == null) {
			endClientSession(response, "address");
			return null;
		}

		Double searchLat = result.getGeometry().getLat();
		Double searchLng = result.getGeometry().getLng();

		List<Restaurant> restaurants = getRestaurants(searchLat, searchLng, 10_000d);
		Collections.sort(restaurants, new Comparator<Restaurant>() {
			@Override public int compare(Restaurant r1, Restaurant r2) {
				double d1 = Geocoder.distance(searchLat, r1.getLatitude(), searchLng, r2.getLongitude());
				double d2 = Geocoder.distance(searchLat, r2.getLatitude(), searchLng, r2.getLongitude());
				return d1 < d2 ? -1 : 1;
			}
		});

		if (restaurants.size() == 0) {
			endClientSession(response, "address");
			return null;
		}

		try {
			
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode results = mapper.createArrayNode();
			for (Restaurant r: restaurants) {
				results.add(r.publicSerialize());
			}
			return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(results);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}


	@GetMapping(value = "/userForAccessToken", produces = "application/json")
	public String userForAccessToken(Model model, HttpServletRequest request, HttpServletResponse response) {
		
		String accessToken = request.getHeader("accessToken");
		if (accessToken == null) {
			endClientSession(response, "accessToken");
			return null;
		}

		// check if the user exists
		User u = getUserForAccessToken(accessToken);
		if (u == null) {
			endClientSession(response, null);
			return null;
		}

		try {
			return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(u.serialize());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
		
	}



	@PostMapping(value = "/customerLogin", consumes = "application/json", produces = "application/json")
	public String customerLogin(Model model, HttpServletResponse response, @RequestBody String postPayload) {
		
		Customer c = null;
		try {
			c = new Customer(new ObjectMapper().readTree(postPayload));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		if (c == null) {
			endClientSession(response, null);
			return null;
		}

		List<String> error = c.validate();
		if (!error.contains("email") && !error.contains("password")) {

			// check if the user exists
			User u = getUserForEmail(c.getEmail());
			if (u == null) {
				endClientSession(response, "credentials");
				return null;
			}

			// check if the password matches
			if (!u.getHashedPassword().equals(c.getHashedPassword())) {
				endClientSession(response, "credentials");
				return null;
			}

			// create session for the user
			String accessToken = getNextAccessToken();
			Cookie sessionCookie = new Cookie("accessToken", accessToken);
			sessionCookie.setSecure(true);
			response.addCookie(sessionCookie);
			u.setAccessToken(accessToken);
			userRepository.save(u);
			try {
				return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(u.serialize());
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				endClientSession(response, "credentials");
				return null;
			}
		}

		endClientSession(response, "credentials");
		return null;
	}

	@PostMapping(value = "/restaurantLogin", consumes = "application/json", produces = "application/json")
	public String restaurantLogin(Model model, HttpServletResponse response, @RequestBody String postPayload) {

		Restaurant r = null;
		try {
			r = new Restaurant(new ObjectMapper().readTree(postPayload));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		if (r == null) {
			endClientSession(response, null);
			return null;
		}

		List<String> error = r.validate();
		if (!error.contains("email") && !error.contains("password")) {

			// check if the user exists
			User u = getUserForEmail(r.getEmail());
			if (u == null) {
				endClientSession(response, "credentials");
				return null;
			}

			// check if the password matches
			if (!u.getHashedPassword().equals(r.getHashedPassword())) {
				endClientSession(response, "credentials");
				return null;
			}

			// create session for the user
			String accessToken = getNextAccessToken();
			Cookie sessionCookie = new Cookie("accessToken", accessToken);
			sessionCookie.setSecure(true);
			response.addCookie(sessionCookie);
			u.setAccessToken(accessToken);
			userRepository.save(u);
			try {
				return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(u.serialize());
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				endClientSession(response, "credentials");
				return null;
			}

		}

		endClientSession(response, "credentials");
		return null;
	}


	@PostMapping(value = "/customerSignup", consumes = "application/json")
	public String customerSignup(Model model, HttpServletResponse response, @RequestBody String postPayload) {
		
		Customer c = null;
		try {
			c = new Customer(new ObjectMapper().readTree(postPayload));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		if (c == null) {
			endClientSession(response, null);
			return null;
		}

		List<String> error = c.validate();
		if (error.isEmpty()) {
			
			// check for conflicts with existing users
			if (getUserForEmail(c.getEmail()) != null) {
				endClientSession(response, "email");
				return null;
			}

			// create session for the user
			String accessToken = getNextAccessToken();
			Cookie sessionCookie = new Cookie("accessToken", accessToken);
			sessionCookie.setSecure(true);
			response.addCookie(sessionCookie);
			c.setAccessToken(accessToken);

			// save the payment method (if given) and the new user
			BaseMethod p = c.getPaymentMethod();
			if (p != null) {
				methodRepository.save(p);
			}
			userRepository.save(c);

			return null;

		}
		else {
			endClientSession(response, String.join(", ", error));
			return null;
		}
		
	}

	@PostMapping(value = "/restaurantSignup", consumes = "application/json")
	public String restaurantSignup(Model model, HttpServletResponse response, @RequestBody String postPayload) throws InterruptedException {
		
		Restaurant r = null;
		try {
			r = new Restaurant(new ObjectMapper().readTree(postPayload));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		if (r == null) {
			endClientSession(response, null);
			return null;
		}
		
		List<String> error = r.validate();
		if (error.isEmpty()) {
			
			// check for conflicts with existing users
			if (getUserForEmail(r.getEmail()) != null) {
				endClientSession(response, "email");
				return null;
			}

			// create session for the user
			String accessToken = getNextAccessToken();
			Cookie sessionCookie = new Cookie("accessToken", accessToken);
			sessionCookie.setSecure(true);
			response.addCookie(sessionCookie);
			r.setAccessToken(accessToken);

			// save the receipt method and the new user
			methodRepository.save(r.getReceiptMethod());
			userRepository.save(r);

			return null;
			
		}
		else {
			endClientSession(response, String.join(", ", error));
			return null;
		}
	}

	
	@PostMapping(value = "/logout")
	public String logout(Model model, HttpServletResponse response, HttpServletRequest request) {
		
		String accessToken = request.getHeader("accessToken");
		if (accessToken == null) {
			endClientSession(response, "accessToken");
			return null;
		}

		User u = getUserForAccessToken(accessToken);
		if (u != null) {
			u.setAccessToken(null);
			userRepository.save(u);
		}

		Cookie sessionCookie = new Cookie("accessToken", null);
		sessionCookie.setMaxAge(0);
		sessionCookie.setSecure(true);
		response.addCookie(sessionCookie);
		
		return null;
	}

	private void endClientSession(HttpServletResponse response, String reason) {
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		Cookie sessionCookie = new Cookie("accessToken", null);
		sessionCookie.setMaxAge(0);
		sessionCookie.setSecure(true);
		response.addCookie(sessionCookie);
		response.setHeader("reason", reason);
	}


	private String getNextAccessToken() {
		final String ALPHA_NUMERIC_STRING = "abcdefghijklmnopqrstuvwxyz" + 
																				"ABCDEFGHIJKLMNOPQRSTUVWXYZ" + 
																				"0123456789";

		String candidate = null;
		for (int i = 0; i < 100; i++) {
			
			StringBuilder builder = new StringBuilder();
			int count = 64;
			while (count-- != 0) {
				int character = (int)(Math.random() * ALPHA_NUMERIC_STRING.length());
				builder.append(ALPHA_NUMERIC_STRING.charAt(character));
			}

			candidate = builder.toString();
			if (getUserForAccessToken(candidate) == null) {
				break;
			}
		}

		return candidate;
	}

	private User getUserForEmail(String email) {
		for (User u: userRepository.findAll()) {
			if (u.getEmail().equals(email)) {
				return u;
			}
		}
		return null;
	}

	private User getUserForAccessToken(String accessToken) {
		for (User u: userRepository.findAll()) {
			if (u.getAccessToken() != null && u.getAccessToken().equals(accessToken)) {
				return u;
			}
		}
		return null;
	}


	/*
	private List<Customer> getCustomers() {
		List<Customer> results = new ArrayList<Customer>();
		for (User u: userRepository.findAll()) {
			if (u instanceof Customer) {
				results.add((Customer) u);
			}
		}
		return results;
	}
	*/

	// maxDistance in meters
	private List<Restaurant> getRestaurants(Double lat, Double lng, Double maxDistance) {
		List<Restaurant> results = new ArrayList<Restaurant>();
		for (User u: userRepository.findAll()) {
			if (u instanceof Restaurant) {
				Restaurant r = (Restaurant) u;
				double d = Geocoder.distance(lat, r.getLatitude(), lng, r.getLongitude());
				if (d < maxDistance) {
					results.add(r);
				}
			}
		}
		return results;
	}
	
	
	// development only

	@GetMapping(path="/testall")
  public @ResponseBody Iterable<User> getAllUsers() {
    return userRepository.findAll();
	}

}
