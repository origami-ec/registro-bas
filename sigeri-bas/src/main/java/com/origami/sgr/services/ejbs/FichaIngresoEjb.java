/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.services.ejbs;

import com.origami.sgr.entities.CtlgItem;
import com.origami.sgr.entities.RegFicha;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.entities.RegMovimientoFicha;
import com.origami.sgr.models.ActividadesTransaccionales;
import com.origami.sgr.services.interfaces.BitacoraServices;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.services.interfaces.FichaIngresoService;
import com.origami.sgr.services.interfaces.SeqGenMan;
import com.origami.sgr.util.HiberUtil;
import com.origami.sgr.util.Constantes;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.AccessTimeout;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

/**
 *
 * @author Juan Carlos
 */
@Singleton(name = "fichaIngresoEjb")
@Lock(LockType.READ)
@Interceptors(value = {HibernateEjbInterceptor.class})
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@AccessTimeout(value = 320000)
public class FichaIngresoEjb implements FichaIngresoService {

    @Inject
    private SeqGenMan sec;

    @EJB(beanName = "manager")
    private Entitymanager em;

    @Inject
    private BitacoraServices bs;

    @Override
    @Lock(LockType.WRITE)
    synchronized public Long saveListFichasPredial(List<RegFicha> list, List<RegMovimiento> movs) {
        BigInteger periodo = new BigInteger(Integer.toString(Calendar.getInstance().get(Calendar.YEAR)));
        List<RegMovimientoFicha> listMovFic;
        RegMovimientoFicha mf;
        Long numFicha;
        try {
            //numFicha = sec.getSecuenciaGeneral(Variables.secuenciaNumeroFicha).longValue();
            //Long secuencia = numFicha;
            for (RegFicha f : list) {
                //secuencia = secuencia + 1;
                HiberUtil.newTransaction();
                f.setNumFicha(sec.getSecuenciaGeneral(Constantes.secuenciaNumeroFicha).longValue());
                f = (RegFicha) em.merge(f);
                bs.registrarFicha(f, ActividadesTransaccionales.GENERACION_FICHA, periodo, null);
                listMovFic = new ArrayList<>();
                for (RegMovimiento m : movs) {
                    mf = new RegMovimientoFicha();
                    mf.setFicha(f);
                    mf.setMovimiento(m);
                    listMovFic.add(mf);
                }
                if (!listMovFic.isEmpty()) {
                    for (Object entitie : listMovFic) {
                        em.merge(entitie);
                    }
                }
                bs.registrarFichaMovs(f, movs, ActividadesTransaccionales.AGREGAR_REFERENCIA, periodo);
            }
            Collections.sort(list, Comparator.comparing((RegFicha p) -> p.getNumFicha()));
            numFicha = list.get(0).getNumFicha();

        } catch (Exception e) {
            Logger.getLogger(FichaIngresoEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return numFicha;
    }

    @Override
    @Lock(LockType.WRITE)
    synchronized public String saveFichasDuplicadas(RegFicha matriz, Integer cantidad, String usuario, 
            String linderos, List<RegMovimiento> movimientos) {
        List<RegFicha> listFichas = new ArrayList<>();
        RegFicha ficha;
        Long inicio, fin;
        String mensaje;
        try {
            for (int i = 0; i < cantidad; i++) {
                ficha = new RegFicha();
                ficha.setTipoFicha(matriz.getTipoFicha());
                ficha.setTipoPredio(matriz.getTipoPredio());
                ficha.setParroquia(matriz.getParroquia());
                ficha.setClaveCatastral("NO REGISTRA");
                ficha.setDescripcionBien(matriz.getDescripcionBien());
                ficha.setLinderos(linderos);
                ficha.setUserIngreso(usuario);
                ficha.setEstado(matriz.getEstado());
                ficha.setFechaApe(new Date());
                listFichas.add(ficha);
            }
            /*for (RegMovimiento rm : movimientos) {
                rm.setEditable(true);
                em.update(rm);
            }*/
            inicio = this.saveListFichasPredial(listFichas, movimientos);
            fin = inicio + cantidad - 1;
            mensaje = "Fichas creadas desde la numero : " + inicio + ", hasta : " + fin + ".";
        } catch (Exception e) {
            Logger.getLogger(FichaIngresoEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return mensaje;
    }

}
