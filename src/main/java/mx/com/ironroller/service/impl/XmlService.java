package mx.com.ironroller.service.impl;

import java.io.File;
import java.io.FileOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class XmlService {
    private Logger log = LoggerFactory.getLogger(XmlService.class);
    
    public void creaXml(byte[] archivo, String carpeta, String nombre, boolean throwException) {
        log.info("Creando xml... {} en {}", nombre, carpeta);
        try {
            File dir = new File(carpeta);
            if (!dir.exists()) {
                log.info("Carpeta de mes creada {}: {}", carpeta, dir.mkdirs());
                log.info("permiso ejecucion: {}", dir.setExecutable(true, false));
                log.info("permiso lectura: {}", dir.setReadable(true, false));
                log.info("permiso escritura: {}", dir.setWritable(true, false));
            }
            FileOutputStream fos = new FileOutputStream(new File(carpeta, String.format("%s.xml", nombre)));
            fos.write(archivo);
            fos.flush();
            fos.close();
            log.info("Xml creado...");
        } catch (Exception e) {
            if (throwException) {
                throw new RuntimeException("Error en la creación del archivo " + nombre, e);
            } else {
                log.error("Error en la creación del archivo " + nombre);
            }
        }
    }
}
