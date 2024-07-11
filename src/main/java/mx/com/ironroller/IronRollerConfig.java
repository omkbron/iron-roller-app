package mx.com.ironroller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import mx.com.ironroller.model.amece71.RequestForPayment;
import mx.com.ironroller.util.CfdiPrefixMapper;
import mx.gob.sat.cfd._4.Comprobante;
import mx.gob.sat.timbrefiscaldigital.TimbreFiscalDigital;

@Configuration
public class IronRollerConfig {
	private final Logger log = LoggerFactory.getLogger(IronRollerConfig.class);
	
	@Bean(name = "cfdiMarshaller")
	public Jaxb2Marshaller jaxb2CfdiMarshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setClassesToBeBound(Comprobante.class, TimbreFiscalDigital.class);
		Map<String, Object> marshallerProperties = new HashMap<String, Object>();
		marshallerProperties.put(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshallerProperties.put(javax.xml.bind.Marshaller.JAXB_SCHEMA_LOCATION, "http://www.sat.gob.mx/cfd/4 http://www.sat.gob.mx/sitio_internet/cfd/4/cfdv40.xsd");
		marshallerProperties.put("com.sun.xml.bind.namespacePrefixMapper", new CfdiPrefixMapper());
		marshallerProperties.put("jaxb.encoding", "UTF-8");
		marshaller.setMarshallerProperties(marshallerProperties);
		return marshaller;
	}
	
	@Bean(name = "addendaLaComerMarshaller")
	public Jaxb2Marshaller jaxb2AddendaLaComerMarshaller() {
	    Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(RequestForPayment.class);
        Map<String, Object> marshallerProperties = new HashMap<String, Object>();
        marshallerProperties.put(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshallerProperties.put("jaxb.encoding", "UTF-8");
        marshaller.setMarshallerProperties(marshallerProperties);
        return marshaller;
	}
}
