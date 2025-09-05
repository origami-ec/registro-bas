/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author dfcalderio
 */
@FacesValidator("integerValidator")
public class IntegerValidator implements Validator {

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object t) {
        if (t == null) {
            return;
        }
        String value = (String) t;
        if (!StringUtils.isNumeric(value)) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_WARN, null, "Solo numeros."));
        }

    }

}
