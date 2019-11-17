package mx.com.ironroller.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import mx.com.ironroller.model.DatosAddenda;
import mx.com.ironroller.model.UploadedFile;
import mx.com.ironroller.model.amece71.RequestForPayment;
import mx.com.ironroller.service.AddendaLaComerService;
import mx.com.ironroller.service.AddendaLaComerXmlService;
import mx.com.ironroller.service.FacturaService;
import mx.gob.sat.cfd._3.Comprobante;

@Controller
public class AddendaController {
    private final Logger log = LoggerFactory.getLogger(AddendaController.class);

    @Autowired
    private FacturaService facturaService;
    
    @Autowired
    private AddendaLaComerService addendaLaComerService;

    @Autowired
    private AddendaLaComerXmlService addendaLaComerXmlService;
    
    @GetMapping("/factura/preview")
    public String facturaPreview(@SessionAttribute("uploadedFile") UploadedFile uploadedFile,
            RedirectAttributes redirectAttributes, Model model) {
        log.info("en preview page");
        if (uploadedFile.getData() == null || uploadedFile.getData().length == 0) {
            redirectAttributes.addFlashAttribute("message", "Debe seleccionar un xml válido.");
            return "redirect:/factura/upload";
        }
        Comprobante comprobante = facturaService.procesarXml(uploadedFile.getData());
        model.addAttribute("comprobante", comprobante);
        DatosAddenda datosAddenda = new DatosAddenda();
        model.addAttribute("datosAddenda", datosAddenda);
        return "factura/record-preview";
    }

    @PostMapping("/factura/addenda")
    public String generarAddenda(@SessionAttribute("uploadedFile") UploadedFile uploadedFile,
            @ModelAttribute DatosAddenda datosAddenda, RedirectAttributes redirectAttributes) {
        log.info("en generar addenda");
        if (uploadedFile.getData() == null || uploadedFile.getData().length == 0) {
            redirectAttributes.addFlashAttribute("message", "Debe seleccionar un xml válido.");
            return "redirect:/factura/upload";
        }
        Comprobante comprobante = facturaService.procesarXml(uploadedFile.getData());

        RequestForPayment requestForPayment = addendaLaComerService.crear(comprobante, datosAddenda);
        addendaLaComerXmlService.imprimeEnConsola(requestForPayment);
        
        return "redirect:/factura/upload";
    }
}
