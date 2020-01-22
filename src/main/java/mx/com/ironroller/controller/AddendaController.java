package mx.com.ironroller.controller;

import java.io.ByteArrayInputStream;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import mx.com.ironroller.model.ComprobanteInvalidoException;
import mx.com.ironroller.model.DatosAddenda;
import mx.com.ironroller.model.IronRollerAppException;
import mx.com.ironroller.model.UploadedFile;
import mx.com.ironroller.model.amece71.RequestForPayment;
import mx.com.ironroller.service.AddendaLaComerService;
import mx.com.ironroller.service.AddendaLaComerXmlService;
import mx.com.ironroller.service.FacturaService;
import mx.com.ironroller.service.NotificacionService;
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
    
    @Autowired
    private NotificacionService notificacionService;

    @GetMapping("/factura/preview")
    public String facturaPreview(@SessionAttribute("uploadedFile") UploadedFile uploadedFile,
            RedirectAttributes redirectAttributes, @ModelAttribute DatosAddenda datosAddenda, Model model) {
        log.info("en preview page");
        if (uploadedFile.getData() == null || uploadedFile.getData().length == 0) {
            redirectAttributes.addFlashAttribute("message", "Debe seleccionar un xml válido.");
            return "redirect:/factura/upload";
        }
        Comprobante comprobante = facturaService.procesarXml(uploadedFile.getData());
        model.addAttribute("comprobante", comprobante);
        return "factura/record-preview";
    }

    @PostMapping("/factura/addenda")
    public String generarAddenda(@SessionAttribute("uploadedFile") UploadedFile uploadedFile,
            @Valid @ModelAttribute DatosAddenda datosAddenda, BindingResult bindingResult,
            RedirectAttributes redirectAttributes, Model model) {
        if (uploadedFile.getData() == null || uploadedFile.getData().length == 0) {
            redirectAttributes.addFlashAttribute("message", "Debe seleccionar un xml válido.");
            return "redirect:/factura/upload";
        }
        if (bindingResult.hasErrors()) {
            log.error("Los datos para la addenda no se encuentran");
            Comprobante comprobante = facturaService.procesarXml(uploadedFile.getData());
            model.addAttribute("comprobante", comprobante);
            return "factura/record-preview";
        }

        Comprobante comprobante = facturaService.procesarXml(uploadedFile.getData());

        RequestForPayment requestForPayment = addendaLaComerService.crear(comprobante, datosAddenda);
        uploadedFile.setRequestForPayment(requestForPayment);
        notificacionService.notificacionAddendaCreada(comprobante);
        return "redirect:/factura/download";
    }

    @GetMapping("/factura/download")
    public String downloadPage() {
        return "factura/download";
    }

    @GetMapping("/factura/process-download")
    public String descargaFactura(@SessionAttribute("uploadedFile") UploadedFile uploadedFile,
            RedirectAttributes redirectAttributes, HttpServletResponse response) {
        if (uploadedFile.getData() == null || uploadedFile.getData().length == 0) {
            redirectAttributes.addFlashAttribute("message", "El recurso ya no está disponible.");
            return "redirect:/factura/upload";
        }
        byte[] cfdiConAddenda = addendaLaComerXmlService.agregaAddenda(uploadedFile.getData(),
                addendaLaComerXmlService.convierteAddendaEnByteArray(uploadedFile.getRequestForPayment()));
        try {
            response.addHeader("Content-Disposition", String.format("attachment; filename=%s",
                    uploadedFile.getOriginalFilename().replace(".xml", "_addenda.xml")));
            response.setContentType("application/xml");
            IOUtils.copy(new ByteArrayInputStream(cfdiConAddenda), response.getOutputStream());
            response.flushBuffer();
            return null;
        } catch (Exception e) {
            log.error("Error al descargar el recurso: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("message", "Ocurrió un error al descargar el recurso.");
            return "redirect:/factura/upload";
        }
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    public String sessionError(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "La sesión ha expirado.");
        return "redirect:/factura/upload";
    }
    
    @ExceptionHandler(IronRollerAppException.class)
    public String descripcionConceptoError(IronRollerAppException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", ex.getMessage());
        return "redirect:/factura/upload";
    }
    
    @ExceptionHandler(ComprobanteInvalidoException.class)
    public String comprobanteInvalido(Exception ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", ex.getMessage());
        return "redirect:/factura/upload";
    }
}
