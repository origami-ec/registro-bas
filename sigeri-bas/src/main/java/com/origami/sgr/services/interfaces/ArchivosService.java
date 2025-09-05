/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.services.interfaces;

import com.origami.archivosHist.ModelText;
import com.origami.documental.models.Imagen;
import com.origami.sgr.entities.RegMovimientoCliente;
import com.origami.sgr.models.index.TbBlob;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Angel Navarro
 */
@Local
public interface ArchivosService {

    public List<TbBlob> getImageMovimiento(RegMovimientoCliente rmc);

    public List<TbBlob> getImageMovimiento(Long id, Integer numRepertorio, Integer numInscripcion, Date fechaInscripcion);

    public TbBlob getTiffMarginacion(Long idBlob, Date fecha);

    public void addTextWatermark2(List<ModelText> annotaciones, String type, BufferedImage image, ByteArrayOutputStream destination) throws IOException;

    public void addTextWatermark2(List<ModelText> annotaciones, String type, BufferedImage image, ByteArrayOutputStream destination, Boolean createTempFile) throws IOException;

    public List<ModelText> getMarginaciones(byte[] anota, Integer numPage) throws Exception;

    public void addTextWatermark2(List<ModelText> annotaciones, String type, BufferedImage image, OutputStream destination) throws IOException;

    /**
     * Carga la imagen original la escala al tamaño pasado por parametros,
     * despues envia a recortar la imagen
     *
     * @param rutaArchivo Ruta de Imagen que se va procesar, recorta la imagen
     * para luego extraer los caracteres con el OCR.
     * @param x1 Primer punto en el eje X
     * @param x2 Segundo punto en el eje X
     * @param y1 Primer punto en el eje Y
     * @param y2 Segundo punto en el eje X
     * @param imgWidth Ancho de la imagen orginal.
     * @param imgHeight Alto de la imagen original.
     * @return Imagen recortada con los parametros expecificados.
     */
    public BufferedImage recortarImagen(String rutaArchivo, int x1, int x2, int y1, int y2, int imgWidth, int imgHeight);

    /**
     * Carga la imagen original la escala al tamaño pasado por parametros,
     * despues envia a recortar la imagen, finalmente envia a extraer el texto.
     *
     * @param rutaArchivo Ruta de Imagen que se va procesar, recorta la imagen
     * para luego extraer los caracteres con el OCR.
     * @param x1 Primer punto en el eje X
     * @param x2 Segundo punto en el eje X
     * @param y1 Primer punto en el eje Y
     * @param y2 Segundo punto en el eje X
     * @param imgWidth Ancho de la imagen orginal.
     * @param imgHeight Alto de la imagen original.
     * @return Texto encontrado
     */
    public String getTextOfImage(String rutaArchivo, int x1, int x2, int y1, int y2, int imgWidth, int imgHeight);

    /**
     * Toma la imagen que se le pasa y busca los caracteres.
     *
     * @param recortarImagen Imgen a procesar.
     * @return Texto enconrado en la imagen
     */
    public String getTextOfImage(BufferedImage recortarImagen);

    public String rtfToHtml(String rtf) throws IOException;

    public String getTextOfImage(BufferedImage selectImage, int x1, int x2, int y1, int y2, int imgWidth, int imgHeight);
    
    public List<Imagen> separarTiff(byte[] data, Long transanccion, Long movimiento);
    
    public List<Imagen> separarTiffTramite(Long transanccion, Long tramite);

}
