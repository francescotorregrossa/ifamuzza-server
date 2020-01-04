package com.ifamuzza.ingegneriadelsoftware.model.users;

import static org.junit.Assert.assertArrayEquals;

import com.ifamuzza.ingegneriadelsoftware.model.payment.CreditCard;
import com.ifamuzza.ingegneriadelsoftware.model.payment.PayPal;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CustomerTests {

	@Test
	void validateTests() {

		Customer[] customers = new Customer[4];
		for (int i = 0; i < customers.length; i++) {
			customers[i] = new Customer();
		}

		customers[0].setEmail("luca@ifamuzza.it");
		customers[1].setEmail("michele@unipa.com");
		customers[2].setEmail("davide@it");
		customers[3].setEmail("spiderman@libero.org");

		customers[0].setPassword("abc");
		customers[1].setPassword("Abfrancesco");
		customers[2].setPassword("salvo1salvo2");
		customers[3].setPassword("daVide1234");

		customers[0].setFirstName("Luca");
		customers[1].setFirstName("Davide98");
		customers[2].setFirstName("San Filippo");
		customers[3].setFirstName(null);

		customers[0].setLastName("D'Arrigo");
		customers[1].setLastName("avellone");
		customers[2].setLastName("San Buca");
		customers[3].setLastName(null);

		customers[0].setPhone("+39abcdefg");
		customers[1].setPhone("+39 123456789121290823236761");
		customers[2].setPhone("+40368232429");
		customers[3].setPhone(null);
		
		customers[0].setAddress("Via Piazza, 34");
		customers[1].setAddress("Via Spinuzza");
		customers[2].setAddress("Via Ugo#Patricolo");
		customers[3].setAddress(null);
		
		customers[0].setAllergies("Pomodoro");
		customers[1].setAllergies("Cipolla");
		customers[2].setAllergies("Niente!");
		customers[3].setAllergies(null);

		CreditCard p0 = new CreditCard();
		p0.setHolder("Signor Fulippo");
		p0.setAddress("Via Gian Filippo Ingrassia, 50, Palermo");
		p0.setNumber("4333535221610574");
		p0.setCCV("000");
		p0.setExpDate("10/21");
		customers[0].setPaymentMethod(p0);

		CreditCard p1 = new CreditCard();
		p1.setHolder("D'Arrigo");
		p1.setAddress("Via Gian Filippo Ingrassia, 50, Palermo");
		p1.setNumber("4333535221610573");
		p1.setCCV("abc");
		p1.setExpDate("13/21");
		customers[1].setPaymentMethod(p1);

		PayPal p2 = new PayPal();
		p2.setHolder("D'Arrigo");
		p2.setAddress("Via Gian Filippo Ingrassia, 50, Palermo");
		p2.setPayPalAccessToken("cweuhiwjowfhw2wghjq21a111a");
		customers[2].setPaymentMethod(p2);
		
		customers[3].setPaymentMethod(null);

		assertArrayEquals(new String[] {"password", "phone"}, customers[0].validate().toArray());
		assertArrayEquals(new String[] {"password", "firstName", "phone", "number", "ccv", "expDate"}, customers[1].validate().toArray());
		assertArrayEquals(new String[] {"email", "password", "address", "allergies"}, customers[2].validate().toArray());
		assertArrayEquals(new String[] {}, customers[3].validate().toArray());

	}

}
