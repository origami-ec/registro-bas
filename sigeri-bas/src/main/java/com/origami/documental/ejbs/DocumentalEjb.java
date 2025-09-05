/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.documental.ejbs;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.origami.config.SisVars;
import com.origami.documental.models.ArchivoDocs;
import com.origami.documental.models.ArchivoIndexCampoDto;
import com.origami.documental.models.ArchivoIndexDto;
import com.origami.documental.models.Color;
import com.origami.documental.models.Data;
import com.origami.documental.models.Documentos;
import com.origami.documental.models.Formato;
import com.origami.documental.models.ImagenNota;
import com.origami.documental.models.Indexacion;
import com.origami.documental.models.IndexacionCampo;
import com.origami.documental.models.Nota;
import com.origami.documental.models.Tesauro;
import com.origami.documental.models.TipoDato;
import com.origami.documental.services.DocumentalService;
import com.origami.documental.services.OrigamiGTService;
import com.origami.session.UserSession;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author eduar
 */
@Stateless
public class DocumentalEjb implements DocumentalService {

    @Inject
    private OrigamiGTService service;

    @Inject
    private UserSession us;

    @Override
    public List<ArchivoDocs> buscarArchivos(String indexacion, String detalle) {
        List<ArchivoIndexCampoDto> detalles = new ArrayList<>();
        try {
            ArchivoIndexDto archivoIndex = new ArchivoIndexDto();
            archivoIndex.setEstado(Boolean.TRUE);
            archivoIndex.setTipoIndexacion(indexacion);
            detalles.add(new ArchivoIndexCampoDto(detalle));
            archivoIndex.setDetalles(detalles);

            Gson gson = new Gson();
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(SisVars.urlOrigamiDocs + "archivo/busquedaAvanzada");
            httpPost.setEntity(new StringEntity(gson.toJson(archivoIndex), "UTF-8"));
            httpPost.setHeader("Content-type", "application/json; charset=utf-8");
            HttpResponse httpresponse = httpClient.execute(httpPost);
            StringBuilder response;
            try (BufferedReader in = new BufferedReader(new InputStreamReader(httpresponse.getEntity().getContent(), StandardCharsets.UTF_8))) {
                String inputLine;
                response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
            }
            Type collectionType = new TypeToken<List<ArchivoDocs>>() {
            }.getType();
            return (List<ArchivoDocs>) new Gson().fromJson(response.toString(), collectionType);
        } catch (JsonSyntaxException | IOException | UnsupportedCharsetException e) {
            System.out.println(e);
            return new ArrayList<>();
        }
    }

    @Override
    public ArchivoDocs guardarArchivo(UploadedFile archivo, ArchivoIndexDto archivoIndex) {
        return (ArchivoDocs) service.methodUploadFile(us.getUsuarioDocs(), archivoIndex,
                archivo.getContentType(), archivo.getFileName(), archivo.getContent(),
                SisVars.urlOrigamiDocs + "archivo/cargarDocumento", ArchivoDocs.class);
    }

    @Override
    public ArchivoDocs guardarArchivo(String contentType, String nameFile, byte[] content,
            ArchivoIndexDto archivoIndex) {
        return (ArchivoDocs) service.methodUploadFile(us.getUsuarioDocs(), archivoIndex,
                contentType, nameFile, content, SisVars.urlOrigamiDocs + "archivo/cargarDocumento", ArchivoDocs.class);
    }

    @Override
    public ArchivoDocs guardarArchivo(File archivo, ArchivoIndexDto archivoIndex) {
        String contentType;
        byte[] bytes = null;
        try {
            contentType = Files.probeContentType(archivo.toPath());
            bytes = FileUtils.readFileToByteArray(archivo);
        } catch (IOException e) {
            contentType = "";
            System.out.println(e);
        }
        if (bytes != null) {
            return (ArchivoDocs) service.methodUploadFile(us.getUsuarioDocs(), archivoIndex, contentType,
                    archivo.getName(), bytes, SisVars.urlOrigamiDocs + "archivo/cargarDocumento", ArchivoDocs.class);
        } else {
            return null;
        }
    }

    @Override
    public ArchivoDocs actualizarDatosIndex(ArchivoDocs archivo) {
        return (ArchivoDocs) service.methodPOST(archivo, SisVars.urlOrigamiDocs
                + "archivo/actualizarDatosIndex", ArchivoDocs.class);
    }

    @Override
    public ArchivoDocs imprimirNotas(ArchivoDocs archivo) {
        return (ArchivoDocs) service.methodPOST(archivo, SisVars.urlOrigamiDocs
                + "archivo/imprimirNotas", ArchivoDocs.class);
    }

    @Override
    public List<ArchivoDocs> busquedaAvanzada(ArchivoIndexDto index) {
        return service.methodListPOST(index, SisVars.urlOrigamiDocs
                + "archivo/busquedaAvanzada", ArchivoDocs[].class);
    }

