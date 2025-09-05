/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.util;

import com.origami.sgr.entities.RenCajero;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

/**
 *
 * @author Anyelo
 */
public class LeerFacturaXml {
    
    public static Map<String, Object> leerFacturaXmlAutorizada(RenCajero caja, String claveAcceso) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Map<String, Object> map = new HashMap();
        try {
            File archivo = new File(caja.getRutaComprobantesAutorizados() + claveAcceso + ".xml");
            if (archivo.exists()) {
                SAXBuilder builder = new SAXBuilder();
                Document document = (Document) builder.build(archivo);
                Element rootNode = document.getRootElement();
                String estado = rootNode.getChildTextTrim("estado");
                String numeroAutorizacion = rootNode.getChildTextTrim("numeroAutorizacion");
                String fechaAutorizacion = rootNode.getChildTextTrim("fechaAutorizacion");
                map.put("estado", estado);
                map.put("numeroAutorizacion", numeroAutorizacion);
                map.put("fechaAutorizacion", sdf.parse(fechaAutorizacion));
            }
        } catch (Exception e) {
            Logger.getLogger(LeerFacturaXml.class.getName()).log(Level.SEVERE, null, e);
        }
        return map;
    }
    
}
