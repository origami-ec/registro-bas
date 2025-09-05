/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.services.ejbs;

import com.origami.sgr.entities.HistoricoTramites;
import com.origami.sgr.entities.RegpDocsTarea;
import com.origami.sgr.entities.RegpDocsTramite;
import com.origami.sgr.entities.RegpRespuestaJudicial;
import com.origami.sgr.entities.RegpTareasDinardap;
import com.origami.sgr.entities.RegpTareasDinardapDocs;
import com.origami.sgr.entities.RegpTareasTramite;
import com.origami.sgr.services.interfaces.DocumentsManagedLocal;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.servlets.OmegaUploader;
import com.origami.sgr.util.Querys;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author Anyelo
 */
@Stateless(name = "documentsManaged")
@Interceptors(value = {HibernateEjbInterceptor.class})
public class DocumentsManagedEjb implements DocumentsManagedLocal {

    @Inject
    private OmegaUploader ou;

    @EJB(beanName = "manager")
    private Entitymanager em;

    /**
     * guarda en la base de datos documental el archivo que se carga en la tarea
     * de digitalizacion inicial
     *
     * @param up Object
     * @param ht Object
     * @param user Object
     * @return Object
     */
    @Override
    public Boolean saveDocumentoHabilitante(UploadedFile up, HistoricoTramites ht, Long user) {
        RegpDocsTramite rdt;
        try {
            Long oid = ou.upFileDocument(up.getInputStream(), up.getFileName(), up.getContentType());
            if (oid == null) {
                return false;
            } else {
                //INHABILITA LOS DOCUMENTOS ANTERIORES
                em.updateNativeQuery(Querys.updateDocsEnables, new Object[]{ht.getId()});
                //CREA EL NUEVO REGISTRO PARA EL DOCUMENTO DE REEMPLAZO
                rdt = new RegpDocsTramite();
                rdt.setContentType(up.getContentType());
                rdt.setDoc(oid);
                rdt.setEstado(Boolean.TRUE);
                rdt.setFecha(new Date());
                rdt.setNombreArchivo(up.getFileName());
                rdt.setTramite(ht);
                rdt.setUsuario(user);
                rdt = (RegpDocsTramite) em.persist(rdt);
                if (rdt == null) {
                    return false;
                } else {
                    ht.setDocumento(Boolean.TRUE);
                    em.update(ht);
                    return true;
                }
            }
        } catch (Exception e) {
            Logger.getLogger(DocumentsManagedEjb.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

    @Override
    public Boolean saveDocumentoHabilitante(InputStream is, String nombre, String content, HistoricoTramites ht, Long user) {
        RegpDocsTramite rdt;
        try {
            Long oid = ou.upFileDocument(is, nombre, content);
            System.out.println("oid: " + oid);
            if (oid == null) {
                return false;
            } else {
                //INHABILITA LOS DOCUMENTOS ANTERIORES
                em.updateNativeQuery(Querys.updateDocsEnables, new Object[]{ht.getId()});
                //CREA EL NUEVO REGISTRO PARA EL DOCUMENTO DE REEMPLAZO
                rdt = new RegpDocsTramite();
                rdt.setContentType(content);
                rdt.setDoc(oid);
                rdt.setEstado(Boolean.TRUE);
                rdt.setFecha(new Date());
                rdt.setNombreArchivo(nombre);
                rdt.setTramite(ht);
                rdt.setNumTramite(ht.getNumTramite());
                rdt.setUsuario(user);
                rdt = (RegpDocsTramite) em.persist(rdt);
                if (rdt == null) {
                    return false;
                } else {
                    ht.setDocumento(Boolean.TRUE);
                    em.update(ht);
                    return true;
                }
            }
        } catch (Exception e) {
            Logger.getLogger(DocumentsManagedEjb.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

    /**
     * * guarda en la base de datos documental el archivo que se carga en la
     * tarea de digitalizacion final
     *
     * @param up Object
     * @param tt Object
     * @param user Object
     * @return Object
     */
    @Override
    public Boolean saveDocumentoTarea(UploadedFile up, RegpTareasTramite tt, Long user) {
        RegpDocsTarea rdt;
        try {
            Long oid = ou.uploadFile(up.getInputStream(), up.getFileName(), up.getContentType());
            if (oid == null) {
                return false;
            } else {
                //INHABILITA LOS DOCUMENTOS ANTERIORES
                em.updateNativeQuery(Querys.updateDocsTareas, new Object[]{tt.getId()});
                //CREA EL NUEVO REGISTRO PARA EL DOCUMENTO DE REEMPLAZO
                rdt = new RegpDocsTarea();
                rdt.setContentType(up.getContentType());
                rdt.setDoc(oid);
                rdt.setEstado(Boolean.TRUE);
                rdt.setFecha(new Date());
                rdt.setNombreArchivo(up.getFileName());
                rdt.setTarea(tt);
                rdt.setUsuario(user);
                rdt = (RegpDocsTarea) em.persist(rdt);
                if (rdt == null) {
                    return false;
                } else {
                    tt.setDocumento(Boolean.TRUE);
                    em.update(tt);
                    return true;
                }
            }
        } catch (Exception e) {
            Logger.getLogger(DocumentsManagedEjb.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

    /**
     * * guarda en la base de datos documental el archivo que se carga en la
     * tarea de contestacion judicial
     *
     * @param up Object
     * @param rdt Object
     * @return Object
     */
    @Override
    public Boolean saveDocRespuestaJudicial(UploadedFile up, RegpRespuestaJudicial rdt) {
        try {
            Long oid = ou.uploadFile(up.getInputStream(), up.getFileName(), up.getContentType());
            if (oid == null) {
                return false;
            } else {
                rdt.setContentType(up.getContentType());
                rdt.setDoc(oid);
                rdt.setEstado(Boolean.TRUE);
                rdt.setFecha(new Date());
                rdt.setNombreArchivo(up.getFileName());
                rdt = (RegpRespuestaJudicial) em.persist(rdt);
                if (rdt == null) {
                    return false;
                } else {
                    return true;
                }
            }
        } catch (Exception e) {
            Logger.getLogger(DocumentsManagedEjb.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

    @Override
    public Boolean saveDocsTareaDinardap(UploadedFile up, RegpTareasDinardap td, Long user) {
        RegpTareasDinardapDocs tdd;
        try {
            Long oid = ou.uploadFile(up.getInputStream(), up.getFileName(), up.getContentType());
            if (oid == null) {
                return false;
            } else {
                tdd = new RegpTareasDinardapDocs();
                tdd.setContentType(up.getContentType());
                tdd.setDoc(oid);
                tdd.setEstado(Boolean.TRUE);
                tdd.setFecha(new Date());
                tdd.setNombreArchivo(up.getFileName());
                tdd.setTareaDinardap(td);
                tdd.setUsuario(user);
                tdd = (RegpTareasDinardapDocs) em.persist(tdd);
                return tdd != null;
            }
        } catch (IOException e) {
            Logger.getLogger(DocumentsManagedEjb.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

}
