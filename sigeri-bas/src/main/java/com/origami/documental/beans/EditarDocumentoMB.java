/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.documental.beans;

import com.google.gson.Gson;
import com.origami.config.SisVars;
import com.origami.documental.models.ArchivoDocs;
import com.origami.documental.models.Color;
import com.origami.documental.models.Data;
import com.origami.documental.models.Imagen;
import com.origami.documental.models.IndexacionCampo;
import com.origami.documental.models.Nota;
import com.origami.documental.services.DocumentalService;
import com.origami.session.ServletSession;
import com.origami.session.UserSession;
import com.origami.sgr.services.interfaces.ArchivosService;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Utils;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.imageio.stream.FileImageOutputStream;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.data.PageEvent;
import org.primefaces.extensions.event.ImageAreaSelectEvent;
import org.primefaces.model.CroppedImage;

/**
 *
 * @author ORIGAMI
 */
@Named
@ViewScoped
public class EditarDocumentoMB implements Serializable {
    
    private static final Logger LOG = Logger.getLogger(EditarDocumentoMB.class.getName());

    @Inject
    protected DocumentalService doc;
    @Inject
    protected UserSession session;
    @Inject
    protected ServletSession ss;
    @Inject
    protected ArchivosService arc;

    private Date fecha;
    private Boolean crearNota, verTodasNotas, recortarImagen;
    private ArchivoDocs archivo;
    private Nota nota, notaSeleccionada;
    private Integer indexImagen;
    private List<Color> colores;
    private Color color;
    private CroppedImage croppedImage;
    private String newImageName;
    private String tipoAnotacion;

    private List<Data> sellos;
    private Data sello;

    protected Boolean enableOCR;
    protected Integer visualizacion;
    protected String visorUrl;
    protected String archivoId;
    protected String transaccion;
    protected String tramite;
    protected String textAux;
    protected Integer indice;
    protected Integer pagina;
    protected List<Imagen> imagenes;
    protected List<ArchivoDocs> archivos;

    public void doPreRenderView() {
        if (!JsfUti.isAjaxRequest()) {
            this.initView();
        }
    }
    
