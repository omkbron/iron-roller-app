package mx.com.ironroller.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import mx.com.ironroller.model.UploadedFile;

@Controller
@SessionAttributes(value = { "uploadedFile" })
public class FacturaController {
    private final Logger log = LoggerFactory.getLogger(FacturaController.class);

    @ModelAttribute("uploadedFile")
    public UploadedFile initUploadedFile() {
        return new UploadedFile();
    }

    @GetMapping("/factura/upload")
    public String facturas(Model model) {
        log.info("Regresando factura page");
        model.addAttribute("uploadedFile", new UploadedFile());
        return "factura/upload-form";
    }

    @PostMapping("/factura/process-upload")
    public String procesarFactura(@RequestParam("invoiceFile") MultipartFile invoiceFile,
            @ModelAttribute("uploadedFile") UploadedFile uploadedFile, RedirectAttributes redirectAttributes,
            Model model) {
        log.info("procesar xml y agregar addenda");
        log.info("file->{}", invoiceFile.getOriginalFilename());
        try {
            model.addAttribute("uploadedFile",
                    new UploadedFile(invoiceFile.getOriginalFilename(), invoiceFile.getBytes()));
        } catch (IOException ex) {
            redirectAttributes.addFlashAttribute("message", "Ocurrió un error al leer el xml: " + ex.getMessage());
            return "redirect:/factura/upload";
        }
        return "redirect:/factura/preview";
    }
}
