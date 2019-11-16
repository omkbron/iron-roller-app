package mx.com.ironroller.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContactoController {
	private final Logger log = LoggerFactory.getLogger(ContactoController.class);
	
	@GetMapping("/contacto")
	public String contacto() {
		log.info("Regresando contacto page");
		return "contacto/contacto";
	}
}
