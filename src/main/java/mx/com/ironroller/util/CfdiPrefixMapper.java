package mx.com.ironroller.util;

import java.util.HashMap;
import java.util.Map;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

public class CfdiPrefixMapper extends NamespacePrefixMapper {

	private Map<String, String> namespaceMap = new HashMap<String, String>();
	
	public CfdiPrefixMapper() {
		namespaceMap.put("http://www.sat.gob.mx/cfd/3", "cfdi");
		namespaceMap.put("http://www.sat.gob.mx/TimbreFiscalDigital", "tfd");
		namespaceMap.put("http://www.w3.org/2001/XMLSchema-instance", "xsi");
	}
	
	@Override
	public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
		return namespaceMap.get(namespaceUri);
	}
	
}
