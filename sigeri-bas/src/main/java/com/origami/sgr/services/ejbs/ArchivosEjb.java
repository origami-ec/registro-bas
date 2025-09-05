/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.services.ejbs;

import com.origami.archivosHist.ModelFont;
import com.origami.archivosHist.ModelPoint;
import com.origami.archivosHist.ModelText;
import com.origami.config.SisVars;
import com.origami.documental.ejbs.DocumentsEjb;
import com.origami.documental.models.Imagen;
import com.origami.session.ServletSession;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.entities.RegMovimientoCliente;
import com.origami.sgr.entities.RegMovimientoFile;
import com.origami.sgr.entities.RegpDocsTramite;
import com.origami.sgr.models.index.TbBlob;
import com.origami.sgr.services.interfaces.DatabaseLocal;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.ReflexionEntity;
import com.origami.sgr.util.Utils;
//import com.sun.media.jai.codec.ImageCodec;
//import com.sun.media.jai.codec.ImageDecoder;
//import com.sun.media.jai.codec.TIFFDecodeParam;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.inject.Inject;
import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.EditorKit;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.origami.sgr.services.interfaces.ArchivosService;

/**
 *
 * @author Asilva
 */
@Stateless
public class ArchivosEjb implements ArchivosService {

    @Inject
    private Entitymanager manager;
    @Inject
    private DatabaseLocal dataSourceEjb;
    @Inject
    private ServletSession ss;
    @Inject
    private DocumentsEjb docs;

    @Override
    public List<TbBlob> getImageMovimiento(RegMovimientoCliente rmc) {
        try {
            List<TbBlob> blobs;
            String sql = "SELECT * FROM get_archivos_movimiento(?, ?, ?, ?)";
            List pm = Arrays.asList(rmc.getMovimiento().getId(), rmc.getMovimiento().getNumRepertorio(),
                    rmc.getMovimiento().getNumInscripcion(), Utils.dateFormatPattern("yyyy-MM-dd", rmc.getMovimiento().getFechaInscripcion()));
            //blobs = manager.findList(dataSourceEjb.getDocs(rmc.getMovimiento().getFechaInscripcion()).getConnection(), sql, pm, TbBlob.class);
            blobs = manager.findList(dataSourceEjb.getDocs().getConnection(), sql, pm, TbBlob.class);
            if (Utils.isNotEmpty(blobs)) {
                blobs.forEach((blob) -> {
                    blob.setFechaInscripcion("" + rmc.getMovimiento().getFechaInscripcion().getTime());
                });
            }
            return blobs;
        } catch (SQLException e) {
            Logger.getLogger(ArchivosEjb.class.getName()).log(Level.SEVERE, "getImageMovimiento", e);
        }
        return null;
    }

    @Override
    public List<TbBlob> getImageMovimiento(Long id, Integer numRepertorio, Integer numInscripcion, Date fechaInscripcion) {
        try {
            List<TbBlob> blobs;
            String sql = "SELECT * FROM get_archivos_movimiento(?, ?, ?, ?)";
            List pm = Arrays.asList(id, numRepertorio, numInscripcion, Utils.dateFormatPattern("yyyy-MM-dd", fechaInscripcion));
            //blobs = manager.findList(dataSourceEjb.getDocs(fechaInscripcion).getConnection(), sql, pm, TbBlob.class);
            blobs = manager.findList(dataSourceEjb.getDocs().getConnection(), sql, pm, TbBlob.class);
            if (Utils.isNotEmpty(blobs)) {
                blobs.forEach((blob) -> {
                    blob.setFechaInscripcion("" + fechaInscripcion.getTime());
                });
            }
            return blobs;
        } catch (SQLException e) {
            Logger.getLogger(ArchivosEjb.class.getName()).log(Level.SEVERE, "getImageMovimiento", e);
        }
        return null;
    }