    @Override
    public Data guardarNota(String archivoId, Nota nota, Integer indexImagen) {
        ImagenNota imagenNota = new ImagenNota(archivoId, SisVars.notaAgregar, nota);
        imagenNota.setIndiceImagen(indexImagen);
        return (Data) service.methodPOST(imagenNota, SisVars.urlOrigamiDocs + "archivo/agregarNota", Data.class);
    }

    @Override
    public Data eliminarNota(String archivoId, Nota nota, Integer indexImagen, Integer indexNota) {
        ImagenNota imagenNota = new ImagenNota(archivoId, SisVars.notaEliminar, nota);
        imagenNota.setIndiceImagen(indexImagen);
        imagenNota.setIndiceNota(indexNota);
        return (Data) service.methodPOST(imagenNota, SisVars.urlOrigamiDocs + "archivo/agregarNota", Data.class);
    }

    @Override
    public ArchivoDocs consultarArchivo(ArchivoDocs archivo) {
        return (ArchivoDocs) service.methodPOST(archivo, SisVars.urlOrigamiDocs + "archivo/consultar", ArchivoDocs.class);
    }
    
    @Override
    public ArchivoDocs visualizarArchivo(ArchivoDocs archivo) {
        return (ArchivoDocs) service.methodPOST(archivo, SisVars.urlOrigamiDocs + "archivo/visualizar", ArchivoDocs.class);
    }

    @Override
    public ArchivoDocs convertirArchivo(ArchivoDocs archivo) {
        return (ArchivoDocs) service.methodPOST(archivo, SisVars.urlOrigamiDocs + "archivo/convertir", ArchivoDocs.class);
    }

    @Override
    public ArchivoDocs eliminarArchivo(ArchivoDocs archivo) {
        return (ArchivoDocs) service.methodPOST(archivo, SisVars.urlOrigamiDocs + "archivo/eliminar", ArchivoDocs.class);
    }

    @Override
    public List<Color> getColores() {
        return (List<Color>) service.methodListGET(SisVars.urlOrigamiDocs + "colores", Color[].class);
    }

    @Override
    public Formato guardarFormato(Formato formato) {
        return (Formato) service.methodPOST(formato, SisVars.urlOrigamiDocs + "formato/grabar", Formato.class);
    }

    @Override
    public List<Formato> getFormatos() {
        return (List<Formato>) service.methodListGET(SisVars.urlOrigamiDocs + "formatos", Formato[].class);
    }

    @Override
    public List<TipoDato> getTipoDatos() {
        return (List<TipoDato>) service.methodListGET(SisVars.urlOrigamiDocs + "tipoDatos", TipoDato[].class);
    }

    @Override
    public List<IndexacionCampo> getIndicesPredeterminados() {
        return (List<IndexacionCampo>) service.methodListGET(SisVars.urlOrigamiDocs + "indicePredeterminados",
                IndexacionCampo[].class);
    }

    @Override
    public List<Indexacion> getIndices() {
        return (List<Indexacion>) service.methodListGET(SisVars.urlOrigamiDocs + "indexacion/consultar", Indexacion[].class);
    }

    @Override
    public Indexacion getIndiceByDescripcion(String descripcion) {
        return (Indexacion) service.methodGET(SisVars.urlOrigamiDocs + "indexacion/descripcion/"
                + descripcion, Indexacion.class);
    }

    @Override
    public Indexacion guardarIndexacion(Indexacion indexacion) {
        return (Indexacion) service.methodPOST(indexacion, SisVars.urlOrigamiDocs + "indexacion/grabar", Indexacion.class);
    }

    @Override
    public Indexacion validarIndexacionFormulario(Indexacion indexacion) {
        return (Indexacion) service.methodPOST(indexacion, SisVars.urlOrigamiDocs + "indexacion/validar", Indexacion.class);
    }

    @Override
    public List<Data> getSellos() {
        return (List<Data>) service.methodListGET(SisVars.urlOrigamiDocs + "archivo/sellos", Data[].class);
    }

    @Override
    public List<Documentos> getDocumentosScan() {
        return (List<Documentos>) service.methodListGET(SisVars.urlOrigamiZuul + "documents/consultar/"
                + us.getName_user(), Documentos[].class);
    }

    @Override
    public Documentos guardarDocumento(Documentos documentos) {
        return (Documentos) service.methodPOST(documentos, SisVars.urlOrigamiZuul + "documents/grabar", Documentos.class);
    }

    @Override
    public Tesauro validarIndexacionTesauro(Tesauro data) {
        return (Tesauro) service.methodPOST(data, SisVars.urlOrigamiDocs + "tesauro/validar", Tesauro.class);
    }

    @Override
    public Tesauro guardarTesauro(Tesauro data) {
        return (Tesauro) service.methodPOST(data, SisVars.urlOrigamiDocs + "tesauro/grabar", Tesauro.class);
    }

    @Override
    public List<Tesauro> getTesauros() {
        return (List<Tesauro>) service.methodListGET(SisVars.urlOrigamiZuul + "tesauro/consultar", Tesauro[].class);
    }

}
