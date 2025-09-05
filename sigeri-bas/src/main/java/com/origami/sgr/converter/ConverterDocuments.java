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

/**
 *
 * @author Angel Navarro
 */
@FacesConverter("converterDocuments")
public class ConverterDocuments implements Converter, Serializable {

    @Override
    public Object getAsObject(FacesContext context, UIComponent c, String value) {
        if (value == null || value.isEmpty() == true || value.equals("")) {
            return null;
        }
        try {
            String[] p = value.split(":");
            if (p == null || p.length < 3) {
                return null;
            }
            
            Object find = EjbsCaller.getEjbDocuments().find(Class.forName(p[0]), ReflexionEntity.instanceConsString(p[2], p[1]));
            return find;
        } catch (NullPointerException | ELException | NumberFormatException e) {
            Logger.getLogger(ConverterDocuments.class.getName()).log(Level.SEVERE, value, e);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConverterDocuments.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent c, Object value) {
        if (value == null) {
            return null;
        }
        String temp = ReflexionEntity.getIdEntity(value);
        return temp;
    }
}
