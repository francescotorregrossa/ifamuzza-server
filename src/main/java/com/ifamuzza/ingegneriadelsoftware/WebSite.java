package com.ifamuzza.ingegneriadelsoftware;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class WebSite {
	
	@RequestMapping("/")
	public String index(Model model) {
		return "iFamuzza.it";
  }

}
