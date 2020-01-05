package com.ifamuzza.ingegneriadelsoftware.model.users;

import com.ifamuzza.ingegneriadelsoftware.model.payment.BankTransfer;
import com.ifamuzza.ingegneriadelsoftware.model.payment.PayPal;

import static org.junit.Assert.assertArrayEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RestaurantTests {


	@Test
	void validateTests() {
		Restaurant[] restaurants = new Restaurant[4];
		for (int i = 0; i < restaurants.length; i++) {
			restaurants[i] = new Restaurant();
		}

		restaurants[0].setEmail("cortedeimangioni@gmail.com");
		restaurants[1].setEmail("savoca@libero.com");
		restaurants[2].setEmail("newparadise@it"); // error
		restaurants[3].setEmail("mounir@libero.org");

		restaurants[0].setPassword("timangiotimangio"); //error
		restaurants[1].setPassword("savocapolleriA"); //error
		restaurants[2].setPassword("Salvo1Salvo2");
		restaurants[3].setPassword("daVide1234");

		restaurants[0].setName("1Corte dei mangioni");//error
		restaurants[1].setName("Savoca");
		restaurants[2].setName("New Paradise");
		restaurants[3].setName("Mounir");

		restaurants[0].setAddress("Via Guglielmo Marconi");
		restaurants[1].setAddress("#Vi4 Giovanni Sgambati");//error
		restaurants[2].setAddress("Via Chie$a Nuov@");//error
		restaurants[3].setAddress("Via Giuseppe Patricolo, 104");

		restaurants[0].setOpeningTime(new String[]{"mon 11:30-18:30", "fri 10:30-19:30"});
		restaurants[1].setOpeningTime(new String[]{"sat11:30-18:30", "sun 10:30-19:30"});//error
		restaurants[2].setOpeningTime(new String[]{"tue 11.30-18.30", "wed 14.30-19.30"});//error
		restaurants[3].setOpeningTime(new String[]{"mon 11:30-18:30", "fri 10:30-19:30"});

		restaurants[0].setPhone("+39abcdefg");//error
		restaurants[1].setPhone("+39 1234335690121290823236761");//error
		restaurants[2].setPhone(null); // error
		restaurants[3].setPhone("+40335632429");

		restaurants[0].setDownPayment(120); // error
		restaurants[1].setDownPayment(100);
		restaurants[2].setDownPayment(null); // error
		restaurants[3].setDownPayment(40);

		BankTransfer p0 = new BankTransfer();
		p0.setHolder("SignoMMario");
		p0.setAddress("Via San Filippo Michele, 50, Palermo");
		p0.setIBAN("IT92B0300203280436737196943");
		restaurants[0].setReceiptMethod(p0);

		BankTransfer p1 = new BankTransfer();
		p1.setHolder("SignoFFranco");
		p1.setAddress("Via della Liberta', 100, Palermo");
		p1.setIBAN("IT92C03002032804363196959");//error
		restaurants[1].setReceiptMethod(p1);

		restaurants[2].setReceiptMethod(null);

		PayPal p3 = new PayPal();
		p3.setHolder("Nicola San");
		p3.setAddress("Via Pappardella, 21, Palermo");
		p3.setPayPalAccessToken("cweuhiwjowfhw2wghjq21a111a");
		restaurants[3].setReceiptMethod(p3);

		assertArrayEquals(new String[] {"password", "name", "phone" , "downPayment"}, restaurants[0].validate().toArray());
		assertArrayEquals(new String[] {"password", "address", "openingTime", "phone", "iban"}, restaurants[1].validate().toArray());
		assertArrayEquals(new String[] {"email", "address", "openingTime", "phone", "downPayment", "receiptMethod"}, restaurants[2].validate().toArray());
		assertArrayEquals(new String[] {}, restaurants[3].validate().toArray());

	}
	

}
