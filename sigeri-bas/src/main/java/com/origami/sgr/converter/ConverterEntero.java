/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.converter;

import com.origami.sgr.util.EjbsCaller;
import com.origami.sgr.util.ReflexionEntity;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.el.ELException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.apache.commons.lang.math.NumberUtils;

/**
 *
 * @author Angel Navarro
 */
@FacesConverter("converterInt")
public class ConverterEntero implements Converter, Serializable {

    @Override
    public Object getAsObject(FacesContext context, UIComponent c, String value) {
        if (value == null || value.isEmpty() == true || value.equals("")) {
            return null;
        }
        try {
            String[] p = value.split(":");
            if (!NumberUtils.isNumber(p[1])) {
                return null;
            }
            return EjbsCaller.getTransactionManager().find(Class.forName(p[0]), Integer.parseInt(p[1]));
        } catch (NullPointerException | ELException | NumberFormatException e) {
            Logger.getLogger(ConverterEntero.class.getName()).log(Level.SEVERE, value, e);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConverterEntero.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent c, Object value) {
        if (value == null) {
            return null;
        }
        return ReflexionEntity.getIdEntity(value);
    }
}
