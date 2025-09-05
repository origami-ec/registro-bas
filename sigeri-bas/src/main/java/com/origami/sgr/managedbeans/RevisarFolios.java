/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans;

import com.origami.config.SisVars;
import com.origami.session.ServletSession;
import com.origami.sgr.entities.RegLibro;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.lazymodels.RegMovimientosLazy;
import com.origami.sgr.models.NamedItem;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Querys;
import java.io.Serializable;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author origami
 */
@Named
@ViewScoped
public class RevisarFolios implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(RevisarFolios.class.getName());

    @Inject
    private Entitymanager em;
    @Inject
    private ServletSession ss;
    @Inject
    private RegistroPropiedadServices rps;

    protected RegMovimientosLazy foliosLazy;
    protected RegMovimiento movimiento;
    protected List<RegLibro> regLibroList;
    protected RegLibro libro;
    protected Date inscripcionDesde;
    protected Date inscripcionHasta;
    protected SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    protected Calendar cal;

    private Integer desde;
    private Integer hasta;

    @PostConstruct
    public void initView() {
        try {
            cal = Calendar.getInstance();
            cal.clear();
            cal.set(2019, 5, 23); //fecha de salida a produccion - 23/Junio/2019
            regLibroList = em.findAllEntCopy(Querys.getRegLibroList);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void consultarFolios() {
        try {
            foliosLazy = null;
            if (libro != null && inscripcionDesde != null && inscripcionHasta != null) {
                if (!inscripcionDesde.after(inscripcionHasta)) {
                    if (inscripcionHasta.after(cal.getTime())) {
                        foliosLazy = new RegMovimientosLazy(libro, inscripcionDesde, this.getFechaHasta(inscripcionHasta));
                    } else {
                        foliosLazy = new RegMovimientosLazy(libro, inscripcionDesde, inscripcionHasta);
                    }
                } else {
                    JsfUti.messageWarning(null, "Fecha Hasta debe ser mayor o igual a Fecha Desde.", "");
                }
            } else {
                JsfUti.messageWarning(null, "Libro y Fechas campos obligatorios para la busqueda.", "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public Date getFechaHasta(Date actual) {
        Calendar fecha = Calendar.getInstance();
        fecha.setTime(actual);
        fecha.add(Calendar.DAY_OF_MONTH, 1);
        fecha.add(Calendar.SECOND, -1);
        return fecha.getTime();
    }

    public void showDlgFolioSelect(RegMovimiento mov) {
        try {
            movimiento = mov;
            JsfUti.update("formMovRegSelec");
            JsfUti.executeJS("PF('dlgMovRegSelec').show();");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            Logger.getLogger(RevisarFolios.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void generarPdfFolios() {
        try {
            if (desde == null && hasta == null) {
                JsfUti.messageError(null, "Debe ingresa el desde y hasta.", "");
                return;
            }
            if (desde > hasta) {
                JsfUti.messageError(null, "Desde no debe ser mayor al hasta.", "");
                return;
            }
            ss.instanciarParametros();
            ss.setNombreReporte("foliacion");
            ss.setTieneDatasource(false);
            ss.setNombreSubCarpeta("archivos");
            ss.agregarParametro("ciRuc", ("Folio:" + desde + "-" + hasta));
            List<NamedItem> list = new ArrayList<>();
            for (int i = desde; i <= hasta; i++) {
                list.add(new NamedItem(new BigInteger(String.valueOf(i))));
            }
            ss.setDataSource(list);
            ss.agregarParametro("desde", desde);
            ss.agregarParametro("hasta", hasta);
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
//            JsfUti.redirectNewTab(SisVars.urlbase + "PdfFolio");
        } catch (Exception ex) {
            Logger.getLogger(RevisarFolios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateMovimiento() {
        try {
            if (movimiento.getFolioInicio() != null && movimiento.getFolioFin() != null
                    && !movimiento.getNumTomo().isEmpty()) {
                em.update(movimiento);
                JsfUti.update("formFolios");
                JsfUti.executeJS("PF('dlgMovRegSelec').hide();");
                JsfUti.messageInfo(null, "Se actualizó el movimiento con éxito.", "");
            } else {
                JsfUti.messageWarning(null, "Debe ingresar los 3 campos obligatorios(*).", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            Logger.getLogger(RevisarFolios.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void documentalDSM(RegMovimiento mov) {
        try {
            if (mov.getFolioInicio() != null && mov.getFolioFin() != null && !mov.getNumTomo().isEmpty()) {
                Long result = 0L;
                //Long result = rps.insertDSM(mov);
                if (result != null) {
                    mov.setFolioAnterior(result.intValue());
                    em.update(mov);
                    JsfUti.messageInfo(null, "Se registró el indice DSM.", "Id de transaccion: " + result);
                } else {
                    JsfUti.messageWarning(null, "No se puedo registrar el indice DSM.", "");
                }
            } else {
                JsfUti.messageWarning(null, "Primero debe estar foliada la inscripcion antes de indexar.", "");
            }
        } catch (Exception e) {
            System.out.println(e);
            JsfUti.messageWarning(null, "ERROR DE TRANSACCION.", "");
        }
    }

    public RegMovimientosLazy getFoliosLazy() {
        return foliosLazy;
    }

    public void setFoliosLazy(RegMovimientosLazy foliosLazy) {
        this.foliosLazy = foliosLazy;
    }

    public List<RegLibro> getRegLibroList() {
        return regLibroList;
    }

    public void setRegLibroList(List<RegLibro> regLibroList) {
        this.regLibroList = regLibroList;
    }

    public RegLibro getLibro() {
        return libro;
    }

    public void setLibro(RegLibro libro) {
        this.libro = libro;
    }

    public Date getInscripcionDesde() {
        return inscripcionDesde;
    }

    public void setInscripcionDesde(Date inscripcionDesde) {
        this.inscripcionDesde = inscripcionDesde;
    }

    public Date getInscripcionHasta() {
        return inscripcionHasta;
    }

    public void setInscripcionHasta(Date inscripcionHasta) {
        this.inscripcionHasta = inscripcionHasta;
    }

    public RegMovimiento getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(RegMovimiento movimiento) {
        this.movimiento = movimiento;
    }

    public Integer getDesde() {
        return desde;
    }

    public void setDesde(Integer desde) {
        this.desde = desde;
    }

    public Integer getHasta() {
        return hasta;
    }

    public void setHasta(Integer hasta) {
        this.hasta = hasta;
    }

}
