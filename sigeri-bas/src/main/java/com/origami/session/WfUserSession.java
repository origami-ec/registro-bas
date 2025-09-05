/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.session;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author fernando
 */
public interface WfUserSession extends Serializable {

    String getDepartamento();

    Boolean getEsDirector();

    String getName();

    String getName_user();

    String getNombrePersonaLogeada();

    Boolean getTemp();

    String getUrlSolicitada();

    Long getUserId();

    List<Long> getRoles();

    List<Long> getDepts();

    String getIpClient();

}
