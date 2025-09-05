/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sql;

import java.sql.SQLException;
import javax.ejb.Local;

/**
 *
 * @author eduar
 */
@Local
public interface ConsultasSQLService {
    
    public Integer getSecuenciaRepertorio(Integer anio) throws SQLException;

    public String getcontentTypeDoc(Long oid);

}
