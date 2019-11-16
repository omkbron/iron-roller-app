package mx.com.ironroller.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	private final Logger log = LoggerFactory.getLogger(HomeController.class);

	@GetMapping(value = { "/", "/home" })
	public String home() {
		log.info("Regresando home page");
		return "home/home";
	}
}
