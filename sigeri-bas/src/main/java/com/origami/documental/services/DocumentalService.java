/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.documental.services;

import com.origami.documental.models.ArchivoDocs;
import com.origami.documental.models.ArchivoIndexDto;
import com.origami.documental.models.Color;
import com.origami.documental.models.Data;
import com.origami.documental.models.Documentos;
import com.origami.documental.models.Formato;
import com.origami.documental.models.Indexacion;
import com.origami.documental.models.IndexacionCampo;
import com.origami.documental.models.Nota;
import com.origami.documental.models.Tesauro;
import com.origami.documental.models.TipoDato;
import java.io.File;
import java.util.List;
import javax.ejb.Local;
import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author eduar
 */
@Local
public interface DocumentalService {
    
    public List<ArchivoDocs> buscarArchivos(String indexacion, String valor);
    
    public ArchivoDocs guardarArchivo(UploadedFile archivo, ArchivoIndexDto archivoIndex);

    public ArchivoDocs guardarArchivo(String contentType, String nameFile, byte[] content, ArchivoIndexDto archivoIndex);

    public ArchivoDocs guardarArchivo(File archivo, ArchivoIndexDto archivoIndex);

    public ArchivoDocs consultarArchivo(ArchivoDocs archivo);
    
    public ArchivoDocs visualizarArchivo(ArchivoDocs archivo);

    public ArchivoDocs convertirArchivo(ArchivoDocs archivo);

    public ArchivoDocs eliminarArchivo(ArchivoDocs archivo);

    /**
     *
     * @param archivo recibe un dto lleno pro solo se usan los campos de la
     * descripcion del documento y los campos de la indeacion porque para eso es
     * este metodo
     * @return el mismo dto xd
     */
    public ArchivoDocs actualizarDatosIndex(ArchivoDocs archivo);

    /**
     *
     * @param archivo Recibe un archivo para poder generar el nuevo archivo con
     * las notas
     * @return
     */
    public ArchivoDocs imprimirNotas(ArchivoDocs archivo);

    /**
     *
     * @param index son los campos de busqueda mediante indices
     * @return retorna una lista de archivos
     */
    public List<ArchivoDocs> busquedaAvanzada(ArchivoIndexDto index);

    /**
     *
     * @param archivoId es el archivo de la base en mongo
     * @param nota la nota que se esta creando es una lista que se agrega a las
     * imagenes
     * @param indexImagen es la posicion de la imagen en la lista del archivo
     * para aregarle la nota
     * @return el nuevo archivo docs devuelve 0 Si todo esta correcto y en el la
     * variable data envia el json de ArchivoDocs y envia 1 si hubo un error
     */
    public Data guardarNota(String archivoId, Nota nota, Integer indexImagen);

    public Data eliminarNota(String archivoId, Nota nota, Integer indexImagen, Integer indexNota);

    /**
     *
     * @return listado de colores predeterminados
     */
    public List<Color> getColores();

    /**
     *
     * @return listado de formatos predeterminados
     */
    public List<Formato> getFormatos();

    /**
     *
     * @param formato formato con los campos llenos
     * @return retorna el formato con el ID creado ---- null en caso de error
     */
    public Formato guardarFormato(Formato formato);

    /**
     *
     * @return retorna el listado de los tipos de datos
     */
    public List<TipoDato> getTipoDatos();

    /**
     *
     * @return retorna el listado de los indices predeterminados como los campos
     * descripcion fecha y usuario
     */
    public List<IndexacionCampo> getIndicesPredeterminados();

    /**
     *
     * @return retorna el listado de los indices registrados
     */
    public List<Indexacion> getIndices();
    
    public Indexacion getIndiceByDescripcion(String descripcion);

    /**
     *
     * @param indexacion los indices con sus campos
     * @return retorna el indice con el ID creado ---- null en caso de error
     */
    public Indexacion guardarIndexacion(Indexacion indexacion);

    /**
     *
     * @param indexacion solo tiene el campo de descripcion para validar si
     * existe o no
     * @return retorna el indice con el ID creado ---- null en caso de error
     */
    public Indexacion validarIndexacionFormulario(Indexacion indexacion);

    /**
     *
     * @return listado de colores sellos ingresados en indexacion
     */
    public List<Data> getSellos();

    public List<Documentos> getDocumentosScan();

    public Documentos guardarDocumento(Documentos documentos);

    public Tesauro validarIndexacionTesauro(Tesauro data);

    public Tesauro guardarTesauro(Tesauro data);

    public List<Tesauro> getTesauros();
    
}
