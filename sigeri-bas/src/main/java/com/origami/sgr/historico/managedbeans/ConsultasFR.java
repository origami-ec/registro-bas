/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.historico.managedbeans;

import com.origami.config.SisVars;
import com.origami.session.ServletSession;
import com.origami.session.UserSession;
import com.origami.sgr.historico.entities.Cercontrato;
import com.origami.sgr.historico.entities.ImagenView;
import com.origami.sgr.historico.entities.IndiceProp;
import com.origami.sgr.historico.lazy.IndicePropLazy;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.util.FilterName;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Constantes;
import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.swing.text.Document;
import javax.swing.text.rtf.RTFEditorKit;
import com.origami.sgr.util.Querys;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class ConsultasFR implements Serializable {
    
    private static final Logger LOG = Logger.getLogger(ConsultasFR.class.getName());

    @Inject
    private RegistroPropiedadServices reg;
    @Inject
    private Entitymanager em;
    @Inject
    private ServletSession ss;
    @Inject
    private UserSession us;

    protected Map map;
    protected IndicePropLazy indices;
    protected IndiceProp select;
    protected Cercontrato cercontrato = new Cercontrato();
    protected ImagenView imagen = new ImagenView();
    protected SimpleDateFormat sdf = new SimpleDateFormat("MM-yyyy");
    protected List<IndiceProp> detalles = new ArrayList<>();

    protected String antiguo = "13060001";
    protected String cedula, nombre, nombre2, contrato, contratante;
    protected Integer inscripcion, repertorio, mi, desde, hasta, certificado;
    protected Date fecha, fmi;
    protected Boolean solvencia = true;

    protected String texto = "";
    protected String rutaPdf;
    protected String rutaActa;
    protected String rutaRazon;
    protected String salto;

    @PostConstruct
    protected void iniView() {
        try {
            /*rutaPdf = Variables.rutaPdf;
            rutaActa = Variables.rutaActa;
            rutaRazon = Variables.rutaRazon;*/
            rutaPdf = "";
            rutaActa = "";
            rutaRazon = "";
            salto = Constantes.salto;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void updateFilters() {
        try {
            if (cedula.isEmpty() && nombre.isEmpty() && nombre2.isEmpty() && contrato.isEmpty() && contratante.isEmpty()
                    && mi == null && inscripcion == null && repertorio == null && desde == null && hasta == null
                    && certificado == null && fecha == null) {
                JsfUti.messageWarning(null, "Debe ingresar un filtro para las busquedas.", "");
            } else {
                indices = new IndicePropLazy(cedula, nombre, nombre2, contrato, contratante, mi, inscripcion, repertorio, desde, hasta, certificado, solvencia, fecha, fmi);
                JsfUti.update("mainForm:tbViewHistorico:dtIndices");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void reporteIndices() {
        try {
            if (indices != null) {
                List<IndiceProp> list = indices.getWrappedData();
                if (!list.isEmpty()) {
                    ss.instanciarParametros();
                    ss.setTieneDatasource(Boolean.FALSE);
                    ss.setNombreSubCarpeta("registro");
                    ss.setNombreReporte("Indices");
                    ss.agregarParametro("USER", us.getName_user());
                    ss.setDataSource(list);
                    JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
                } else {
                    JsfUti.messageWarning(null, "La consulta no muestra resultados.", "");
                }
            } else {
                JsfUti.messageWarning(null, "La consulta no muestra resultados.", "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgIndice() {
        if (select.getIndId() != null) {
            JsfUti.update("formIndice");
            JsfUti.executeJS("PF('dlgIndice').show();");
        }
    }

    public void selectTypeFile(Integer code) {
        try {
            String nombrePdf;
            String ruta = null;
            if (select.getIndLibro() != null && select.getIndFecha() != null) {
                ruta = select.getIndLibro() + "/" + sdf.format(select.getIndFecha());
            } else {
                JsfUti.messageWarning(null, "Faltan campos en la base.", "");
            }
            switch (code) {
                case 1: //VISUALIZAR ACTA
                    if (ruta != null && select.getIndRepertorio() != null) {
                        this.leerArchivoRtf(rutaActa + ruta, select.getIndRepertorio().toString());
                    } else {
                        JsfUti.messageWarning(null, "Faltan campos en la base.", "");
                    }
                    break;
                case 2: //VISUALIZAR RAZON
                    if (ruta != null && select.getIndRepertorio() != null) {
                        this.leerArchivoRtf(rutaRazon + ruta, select.getIndRepertorio().toString());
                    } else {
                        JsfUti.messageWarning(null, "Faltan campos en la base.", "");
                    }
                    break;
                case 3: //VISUALIZAR PDF
                    if (ruta != null && select.getIndInscripcion() != null) {
                        nombrePdf = "/" + select.getIndInscripcion() + ".pdf";
                        ss.instanciarParametros();
                        ss.setNombreDocumento(rutaPdf + ruta + nombrePdf);
                        JsfUti.redirectNewTab("/rpp/DownLoadFiles");
                    } else {
                        JsfUti.messageWarning(null, "Faltan campos en la base.", "");
                    }
                    break;
                case 4: //VISUALIZAR CERTIFICADO
                    this.visualizarMiCertificado();
                    break;
                case 5: //VISUALIZAR M.I.
                    this.visualizarMi();
                    break;
                case 6: //VISUALIZAR DETALLES
                    this.consultarDetalles();
                    break;
                default:
                    JsfUti.messageWarning(null, "ERROR", "");
                    break;
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageWarning(null, "ERROR", "");
        }
    }

    public void leerArchivoRtf(String ruta, String nombre) {
        try {
            texto = "";
            FilterName fn = new FilterName(nombre + "_", true);
            File folder = new File(ruta);
            if (folder.exists()) {
                for (File f : folder.listFiles(fn)) {
                    texto = texto + "\n" + salto;
                    FileInputStream stream = new FileInputStream(f);
                    RTFEditorKit kit = new RTFEditorKit();
                    Document doc = kit.createDefaultDocument();
                    kit.read(stream, doc, 0);
                    texto = texto + "\n" + doc.getText(0, doc.getLength());
                }
                if (texto.isEmpty()) {
                    JsfUti.messageWarning(null, "No se encuentra el archivo.", "");
                } else {
                    JsfUti.update("formEdit");
                    JsfUti.executeJS("PF('dlgEditCert').show();");
                }
            } else {
                JsfUti.messageWarning(null, "No se encuentra el archivo.", "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void visualizarMiCertificado() {
        try {
            map = new HashMap();
            if (select.getIndNlinea() != null) {
                map.put("cerNumero", select.getIndNlinea());
                cercontrato = (Cercontrato) em.findObjectByParameter(Cercontrato.class, map);
            } else if (select.getIndNumero() != null) {
                map.put("cerNumero", select.getIndNumero());
                cercontrato = (Cercontrato) em.findObjectByParameter(Cercontrato.class, map);
            } else {
                cercontrato = null;
            }
            //cercontrato = (Cercontrato) em.findObjectByParameter(Cercontrato.class, map);
            if (cercontrato != null) {
                //cercontrato.updateEstado();
                /*System.out.println("//estado 1 : " + cercontrato.getCerEstado());
                System.out.println("//estado 2 : " + cercontrato.getCerEstado2());
                cercontrato.setCerEstado(cercontrato.getCerEstado().concat(cercontrato.getCerEstado2()));*/
                JsfUti.update("formInfoMi");
                JsfUti.executeJS("PF('dlgInfoMi').show();");
            } else {
                cercontrato = new Cercontrato();
                JsfUti.messageWarning(null, "No se encuentra el Certificado.", "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void visualizarMi() {
        try {
            map = new HashMap();
            if (select.getIndMi() != null) {
                map.put("matricula", select.getIndMi());
                cercontrato = (Cercontrato) em.findObjectByParameter(Querys.getCertificadoByMi, map);
                if (cercontrato != null) {
                    //cercontrato.updateEstado();
                    JsfUti.update("formInfoMi");
                    JsfUti.executeJS("PF('dlgInfoMi').show();");
                } else {
                    cercontrato = new Cercontrato();
                    JsfUti.messageWarning(null, "No se encuentra el Certificado.", "");
                }
            } else {
                JsfUti.messageWarning(null, "No tiene numero de M.I.", "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void downLoadMiOld() {
        try {
            if (antiguo != null) {
                map = new HashMap();
                map.put("imgMi", antiguo);
                imagen = (ImagenView) em.findObjectByParameter(ImagenView.class, map);
                if (imagen != null && imagen.getOid() != null) {
                    JsfUti.redirectNewTab("/rpp/OmegaDownLoad?code=" + imagen.getOid() + "&name=" + imagen.getImgMi());
                } else {
                    JsfUti.messageWarning(null, "No se encuentra el MI.", "");
                }
            } else {
                JsfUti.messageWarning(null, "No se encuentra el MI.", "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void consultarDetalles() {
        try {
            if (select.getIndFecha() != null) {
                map = new HashMap();
                map.put("indFecha", select.getIndFecha());
                if (select.getIndRepertorio() != null) {
                    map.put("indRepertorio", select.getIndRepertorio());
                }
                if (select.getIndInscripcion() != null) {
                    map.put("indInscripcion", select.getIndInscripcion());
                }
                if (select.getIndContrato() != null) {
                    map.put("indContrato", select.getIndContrato());
                }
                detalles = em.findObjectByParameterList(IndiceProp.class, map);
                if (detalles != null & !detalles.isEmpty()) {
                    JsfUti.update("formMovimientoRegistral");
                    JsfUti.executeJS("PF('dlgMovimientoRegistral').show();");
                } else {
                    JsfUti.messageWarning(null, "No se encontraron los detalles.", "");
                }
            } else {
                JsfUti.messageWarning(null, "Falta el campo fecha.", "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public IndicePropLazy getIndices() {
        return indices;
    }

    public void setIndices(IndicePropLazy indices) {
        this.indices = indices;
    }

    public String getNameLibro(Integer codigo) {
        return reg.getLibroByCodigo(codigo.longValue());
    }

    public IndiceProp getSelect() {
        return select;
    }

    public void setSelect(IndiceProp select) {
        this.select = select;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContrato() {
        return contrato;
    }

    public void setContrato(String contrato) {
        this.contrato = contrato;
    }

    public Integer getInscripcion() {
        return inscripcion;
    }

    public void setInscripcion(Integer inscripcion) {
        this.inscripcion = inscripcion;
    }

    public Integer getRepertorio() {
        return repertorio;
    }

    public void setRepertorio(Integer repertorio) {
        this.repertorio = repertorio;
    }

    public Integer getMi() {
        return mi;
    }

    public void setMi(Integer mi) {
        this.mi = mi;
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

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Cercontrato getCercontrato() {
        return cercontrato;
    }

    public void setCercontrato(Cercontrato cercontrato) {
        this.cercontrato = cercontrato;
    }

    public String getAntiguo() {
        return antiguo;
    }

    public void setAntiguo(String antiguo) {
        this.antiguo = antiguo;
    }

    public ImagenView getImagen() {
        return imagen;
    }

    public void setImagen(ImagenView imagen) {
        this.imagen = imagen;
    }

    public Integer getCertificado() {
        return certificado;
    }

    public void setCertificado(Integer certificado) {
        this.certificado = certificado;
    }

    public String getNombre2() {
        return nombre2;
    }

    public void setNombre2(String nombre2) {
        this.nombre2 = nombre2;
    }

    public String getContratante() {
        return contratante;
    }

    public void setContratante(String contratante) {
        this.contratante = contratante;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getFmi() {
        return fmi;
    }

    public void setFmi(Date fmi) {
        this.fmi = fmi;
    }

    public Boolean getSolvencia() {
        return solvencia;
    }

    public void setSolvencia(Boolean solvencia) {
        this.solvencia = solvencia;
    }

    public List<IndiceProp> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<IndiceProp> detalles) {
        this.detalles = detalles;
    }

}
