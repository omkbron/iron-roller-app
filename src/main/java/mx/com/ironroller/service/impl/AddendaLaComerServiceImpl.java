package mx.com.ironroller.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import mx.com.ironroller.model.DatosAddenda;
import mx.com.ironroller.model.amece71.ObjectFactory;
import mx.com.ironroller.model.amece71.RequestForPayment;
import mx.com.ironroller.model.amece71.RequestForPayment.AdditionalInformation;
import mx.com.ironroller.model.amece71.RequestForPayment.BaseAmount;
import mx.com.ironroller.model.amece71.RequestForPayment.Buyer;
import mx.com.ironroller.model.amece71.RequestForPayment.Buyer.ContactInformation;
import mx.com.ironroller.model.amece71.RequestForPayment.Buyer.ContactInformation.PersonOrDepartmentName;
import mx.com.ironroller.model.amece71.RequestForPayment.Currency;
import mx.com.ironroller.model.amece71.RequestForPayment.LineItem;
import mx.com.ironroller.model.amece71.RequestForPayment.LineItem.GrossPrice;
import mx.com.ironroller.model.amece71.RequestForPayment.LineItem.InvoicedQuantity;
import mx.com.ironroller.model.amece71.RequestForPayment.LineItem.NetPrice;
import mx.com.ironroller.model.amece71.RequestForPayment.LineItem.TotalLineAmount;
import mx.com.ironroller.model.amece71.RequestForPayment.LineItem.TradeItemDescriptionInformation;
import mx.com.ironroller.model.amece71.RequestForPayment.LineItem.TradeItemIdentification;
import mx.com.ironroller.model.amece71.RequestForPayment.LineItem.TotalLineAmount.GrossAmount;
import mx.com.ironroller.model.amece71.RequestForPayment.LineItem.TotalLineAmount.NetAmount;
import mx.com.ironroller.model.amece71.RequestForPayment.OrderIdentification;
import mx.com.ironroller.model.amece71.RequestForPayment.OrderIdentification.ReferenceIdentification;
import mx.com.ironroller.model.amece71.RequestForPayment.PayableAmount;
import mx.com.ironroller.model.amece71.RequestForPayment.RequestForPaymentIdentification;
import mx.com.ironroller.model.amece71.RequestForPayment.Seller;
import mx.com.ironroller.model.amece71.RequestForPayment.Seller.AlternatePartyIdentification;
import mx.com.ironroller.model.amece71.RequestForPayment.ShipTo;
import mx.com.ironroller.model.amece71.RequestForPayment.ShipTo.NameAndAddress;
import mx.com.ironroller.model.amece71.RequestForPayment.SpecialInstruction;
import mx.com.ironroller.model.amece71.RequestForPayment.Tax;
import mx.com.ironroller.model.amece71.RequestForPayment.TotalAllowanceCharge;
import mx.com.ironroller.model.amece71.RequestForPayment.TotalAmount;
import mx.com.ironroller.service.AddendaLaComerService;
import mx.gob.sat.cfd._3.Comprobante;
import mx.gob.sat.cfd._3.Comprobante.Conceptos.Concepto;

