/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.services.interfaces;

import com.origami.sgr.entities.NprmSri;
import com.origami.sgr.entities.NprmSriCatalogo;
import com.origami.sgr.entities.UafUsuarios;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Anyelo
 */
public interface AnexosLocal {

    public String getAnexoFormulario2Side(Date fechacorte, Date fechaCorteHasta) throws IOException;

    public String getPropiedadesForm2(Long id);

    public String getDatosBienForm2(Long id);

    public File archivoCabeceraResu(Calendar desde, String clave, UafUsuarios ua);

    public File archivoDetalleResu(Calendar desde, String clave);

    public String anexoNrpm(Date fecha, String ruc);

    public List<NprmSriCatalogo> getCatalogoSri(Integer tipoAnexo);

    public NprmSri buscarAnexos(Integer tipoanexo, Integer tipoanexosri, Integer mesSri, Integer anioSri);

    public String anexoNrpm(NprmSri nprmSri, Date fecha, String ruc);

    public String anexoNrpm1(NprmSri nprmSri, Date fecha, String ruc);
    
    public String getAnexo1Resolucion12(Date fechacorte) throws IOException;
    
    public String getAnexo2Side12(Date fechacorte) throws IOException;
    
    public String getAnexo3Side12(Date fechacorte) throws IOException;
}