    public void initView() {
        try {
            pagina = 0;
            visualizacion = 1;
            enableOCR = false;
            nota = new Nota();
            fecha = new Date();
            verTodasNotas = Boolean.FALSE;
            colores = doc.getColores();
            sellos = doc.getSellos();
            if (archivoId != null && !archivoId.isEmpty()) {
                archivo = doc.consultarArchivo(new ArchivoDocs(archivoId));
            } else if (transaccion != null && tramite != null) {
                archivos = doc.buscarArchivos(transaccion, tramite);
                if (indice == null || indice < 0) {
                    indice = 0;
                }
                archivo = archivos.get(indice);
                imagenes = archivo.getImagenes();
            } else {
                JsfUti.messageError(null, "No se encuentra el archivo", "Intente con otro documento.");
            } 
            this.inicializarVisor();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void inicializarVisor() {
        if (archivo != null && archivo.getNombre() != null) {
            ss.borrarDatos();
            ss.setUrlWebService(SisVars.urlOrigamiMedia + "resource/download/pdf/" + archivo.getNombre());
            visorUrl = SisVars.urlbase + "pdfjs/web/visor.html?file=" + JsfUti.getHostContextUrl() + "ReporteWS";
        }
    }

    public void selectEndListener(ImageAreaSelectEvent e) {

        switch (tipoAnotacion) {
            case SisVars.NOTA:
                nota.setAlto(SisVars.maxAltoNota);
                nota.setAncho(SisVars.maxAnchoNota);
                break;
            case SisVars.LINEA:
                nota.setAlto(SisVars.maxAltoLinea);
                nota.setAncho(e.getWidth());
                break;
            case SisVars.REMARCADO:
                nota.setAlto(SisVars.maxAltoRemarcado);
                nota.setAncho(e.getWidth());
                break;
            case SisVars.SELLO:
                nota.setAlto(e.getHeight());
                nota.setAncho(e.getWidth());
                break;
            default:
                nota.setAlto(e.getHeight());
                nota.setAncho(e.getWidth());
                break;
        }

        nota.setTipo(tipoAnotacion);

        nota.setFecha(Utils.getDate(new Date()));
        nota.setUsuario(session.getUsuarioDocs());
        nota.setPosicionX1(e.getX1());
        nota.setPosicionX2(e.getX2());
        nota.setPosicionY1(e.getY1());
        nota.setPosicionY2(e.getY2());

        indexImagen = Integer.valueOf(e.getComponent().getClientId().replace("dtPdfs:", "").replace(":areaSelect", ""));
        System.out.println("indexImagen: " + indexImagen);
        System.out.println("e.getWidth(): " + e.getWidth());
        System.out.println("e.getHeight(): " + e.getHeight());

        JsfUti.executeJS("PF('dlgNota').show()");
        JsfUti.update("formNota");

    }

    public void actualizarIndexacion() {
        if (Utils.isEmptyString(archivo.getDetalleDocumento())) {
            JsfUti.messageError(null, Messages.descripcionArchivo, "");
            return;
        }
        for (IndexacionCampo ic : archivo.getDetalles()) {
            if (ic.getObligatorio()) {
                if (Utils.isEmptyString(ic.getDetalle())) {
                    JsfUti.messageError(null, Messages.camposObligatorios, "");
                    return;
                }
            }
        }
        archivo = doc.actualizarDatosIndex(archivo);
        JsfUti.messageInfo(null, Messages.correcto, "");
    }

    public void grabarNota() {
        if (nota != null && Utils.isEmptyString(nota.getTitulo())) {
            JsfUti.messageError(null, "Debe ingresar el título de la anotación", "");
            return;
        }
        switch (tipoAnotacion) {
            case SisVars.NOTA:
                if (nota != null && Utils.isEmptyString(nota.getNota())) {
                    JsfUti.messageError(null, "Debe ingresar una descripción", "");
                    return;
                }
                break;
            case SisVars.SELLO:
                if (sello == null) {
                    JsfUti.messageError(null, "Debe escoger un sello", "");
                    return;
                } else {
                    nota.setNota(sello.getData()); ///URL DEL SELLO
                }
                break;
            default:
                nota.setNota("");
                break;
        }
        if (!tipoAnotacion.equals(SisVars.SELLO)) { //SI ES SELLO NO LLEVA COLOR
            if (color == null || Utils.isEmptyString(color.getColor())) {
                JsfUti.messageError(null, "Debe escoger un color", "");
                return;
            }
        } else {
            color = new Color();
            color.setColor("#FFFFFF");
        }

        nota.setColor(color.getColor());
        System.out.println("nota: " + nota.toString());
        Data rest = doc.guardarNota(archivo.getId(), nota, indexImagen);
        if (rest != null) {
            if (rest.getId() == 0) {
                archivo = new Gson().fromJson(rest.getData(), ArchivoDocs.class);
                JsfUti.messageInfo(null, tipoAnotacion + " guardada correctamente", "");
                JsfUti.executeJS("PF('dlgNota').hide()");
                List<Nota> notas = archivo.getImagenes().get(indexImagen).getNotas();
                visualizarNota(notas.get(notas.size() - 1));
                JsfUti.messageInfo(null, Messages.correcto, "");
            } else {
                JsfUti.messageInfo(null, "Intente nuevamente", rest.getStatus());
                JsfUti.executeJS("PF('dlgNota').hide()");
            }

        } else {
            JsfUti.messageError(null, "No se pudo guardar la anotación", Messages.intenteNuevamente);
        }
    }

    public void visualizarNota(Nota nota) {
        notaSeleccionada = nota;
        verTodasNotas = Boolean.FALSE;
        JsfUti.update("dtPdfs");

    }

    public void eliminarNota(Nota notaEliminar, int indexNota, int indexImagen) {
        Data rest = doc.eliminarNota(archivo.getId(), notaEliminar, indexImagen, indexNota);
        if (rest != null) {
            if (rest.getId() == 0) {
                archivo = new Gson().fromJson(rest.getData(), ArchivoDocs.class);
                System.out.println("eliminarNota - archivo.getId(): " + archivo.getId());
                JsfUti.messageInfo(null, "Nota eliminada correctamente", "");
                JsfUti.update("dtPdfs:" + indexNota + ":pngNota");
                //JsfUti.update("dtPdfs");
            } else {
                JsfUti.messageInfo(null, "Intente nuevamente", rest.getStatus());
                JsfUti.executeJS("PF('dlgNota').hide()");
            }

        } else {
            JsfUti.messageError(null, "No se pudo eliminar la nota", "Intente nuevamente");
        }
    }

    public void crop() {
        if (croppedImage == null) {
            return;
        }

        setNewImageName(getRandomImageName());
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        String newFileName = externalContext.getRealPath("") + File.separator + "resources" + File.separator + "demo"
                + File.separator + "images" + File.separator + "crop" + File.separator + getNewImageName() + ".jpg";

        FileImageOutputStream imageOutput;
        try {
            imageOutput = new FileImageOutputStream(new File(newFileName));
            imageOutput.write(croppedImage.getBytes(), 0, croppedImage.getBytes().length);
            imageOutput.close();
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error", "Cropping failed."));
            return;
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Success", "Cropping finished."));
    }

    private String getRandomImageName() {
        int i = (int) (Math.random() * 100000);
        return String.valueOf(i);
    }

    public void limpiarNotaSeleccionada() {

        notaSeleccionada = null;
    }

    public void cerrarDialogoNota() {
        JsfUti.executeJS("PF('dlgNota').hide()");
    }

    public void visualizarDocumento() {
        if (!archivo.getFormato().startsWith("IMG")) {
            this.getPDF(archivo.getRuta());
        } else {
            JsfUti.redirectNewTab(Utils.getUrlDownloadFile(archivo.getRuta()));
        }
    }

    public void imprimirNotas() {
        ArchivoDocs rest = doc.imprimirNotas(archivo);
        if (rest != null && Utils.isNotEmptyString(rest.getRutaNotas())) {
            this.getPDF(rest.getRutaNotas());
        } else {
            JsfUti.messageError(null, Messages.intenteNuevamente, "");
        }
    }

    public void getPDF(String ruta) {
        if (ruta == null) {
            JsfUti.messageInfo(null, "Error.", "No existe el documento");
            return;
        }
        String[] tipo = ruta.split("\\.");
        String lastOne = tipo[tipo.length - 1];

        if (lastOne.equals("zip")) {
            JsfUti.messageInfo(null, "Información.", "El archivo es de tipo ZIP no se puede visualizar, Debe descargarlo");
            return;
        } else if (lastOne.equals("doc") || lastOne.equals("docx") || lastOne.equals("xls") || lastOne.equals("xlsx")) {
            JsfUti.messageInfo(null, "Información.", "El archivo es de tipo " + tipo[1]
                    + " no se puede visualizar, Debe descargarlo");
            JsfUti.redirectNewTab(Utils.getUrlDownloadFile(ruta));
            return;
        }
        ss.borrarDatos();
        ss.setUrlWebService(Utils.getUrlViewFile(ruta));
        JsfUti.redirectNewTab(SisVars.urlbase + "ReporteWS");
    }

    public void selectPage(PageEvent event) {
        try {
            pagina = event.getPage();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void selectAreaImage(final ImageAreaSelectEvent e) {
        try {
            String url = imagenes.get(pagina).getApiUrl();
            this.textAux = arc.getTextOfImage(url, e.getX1(), e.getX2(), e.getY1(), 
                    e.getY2(), e.getImgWidth(), e.getImgHeight());
            if (textAux != null) {
                // Mostramos el texto
                JsfUti.executeJS("PF('dlgTextSelect').show()");
                JsfUti.update("frmTextAux");
            }
        } catch (Exception ex) {
            System.out.println(e);
        }
    }
    
    public void seleccionarImagenes(ArchivoDocs doc) {
        try {
            archivo = doc;
            imagenes = doc.getImagenes();
            this.inicializarVisor();
            visualizacion = 1;
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void descargarDocumento(ArchivoDocs doc) {
        try {
            //inline
            //JsfUti.redirectNewTab(SisVars.urlOrigamiMedia + "resource/download/pdf/" + doc.getNombre());
            //attachment
            JsfUti.redirectNewTab(SisVars.urlOrigamiMedia + "resource/download/document/" + doc.getNombre());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }
    
    public ArchivoDocs getArchivo() {
        return archivo;
    }

    public void setArchivo(ArchivoDocs archivo) {
        this.archivo = archivo;
    }

    public Nota getNota() {
        return nota;
    }

    public void setNota(Nota nota) {
        this.nota = nota;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Nota getNotaSeleccionada() {
        return notaSeleccionada;
    }

    public void setNotaSeleccionada(Nota notaSeleccionada) {
        this.notaSeleccionada = notaSeleccionada;
    }

    public Boolean getCrearNota() {
        return crearNota;
    }

    public void setCrearNota(Boolean crearNota) {
        this.crearNota = crearNota;
    }

    public Boolean getVerTodasNotas() {
        return verTodasNotas;
    }

    public void setVerTodasNotas(Boolean verTodasNotas) {
        this.verTodasNotas = verTodasNotas;
    }

    public List<Color> getColores() {
        return colores;
    }

    public void setColores(List<Color> colores) {
        this.colores = colores;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public CroppedImage getCroppedImage() {
        return croppedImage;
    }

    public void setCroppedImage(CroppedImage croppedImage) {
        this.croppedImage = croppedImage;
    }

    public String getNewImageName() {
        return newImageName;
    }

    public void setNewImageName(String newImageName) {
        this.newImageName = newImageName;
    }

    public Boolean getRecortarImagen() {
        return recortarImagen;
    }

    public void setRecortarImagen(Boolean recortarImagen) {
        this.recortarImagen = recortarImagen;
    }

    public String getTipoAnotacion() {
        return tipoAnotacion;
    }

    public void setTipoAnotacion(String tipoAnotacion) {
        this.tipoAnotacion = tipoAnotacion;
    }

    public List<Data> getSellos() {
        return sellos;
    }

    public void setSellos(List<Data> sellos) {
        this.sellos = sellos;
    }

    public Data getSello() {
        return sello;
    }

    public void setSello(Data sello) {
        this.sello = sello;
    }

    public Integer getVisualizacion() {
        return visualizacion;
    }

    public void setVisualizacion(Integer visualizacion) {
        this.visualizacion = visualizacion;
    }

    public String getVisorUrl() {
        return visorUrl;
    }

    public void setVisorUrl(String visorUrl) {
        this.visorUrl = visorUrl;
    }

    public String getArchivoId() {
        return archivoId;
    }

    public void setArchivoId(String archivoId) {
        this.archivoId = archivoId;
    }

    public String getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(String transaccion) {
        this.transaccion = transaccion;
    }

    public String getTramite() {
        return tramite;
    }

    public void setTramite(String tramite) {
        this.tramite = tramite;
    }

    public List<Imagen> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<Imagen> imagenes) {
        this.imagenes = imagenes;
    }

    public List<ArchivoDocs> getArchivos() {
        return archivos;
    }

    public void setArchivos(List<ArchivoDocs> archivos) {
        this.archivos = archivos;
    }

    public Integer getIndice() {
        return indice;
    }

    public void setIndice(Integer indice) {
        this.indice = indice;
    }

    public Boolean getEnableOCR() {
        return enableOCR;
    }

    public void setEnableOCR(Boolean enableOCR) {
        this.enableOCR = enableOCR;
    }

    public String getTextAux() {
        return textAux;
    }

    public void setTextAux(String textAux) {
        this.textAux = textAux;
    }

    public Integer getPagina() {
        return pagina;
    }

    public void setPagina(Integer pagina) {
        this.pagina = pagina;
    }

}
