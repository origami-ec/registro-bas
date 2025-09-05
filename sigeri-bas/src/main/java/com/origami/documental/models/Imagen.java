/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.documental.models;

import com.origami.config.SisVars;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.apache.commons.io.FileUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Administrator
 */
public class Imagen implements Serializable {

    protected Long transaccion;
    protected Integer pagina;
    protected String archivo;
    protected StreamedContent stream;
    protected BufferedImage filter;

    private Integer indice;
    private String descripcion;
    private String nombreImagen;
    private String apiUrl;
    private String ruta;
    private List<Nota> notas;

    public Imagen() {
    }

    public Imagen(Long transaccion, Integer pagina, String archivo) {
        this.transaccion = transaccion;
        this.pagina = pagina;
        this.archivo = archivo;
    }

    public Long getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(Long transaccion) {
        this.transaccion = transaccion;
    }

    public Integer getPagina() {
        return pagina;
    }

    public void setPagina(Integer pagina) {
        this.pagina = pagina;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public BufferedImage getFilter() {
        return filter;
    }

    public void setFilter(BufferedImage filter) {
        this.filter = filter;
    }

    public Integer getIndice() {
        return indice;
    }

    public void setIndice(Integer indice) {
        this.indice = indice;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public List<Nota> getNotas() {
        return notas;
    }

    public void setNotas(List<Nota> notas) {
        this.notas = notas;
    }

    public String getNombreImagen() {
        return nombreImagen;
    }

    public void setNombreImagen(String nombreImagen) {
        this.nombreImagen = nombreImagen;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Imagen{");
        sb.append("transaccion=").append(transaccion);
        sb.append(", pagina=").append(pagina);
        sb.append(", archivo=").append(archivo);
        sb.append(", stream=").append(stream);
        sb.append(", filter=").append(filter);
        sb.append(", indice=").append(indice);
        sb.append(", descripcion=").append(descripcion);
        sb.append(", apiUrl=").append(apiUrl);
        sb.append(", ruta=").append(ruta);
        sb.append(", nombreImagen=").append(nombreImagen);
        sb.append(", notas=").append(notas);
        sb.append('}');
        return sb.toString();
    }

    public StreamedContent getStream() {
        try {
            //return DefaultStreamedContent.builder().contentType("image/png").stream(() -> retrieveByteArrayInputStream(new File(archivo))).build();
            URI uri = new URI(this.getUrlWebService());
            return DefaultStreamedContent.builder().contentType("image/png").stream(() -> retrieveByteArrayInputStream(new File(uri))).build();
        } catch (URISyntaxException e) {
            System.out.println(e);
            return new DefaultStreamedContent();
        }
    }

    public static ByteArrayInputStream retrieveByteArrayInputStream(File file) {
        try {
            return new ByteArrayInputStream(FileUtils.readFileToByteArray(file));
        } catch (IOException ex) {
            Logger.getLogger(Imagen.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Permite suaviar la imagen
     *
     * @throws java.io.IOException
     */
    public void suavizarImagen() throws IOException {
        float ninth = 1.0f / 9.0f;
        float[] blurKernel = {
            ninth, ninth, ninth,
            ninth, ninth, ninth,
            ninth, ninth, ninth
        };
        ConvolveOp co = new ConvolveOp(new Kernel(3, 3, blurKernel));
        filter = co.filter(ImageIO.read(new File(archivo)), null);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(filter, "jpg", os);
        stream = DefaultStreamedContent.builder().contentType("image/png").stream(() -> new ByteArrayInputStream(os.toByteArray())).build();
    }

    /**
     * Permite intensificar la imagen
     *
     * @throws java.io.IOException
     */
    public void intensificarImagen() throws IOException {
        float[] blurKernel = {
            0.0f, -1.0f, 0.0f,
            -1.0f, 5.0f, -1.0f,
            0.0f, -1.0f, 0.0f
        };
        ConvolveOp co = new ConvolveOp(new Kernel(3, 3, blurKernel));
        filter = co.filter(ImageIO.read(new File(archivo)), null);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(filter, "jpg", os);
        stream = DefaultStreamedContent.builder().contentType("image/png").stream(() -> new ByteArrayInputStream(os.toByteArray())).build();
    }

    public String getUrlWebService() {
        return SisVars.urlOrigamiDocs + "imagen/" + nombreImagen;
    }

}
