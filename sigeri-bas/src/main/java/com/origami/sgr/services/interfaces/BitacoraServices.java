/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.services.interfaces;

import com.origami.sgr.entities.RegCertificado;
import com.origami.sgr.entities.RegEnteInterviniente;
import com.origami.sgr.entities.RegFicha;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.entities.RegMovimientoCliente;
import com.origami.sgr.entities.RegMovimientoFicha;
import com.origami.sgr.entities.RegMovimientoReferencia;
import com.origami.sgr.models.ActividadesTransaccionales;
import com.origami.sgr.models.MovimientoModel;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author CarlosLoorVargas
 */
@Local
public interface BitacoraServices {

    public Object registrarFicha(RegFicha f, ActividadesTransaccionales actividadTransaccional, BigInteger periodo, BigInteger orden);

    public Object registrarMovimiento(MovimientoModel movimientoModel, RegMovimiento m, ActividadesTransaccionales actividadTransaccional, BigInteger periodo);

    public boolean registrarFichaProp(RegFicha f, RegEnteInterviniente rei, ActividadesTransaccionales actividadTransaccional, BigInteger periodo);

    public boolean registrarFichaMov(RegFicha f, RegMovimiento mov, ActividadesTransaccionales actividadTransaccional, BigInteger periodo);

    public boolean registrarFichaMovs(RegFicha f, List<RegMovimiento> movs, ActividadesTransaccionales actividadTransaccional, BigInteger periodo);

    public boolean registrarMovProps(RegMovimiento m, List<RegMovimientoCliente> mcs, ActividadesTransaccionales actividadTransaccional, BigInteger periodo);

    public boolean registrarMovFichas(RegMovimiento m, List<RegMovimientoFicha> fs, ActividadesTransaccionales actividadTransaccional, BigInteger periodo);

    public boolean registrarMovMov(RegMovimiento m, RegMovimientoReferencia mv, BigInteger periodo, ActividadesTransaccionales actividadTransaccional);

    public boolean registrarMovMovs(RegMovimiento m, List<RegMovimientoReferencia> movs, ActividadesTransaccionales actividadTransaccional, BigInteger periodo);

    public boolean registrarMovInterv(RegMovimiento m, RegEnteInterviniente rei, ActividadesTransaccionales actividadTransaccional, BigInteger periodo);

    public boolean registrarMovMarginacion(RegMovimiento m, String observacion, BigInteger periodo);

    public boolean registrarEdicionCertificado(RegCertificado certificado, ActividadesTransaccionales actividadTransaccional, BigInteger periodo);

}
