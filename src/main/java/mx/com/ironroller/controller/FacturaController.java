package mx.com.ironroller.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FacturaController {
	private final Logger log = LoggerFactory.getLogger(FacturaController.class);
	
	@GetMapping("/facturas")
	public String facturas() {
		log.info("Regresando factura page");
		return "factura/factura";
	}
	
	@PostMapping("/facturas/upload")
	public String procesarFactura(@RequestParam("invoiceFile") MultipartFile invoiceFile, RedirectAttributes redirectAttributes) {
		log.info("procesar xml y agregar addenda");
		return "redirect:/facturas";
	}
}