    @Override
    public TbBlob getTiffMarginacion(Long idBlob, Date fecha) {
        try {
            String selectSQL = "SELECT * FROM public.get_image_movimiento(?);";
            List<Object> asList = Arrays.asList(idBlob);
            // PREGUNTAR A EDWIN LOS TRES MENES DEL 2015 QUE HAY EN LA BASE 2003-2015
            if (Utils.getAnio(fecha) <= 2014) {
                selectSQL = "SELECT * FROM public.get_image_movimiento(?, ?);";
                asList = Arrays.asList(idBlob, Utils.dateFormatPattern("yyyy-MM-dd", fecha));
            }
            //Connection con = dataSourceEjb.getDocs(fecha).getConnection();
            Connection con = dataSourceEjb.getDocs().getConnection();
            if (con == null) {
                System.out.println("Coneccion null: url ImageServlet");
                return null;
            }
            return manager.find(con, selectSQL, asList, TbBlob.class);
        } catch (SQLException ex) {
            Logger.getLogger(ArchivosEjb.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void addTextWatermark2(List<ModelText> annotaciones, String type, BufferedImage image, ByteArrayOutputStream destination) throws IOException {
        if (annotaciones == null) {
            ImageIO.write(image, type, destination);
            File temp = File.createTempFile(new Date().getTime() + "", null);
            temp.deleteOnExit();
            ImageIO.write(image, type, temp);
            ss.setNombreDocumento(temp.getAbsolutePath());
            return;
        }
        // determine image type and handle correct transparency
        int imageType = "png".equalsIgnoreCase(type) ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
        BufferedImage watermarked = new BufferedImage(image.getWidth(), image.getHeight(), imageType);
        // initializes necessary graphic properties
        Graphics2D w = (Graphics2D) watermarked.getGraphics();
        w.drawImage(image, 0, 0, null);
        AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f);
        w.setComposite(alphaChannel);
        // AGREGAMOS EL TEXTO
        Collections.sort(annotaciones, (ModelText x1, ModelText x2) -> x1.getX1().compareTo(x2.getX1()));
        int count = 0;
        Double ancho = 0d;
        for (ModelText marginacion : annotaciones) {
            if (marginacion.getTextColor() != null) {
                w.setColor(Color.decode(marginacion.getTextColor()));
            } else {
                w.setColor(Color.BLACK);
            }
            Font font = new java.awt.Font(marginacion.getFont().getValue(), java.awt.Font.PLAIN, marginacion.getFont().getHeightFont());
            w.setFont(font);
            AffineTransform at = new AffineTransform();
            at.rotate(Math.PI / -2);
            w.setTransform(at);
            if (count == 0) {
                Float floatValue = marginacion.getX1().floatValue();
                if (floatValue < marginacion.getFont().getHeightFont()) {
                    floatValue = marginacion.getFont().getHeightFont().floatValue();
                }
                ancho = marginacion.getFont().getHeightFont().doubleValue() + floatValue;
                w.drawString(marginacion.getValue(), -marginacion.getY2().floatValue(), floatValue);
            } else {
                w.drawString(marginacion.getValue(), -marginacion.getY2().floatValue(), ancho.floatValue());
                ancho = marginacion.getFont().getHeightFont() + ancho;
            }
            count++;
        }
        ImageIO.write(watermarked, type, destination);
        File temp = File.createTempFile(new Date().getTime() + "", null);
        temp.deleteOnExit();
        ImageIO.write(watermarked, type, temp);
//        System.out.println("file " + temp.getAbsolutePath());
        temp.getAbsolutePath();
        ss.setNombreDocumento(temp.getAbsolutePath());
        w.dispose();
    }

    @Override
    public void addTextWatermark2(List<ModelText> annotaciones, String type, BufferedImage image, ByteArrayOutputStream destination, Boolean createTempFile) throws IOException {
        if (annotaciones == null) {
            ImageIO.write(image, type, destination);
            if (createTempFile) {
                File temp = File.createTempFile(new Date().getTime() + "", null);
                temp.deleteOnExit();
                ImageIO.write(image, type, temp);
                ss.setNombreDocumento(temp.getAbsolutePath());
            }
            return;
        }
        // determine image type and handle correct transparency
        int imageType = "png".equalsIgnoreCase(type) ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
        BufferedImage watermarked = new BufferedImage(image.getWidth(), image.getHeight(), imageType);
        // initializes necessary graphic properties
        Graphics2D w = (Graphics2D) watermarked.getGraphics();
        w.drawImage(image, 0, 0, null);
        AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f);
        w.setComposite(alphaChannel);
        // AGREGAMOS EL TEXTO
        Collections.sort(annotaciones, (ModelText x1, ModelText x2) -> x1.getX1().compareTo(x2.getX1()));
        int count = 0;
        Double ancho = 0d;
        for (ModelText marginacion : annotaciones) {
            if (marginacion.getTextColor() != null) {
                w.setColor(Color.decode(marginacion.getTextColor()));
            } else {
                w.setColor(Color.BLACK);
            }
            Font font = new java.awt.Font(marginacion.getFont().getValue(), java.awt.Font.PLAIN, marginacion.getFont().getHeightFont());
            w.setFont(font);
            AffineTransform at = new AffineTransform();
            at.rotate(Math.PI / -2);
            w.setTransform(at);
            if (count == 0) {
                Float floatValue = marginacion.getX1().floatValue();
                if (floatValue < marginacion.getFont().getHeightFont()) {
                    floatValue = marginacion.getFont().getHeightFont().floatValue();
                }
                ancho = marginacion.getFont().getHeightFont().doubleValue() + floatValue;
                w.drawString(marginacion.getValue(), -marginacion.getY2().floatValue(), floatValue);
            } else {
                w.drawString(marginacion.getValue(), -marginacion.getY2().floatValue(), ancho.floatValue());
                ancho = marginacion.getFont().getHeightFont() + ancho;
            }
            count++;
        }
        ImageIO.write(watermarked, type, destination);
        if (createTempFile) {
            File temp = File.createTempFile(new Date().getTime() + "", null);
            temp.deleteOnExit();
            ImageIO.write(watermarked, type, temp);
//        System.out.println("file " + temp.getAbsolutePath());
            temp.getAbsolutePath();
            ss.setNombreDocumento(temp.getAbsolutePath());
        }
        w.dispose();
    }

    @Override
    public void addTextWatermark2(List<ModelText> annotaciones, String type, BufferedImage image, OutputStream destination) throws IOException {
        if (annotaciones == null) {
            ImageIO.write(image, type, destination);
            File temp = File.createTempFile(new Date().getTime() + "", null);
            temp.deleteOnExit();
            ImageIO.write(image, type, temp);
            ss.setNombreDocumento(temp.getAbsolutePath());
            return;
        }
        // determine image type and handle correct transparency
        int imageType = "png".equalsIgnoreCase(type) ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
        BufferedImage watermarked = new BufferedImage(image.getWidth(), image.getHeight(), imageType);
        // initializes necessary graphic properties
        Graphics2D w = (Graphics2D) watermarked.getGraphics();
        w.drawImage(image, 0, 0, null);
        AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f);
        w.setComposite(alphaChannel);
        // AGREGAMOS EL TEXTO
        Collections.sort(annotaciones, (ModelText x1, ModelText x2) -> x1.getX1().compareTo(x2.getX1()));
        int count = 0;
        Double ancho = 0d;
        for (ModelText marginacion : annotaciones) {
            if (marginacion.getTextColor() != null) {
                w.setColor(Color.decode(marginacion.getTextColor()));
            } else {
                w.setColor(Color.BLACK);
            }
            Font font = new java.awt.Font(marginacion.getFont().getValue(), java.awt.Font.PLAIN, marginacion.getFont().getHeightFont());
            w.setFont(font);
            AffineTransform at = new AffineTransform();
            at.rotate(Math.PI / -2);
            w.setTransform(at);
            if (count == 0) {
                Float floatValue = marginacion.getX1().floatValue();
                if (floatValue < marginacion.getFont().getHeightFont()) {
                    floatValue = marginacion.getFont().getHeightFont().floatValue();
                }
                ancho = marginacion.getFont().getHeightFont().doubleValue() + floatValue;
                w.drawString(marginacion.getValue(), -marginacion.getY2().floatValue(), floatValue);
            } else {
                w.drawString(marginacion.getValue(), -marginacion.getY2().floatValue(), ancho.floatValue());
                ancho = marginacion.getFont().getHeightFont() + ancho;
            }
            count++;
        }
        ImageIO.write(watermarked, type, destination);
        File temp = File.createTempFile(new Date().getTime() + "", null);
        temp.deleteOnExit();
        ImageIO.write(watermarked, type, temp);
        ss.setNombreDocumento(temp.getAbsolutePath());
        w.dispose();
    }