@Service
public class AddendaLaComerServiceImpl implements AddendaLaComerService {
    private final Logger log = LoggerFactory.getLogger(AddendaLaComerServiceImpl.class);

    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100.00);

    @Override
    public RequestForPayment crear(Comprobante comprobante, DatosAddenda datosAddenda) {
        ObjectFactory of = new ObjectFactory();

        RequestForPayment requestForPayment = of.createRequestForPayment();
        requestForPayment.setDeliveryDate(comprobante.getFecha());
        requestForPayment.setDocumentStatus("ORIGINAL");
        requestForPayment.setType("SimpleInvoiceType");

        RequestForPaymentIdentification requestForPaymentIdentification = of
                .createRequestForPaymentRequestForPaymentIdentification();
        requestForPaymentIdentification.setEntityType("INVOICE");
        requestForPaymentIdentification
                .setUniqueCreatorIdentification(comprobante.getSerie() + "-" + comprobante.getFolio());
        requestForPayment.setRequestForPaymentIdentification(requestForPaymentIdentification);

        SpecialInstruction specialInstructionImporte = of.createRequestForPaymentSpecialInstruction();
        specialInstructionImporte.setCode("ZZZ");
        // TODO Pendiente de implementar
        specialInstructionImporte.getContent().add("IMPORTE EN LETRA");
        requestForPayment.getSpecialInstruction().add(specialInstructionImporte);

        SpecialInstruction specialInstructionFormaPago = of.createRequestForPaymentSpecialInstruction();
        specialInstructionFormaPago.setCode("ZZZ");
        specialInstructionFormaPago.getContent().add(comprobante.getFormaPago());
        requestForPayment.getSpecialInstruction().add(specialInstructionFormaPago);

        OrderIdentification orderIdentification = of.createRequestForPaymentOrderIdentification();
        ReferenceIdentification referenceIdentification = of
                .createRequestForPaymentOrderIdentificationReferenceIdentification();
        referenceIdentification.setType("ON");
        referenceIdentification.setValue("0");
        orderIdentification.getReferenceIdentification().add(referenceIdentification);
        //TODO Revisar el formato de la fecha
        orderIdentification.setReferenceDate(comprobante.getFecha());
        requestForPayment.setOrderIdentification(orderIdentification);

        AdditionalInformation additionalInformation = of.createRequestForPaymentAdditionalInformation();
        mx.com.ironroller.model.amece71.RequestForPayment.AdditionalInformation.ReferenceIdentification refIdAdditionalInfo = of
                .createRequestForPaymentAdditionalInformationReferenceIdentification();
        refIdAdditionalInfo.setType("ATZ");
        refIdAdditionalInfo.setValue(datosAddenda.getNumeroAutorizacion());
        additionalInformation.getReferenceIdentification().add(refIdAdditionalInfo);
        requestForPayment.setAdditionalInformation(additionalInformation);

        Buyer buyer = of.createRequestForPaymentBuyer();
        buyer.setGln("7505000355431");
        ContactInformation contactInformation = of.createRequestForPaymentBuyerContactInformation();
        PersonOrDepartmentName personOrDepartmentName = of
                .createRequestForPaymentBuyerContactInformationPersonOrDepartmentName();
        personOrDepartmentName.setText(datosAddenda.getContactoCompras());
        contactInformation.setPersonOrDepartmentName(personOrDepartmentName);
        buyer.setContactInformation(contactInformation);
        requestForPayment.setBuyer(buyer);

        Seller seller = of.createRequestForPaymentSeller();
        seller.setGln("0000000048537");
        AlternatePartyIdentification alternatePartyIdentification = of
                .createRequestForPaymentSellerAlternatePartyIdentification();
        alternatePartyIdentification.setType("SELLER_ASSIGNED_IDENTIFIER_FOR_A_PARTY");
        alternatePartyIdentification.setValue("48537");
        seller.setAlternatePartyIdentification(alternatePartyIdentification);
        requestForPayment.setSeller(seller);

        ShipTo shipTo = of.createRequestForPaymentShipTo();
        shipTo.setGln("7505000352805");
        NameAndAddress nameAndAddress = of.createRequestForPaymentShipToNameAndAddress();
        JAXBElement<String> name = of.createRequestForPaymentShipToNameAndAddressName("280 - CEDIS Secos");
        nameAndAddress.getNameAndStreetAddressOneAndCity().add(name);
        JAXBElement<String> streetAddressOne = of.createRequestForPaymentShipToNameAndAddressStreetAddressOne(
                "CALZADA VALLEJO NUMERO 980 INDUSTRIAL VALLEJO");
        nameAndAddress.getNameAndStreetAddressOneAndCity().add(streetAddressOne);
        JAXBElement<String> city = of.createRequestForPaymentShipToNameAndAddressCity("CIUDAD DE MEXICO");
        nameAndAddress.getNameAndStreetAddressOneAndCity().add(city);
        JAXBElement<String> postalCode = of.createRequestForPaymentShipToNameAndAddressPostalCode("02300");
        nameAndAddress.getNameAndStreetAddressOneAndCity().add(postalCode);
        shipTo.setNameAndAddress(nameAndAddress);
        requestForPayment.setShipTo(shipTo);

        Currency currency = of.createRequestForPaymentCurrency();
        currency.setCurrencyISOCode(comprobante.getMoneda().value());
        currency.getCurrencyFunction().add("BILLING_CURRENCY");
        if (comprobante.getTipoCambio() != null) {
            currency.setRateOfChange(comprobante.getTipoCambio());
        } else {
            currency.setRateOfChange(BigDecimal.ONE.setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        requestForPayment.getCurrency().add(currency);

        long number = 1;
        for (Concepto concepto : comprobante.getConceptos().getConcepto()) {
            LineItem lineItem = of.createRequestForPaymentLineItem();
            lineItem.setNumber(BigInteger.valueOf(number));
            lineItem.setType("SimpleInvoiceLineItemType");
            
            TradeItemIdentification tradeItemIdentification = of.createRequestForPaymentLineItemTradeItemIdentification();
            //TODO Pendeinte implementar logica para obtener codigo de producto
            tradeItemIdentification.setGtin(concepto.getDescripcion().substring(0, 13));
            lineItem.setTradeItemIdentification(tradeItemIdentification);
            
            TradeItemDescriptionInformation tradeItemDescriptionInformation = of.createRequestForPaymentLineItemTradeItemDescriptionInformation();
            tradeItemDescriptionInformation.setLanguage("ES");
          //TODO Pendeinte implementar logica para obtener descripcion de producto
            tradeItemDescriptionInformation.setLongText(concepto.getDescripcion().substring(13));
            lineItem.setTradeItemDescriptionInformation(tradeItemDescriptionInformation);
            
            InvoicedQuantity invoicedQuantity = of.createRequestForPaymentLineItemInvoicedQuantity();
            //TODO Pendiente implementar logica para obtener clave de unidad equivalente
            invoicedQuantity.setUnitOfMeasure(concepto.getClaveUnidad());
            invoicedQuantity.setValue(concepto.getCantidad());
            lineItem.setInvoicedQuantity(invoicedQuantity);
            
            GrossPrice grossPrice = of.createRequestForPaymentLineItemGrossPrice();
            //TODO Pendiente revisar si es correcto
            grossPrice.setAmount(concepto.getValorUnitario());
            lineItem.setGrossPrice(grossPrice);
            
            NetPrice netPrice = of.createRequestForPaymentLineItemNetPrice();
            //TODO Pendiente revisar si es correcto
            netPrice.setAmount(concepto.getValorUnitario());
            lineItem.setNetPrice(netPrice);
            
            mx.com.ironroller.model.amece71.RequestForPayment.LineItem.AdditionalInformation additionalInformationLineItem = of.createRequestForPaymentLineItemAdditionalInformation();
            mx.com.ironroller.model.amece71.RequestForPayment.LineItem.AdditionalInformation.ReferenceIdentification referenceIdentificationLineItem = of.createRequestForPaymentLineItemAdditionalInformationReferenceIdentification();
            referenceIdentificationLineItem.setType("ON");
            referenceIdentificationLineItem.setValue("0");
            additionalInformationLineItem.setReferenceIdentification(referenceIdentificationLineItem);
            lineItem.setAdditionalInformation(additionalInformationLineItem);
            
            TotalLineAmount totalLineAmount = of.createRequestForPaymentLineItemTotalLineAmount();
            GrossAmount grossAmount = of.createRequestForPaymentLineItemTotalLineAmountGrossAmount();
            //TODO Pendiente revisar si es correcto
            grossAmount.setAmount(concepto.getImporte());
            totalLineAmount.setGrossAmount(grossAmount);
            NetAmount netAmount = of.createRequestForPaymentLineItemTotalLineAmountNetAmount();
            //TODO Pendiente revisar si es correcto
            netAmount.setAmount(concepto.getImporte());
            totalLineAmount.setNetAmount(netAmount);
            lineItem.setTotalLineAmount(totalLineAmount);
            requestForPayment.getLineItem().add(lineItem);
            number++;
        }

        TotalAmount totalAmount = of.createRequestForPaymentTotalAmount();
        totalAmount.setAmount(comprobante.getTotal());
        requestForPayment.setTotalAmount(totalAmount);

        TotalAllowanceCharge totalAllowanceCharge = of.createRequestForPaymentTotalAllowanceCharge();
        totalAllowanceCharge.setAllowanceOrChargeType("ALLOWANCE");
        if (comprobante.getDescuento() != null) {
            totalAllowanceCharge.setAmount(comprobante.getDescuento());
        } else {
            totalAllowanceCharge.setAmount(BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        requestForPayment.getTotalAllowanceCharge().add(totalAllowanceCharge);

        BaseAmount baseAmount = of.createRequestForPaymentBaseAmount();
        baseAmount.setAmount(comprobante.getSubTotal());
        requestForPayment.setBaseAmount(baseAmount);

        Tax tax = of.createRequestForPaymentTax();
        tax.setType("VAT");
        tax.setTaxPercentage(comprobante.getImpuestos().getTraslados().getTraslado().get(0).getTasaOCuota()
                .multiply(HUNDRED).setScale(2, BigDecimal.ROUND_HALF_UP));
        tax.setTaxAmount(comprobante.getImpuestos().getTotalImpuestosTrasladados());
        requestForPayment.getTax().add(tax);
        
        PayableAmount payableAmount = of.createRequestForPaymentPayableAmount();
        payableAmount.setAmount(comprobante.getTotal());
        requestForPayment.setPayableAmount(payableAmount);
        
        return requestForPayment;
    }

}