    @Override
    public List<ModelText> getMarginaciones(byte[] anota, Integer numPage) throws Exception {
        List<ModelText> textList = null;
        org.w3c.dom.Document annotations = this.obtenerDocumentDeByte(anota);
        if (annotations != null) {
            NodeList pages = annotations.getDocumentElement().getElementsByTagName("Page");
            for (int i = 0; i < pages.getLength(); i++) {
                Node page = pages.item(i);
                if (Objects.equals(numPage, Integer.valueOf(getValueAttributes(page.getAttributes(), "PageNumber")))) {
                    textList = new ArrayList<>();
                    if (page.getNodeType() == Node.ELEMENT_NODE) {
                        NodeList texts = ((Element) page).getElementsByTagName("Text");
                        for (int k = 0; k < texts.getLength(); k++) {
                            Element item = (Element) texts.item(k);
                            NamedNodeMap attributes = item.getAttributes();
                            ModelText model = new ModelText(getNode(item.getChildNodes(), "TextString").getTextContent().trim());
                            printAttributes(attributes, model);
                            // Tipo de Letra
                            Element node = getNode(item.getChildNodes(), "Font");
                            ModelFont font = new ModelFont(node.getTextContent());
                            printAttributes(node.getAttributes(), font);
                            model.setFont(font);
                            // PUNTOS
                            NodeList points = item.getElementsByTagName("Point");
                            ModelPoint point1 = new ModelPoint();
                            printAttributes(points.item(0).getAttributes(), point1);
                            model.setPoint(point1);
                            ModelPoint point2 = new ModelPoint();
                            printAttributes(points.item(1).getAttributes(), point2);
                            model.setPoint2(point2);
                            textList.add(model);
                        }
                    }
                    return textList;
                }
            }
        }
        return textList;
    }

    private org.w3c.dom.Document obtenerDocumentDeByte(byte[] documentoXml) throws Exception {
        if (documentoXml == null) {
            return null;
        }
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        factory.setCoalescing(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document parse = builder.parse(new ByteArrayInputStream(documentoXml));
        parse.normalize();
        return parse;
    }

    private void printAttributes(NamedNodeMap attributes, Object model) {
        for (int h = 0; h < attributes.getLength(); h++) {
            Node item1 = attributes.item(h);
            if (item1.getNodeType() == Element.ATTRIBUTE_NODE) {
                ReflexionEntity.setCampo(model, item1.getNodeName(), item1.getNodeValue());
            }
        }
    }

    private String getValueAttributes(NamedNodeMap attributes, String nameAttribute) {
        for (int h = 0; h < attributes.getLength(); h++) {
            Node item1 = attributes.item(h);
            if (item1.getNodeName().equalsIgnoreCase(nameAttribute)) {
                return item1.getNodeValue();
            }
        }
        return null;
    }

    private Element getNode(NodeList list, String nameNode) {
        for (int h = 0; h < list.getLength(); h++) {
            Node item1 = list.item(h);
            if (item1.getNodeType() == Node.ELEMENT_NODE) {
                if (item1.getNodeName().equalsIgnoreCase(nameNode)) {
                    return (Element) item1;
                }
            }
        }
        return null;
    }

    @Override
    public BufferedImage recortarImagen(String rutaArchivo, int x1, int x2, int y1, int y2, int imgWidth, int imgHeight) {
        try {
            BufferedImage read = ImageIO.read(new File(rutaArchivo));
            Image tmp = read.getScaledInstance(imgWidth, imgHeight, Image.SCALE_SMOOTH);
            BufferedImage img = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);

            Graphics2D g2d = img.createGraphics();
            g2d.drawImage(tmp, 0, 0, null);
            g2d.dispose();
            if (Math.abs(x1 - x2) > 0 && Math.abs(y2 - y1) > 0) {
                return img.getSubimage(x1, y1, (Math.abs(x1 - x2)), Math.abs(y2 - y1));
            }
        } catch (IOException ex) {
            Logger.getLogger(ArchivosEjb.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String getTextOfImage(String rutaArchivo, int x1, int x2, int y1, int y2, int imgWidth, int imgHeight) {
        try {
            //BufferedImage read = ImageIO.read(new File(rutaArchivo));
            BufferedImage read = ImageIO.read(new URL(rutaArchivo));
            Image tmp = read.getScaledInstance(imgWidth, imgHeight, Image.SCALE_SMOOTH);
            BufferedImage img = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = img.createGraphics();
            g2d.drawImage(tmp, 0, 0, null);
            g2d.dispose();
            if (Math.abs(x1 - x2) > 0 && Math.abs(y2 - y1) > 0) {
                BufferedImage subimage = img.getSubimage(x1, y1, (Math.abs(x1 - x2)), Math.abs(y2 - y1));
                return getTextOfImage(subimage);
            }
        } catch (IOException ex) {
            Logger.getLogger(ArchivosEjb.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String getTextOfImage(BufferedImage recortarImagen) {
        Tesseract instance = new Tesseract(); // JNA Interface Mapping
        instance.setLanguage("spa");
        instance.setDatapath(JsfUti.getRealPath("tessdata"));
        try {
            String result = instance.doOCR(recortarImagen);
            return result;
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**
     *
     * @param rtf
     * @return
     * @throws IOException
     */
    @Override
    public String rtfToHtml(String rtf) throws IOException {
        JEditorPane p = new JEditorPane();
        p.setContentType("text/rtf");
        try {
            p.setText(rtf);
            EditorKit kitHtml = p.getEditorKitForContentType("text/html");
            String toString;
            try (Writer writer = new StringWriter()) {
                kitHtml.write(writer, p.getDocument(), 0, p.getDocument().getLength());
                toString = writer.toString();
                writer.flush();
            }
            return toString;
        } catch (BadLocationException e) {
            Logger.getLogger(ArchivosEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @Override
    public String getTextOfImage(BufferedImage read, int x1, int x2, int y1, int y2, int imgWidth, int imgHeight) {
        try {
            Image tmp = read.getScaledInstance(imgWidth, imgHeight, Image.SCALE_SMOOTH);
            BufferedImage img = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = img.createGraphics();
            g2d.drawImage(tmp, 0, 0, null);
            g2d.dispose();
            if (Math.abs(x1 - x2) > 0 && Math.abs(y2 - y1) > 0) {
                BufferedImage subimage = img.getSubimage(x1, y1, (Math.abs(x1 - x2)), Math.abs(y2 - y1));
                return getTextOfImage(subimage);
            }
        } catch (Exception ex) {
            Logger.getLogger(ArchivosEjb.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<Imagen> separarTiff(byte[] data, Long transanccion, Long movimiento) {
        List<RegMovimientoFile> rmfs = new ArrayList<>();
        List<Imagen> lista = new ArrayList<>();
        RegMovimientoFile rmf;
        Imagen imagen;
        Map map;
        try {
            map = new HashMap();
            map.put("movimiento", new RegMovimiento(movimiento));
            map.put("idTransaccion", transanccion);
            List<RegMovimientoFile> files = manager.findObjectByParameterOrderList(RegMovimientoFile.class, map, new String[]{"pagina"}, true);
            if (files.isEmpty()) {
                File is = File.createTempFile(UUID.randomUUID().toString(), ".tiff", new File(SisVars.rutaTemporales));
                try (FileOutputStream fos = new FileOutputStream(is)) {
                    fos.write(data);
                }
                Iterator readers = javax.imageio.ImageIO.getImageReadersBySuffix("tiff");
                /*if (readers.hasNext()) {
                    ImageInputStream iis = javax.imageio.ImageIO.createImageInputStream(is);
                    TIFFDecodeParam param = null;
                    ImageDecoder dec = ImageCodec.createImageDecoder("tiff", is, param);
                    // Get the page count of the TIFF image
                    int pageCount = dec.getNumPages();
                    ImageReader _imageReader = (ImageReader) (readers.next());
                    if (_imageReader != null) {
                        _imageReader.setInput(iis, true);
                        // Feed each page to the BarCodeReader
                        for (int i = 0; i < pageCount; i++) {
                            BufferedImage _bufferedImage = _imageReader.read(i);
                            File temp = File.createTempFile(UUID.randomUUID().toString(), ".jpg", 
                                    new File(SisVars.rutaTemporales));
                            ImageIO.write(_bufferedImage, "jpg", new FileOutputStream(temp));
                            imagen = new Imagen(transanccion, i + 1, temp.getAbsolutePath());
                            rmf = new RegMovimientoFile();
                            rmf.setMovimiento(new RegMovimiento(movimiento));
                            rmf.setNombreArchivo(imagen.getArchivo());
                            rmf.setPagina(imagen.getPagina());
                            rmf.setIdTransaccion(transanccion);
                            rmfs.add(rmf);
                            lista.add(imagen);      
                        }
                    }
                }*/
                if(!rmfs.isEmpty()){
                    manager.saveList(rmfs);
                }
            } else {
                for (RegMovimientoFile mf : files) {
                    imagen = new Imagen();
                    imagen.setArchivo(mf.getNombreArchivo());
                    imagen.setPagina(mf.getPagina());
                    imagen.setTransaccion(transanccion);
                    lista.add(imagen);
                }
            }
        } catch (IOException e) {
            Logger.getLogger(ArchivosEjb.class.getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
        return lista;
    }
    
    @Override
    public List<Imagen> separarTiffTramite(Long transanccion, Long tramite) {
        List<RegpDocsTramite> rdts = new ArrayList<>();
        List<Imagen> lista = new ArrayList<>();
        RegpDocsTramite rdt;
        Imagen imagen;
        Map map;
        try {
            map = new HashMap();
            map.put("numTramite", tramite);
            map.put("transaccion", transanccion);
            List<RegpDocsTramite> files = manager.findObjectByParameterOrderList(RegpDocsTramite.class, map, new String[]{"pagina"}, true);
            if (files.isEmpty()) {
                byte[] data = docs.find("SELECT imagen FROM lib_tramites where id_tran = ?", new Object[]{transanccion});
                File is = File.createTempFile(UUID.randomUUID().toString(), ".tiff", new File(SisVars.rutaTemporales));
                try (FileOutputStream fos = new FileOutputStream(is)) {
                    fos.write(data);
                }
                Iterator readers = javax.imageio.ImageIO.getImageReadersBySuffix("tiff");
                /*if (readers.hasNext()) {
                    ImageInputStream iis = javax.imageio.ImageIO.createImageInputStream(is);
                    TIFFDecodeParam param = null;
                    ImageDecoder dec = ImageCodec.createImageDecoder("tiff", is, param);
                    // Get the page count of the TIFF image
                    int pageCount = dec.getNumPages();
                    ImageReader _imageReader = (ImageReader) (readers.next());
                    if (_imageReader != null) {
                        _imageReader.setInput(iis, true);
                        // Feed each page to the BarCodeReader
                        for (int i = 0; i < pageCount; i++) {
                            BufferedImage _bufferedImage = _imageReader.read(i);
                            File temp = File.createTempFile(UUID.randomUUID().toString(), ".jpg", 
                                    new File(SisVars.rutaTemporales));
                            ImageIO.write(_bufferedImage, "jpg", new FileOutputStream(temp));
                            imagen = new Imagen(transanccion, i + 1, temp.getAbsolutePath());
                            rdt = new RegpDocsTramite();
                            rdt.setNumTramite(tramite);
                            rdt.setNombreArchivo(imagen.getArchivo());
                            rdt.setPagina(imagen.getPagina());
                            rdt.setTransaccion(transanccion);
                            rdts.add(rdt);
                            lista.add(imagen);      
                        }
                    }
                }*/
                if(!rdts.isEmpty()){
                    manager.saveList(rdts);
                }
            } else {
                for (RegpDocsTramite dt : files) {
                    imagen = new Imagen();
                    imagen.setArchivo(dt.getNombreArchivo());
                    imagen.setPagina(dt.getPagina());
                    imagen.setTransaccion(dt.getTransaccion());
                    lista.add(imagen);
                }
            }
        } catch (IOException e) {
            Logger.getLogger(ArchivosEjb.class.getName()).log(Level.SEVERE, null, e);
            return new ArrayList<>();
        }
        return lista;
    }

}
