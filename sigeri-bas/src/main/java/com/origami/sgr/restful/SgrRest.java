/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.restful;

import com.google.gson.Gson;
import com.origami.sgr.bpm.managedbeans.BpmManageBeanBaseRoot;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.CtlgItem;
import com.origami.sgr.entities.DocumentoFirma;
import com.origami.sgr.entities.RegCertificado;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.entities.RegpNotaDevolutiva;
import com.origami.sgr.entities.SolicitudServicios;
import com.origami.sgr.restful.models.UsuarioRest;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.services.interfaces.IngresoTramiteLocal;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.services.interfaces.VentanillaPubLocal;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author EDWIN
 */
@Path(value = "api/")
@Produces({"application/json"})
public class SgrRest extends BpmManageBeanBaseRoot implements Serializable {

    @Inject
    private IngresoTramiteLocal itl;
    @Inject
    private RegistroPropiedadServices rps;
    @Inject
    private Entitymanager em;
    @Inject
    protected VentanillaPubLocal vp;
    private static final Logger LOG = Logger.getLogger(SgrRest.class.getName());
    private static final long serialVersionUID = 1L;

    @POST
    @Path(value = "/iniciarSesion")
    @Consumes(MediaType.APPLICATION_JSON)
    public UsuarioRest iniciarSesion(String data) {
        Gson gson = new Gson();
        UsuarioRest rest = gson.fromJson(data, UsuarioRest.class);
        AclUser u = (AclUser) em.find(Querys.getUsuariobyUserClave, new String[]{"user", "clave"}, new Object[]{rest.getUsuario(), rest.getClave()});
        if (u != null) {
            rest.setMensaje("OK");
        } else {
            rest.setMensaje("Usuario o contraseña incorrecto");
        }
        return rest;
    }

    @POST
    @Path(value = "/actualizarDocumento")
    @Consumes(MediaType.APPLICATION_JSON)
    public DocumentoFirma actualizarDocumento(String data) {
        Gson gson = new Gson();
        CtlgItem estado = null;
        DocumentoFirma rest = gson.fromJson(data, DocumentoFirma.class);
        DocumentoFirma bd = manager.find(DocumentoFirma.class, rest.getId());
        if (bd != null && bd.getTipo() != null) {
            if (bd.getEstado().getCodename().equals("firma_pendiente")) {
                String nameTask = rps.getNameTaskFromNumTramite(bd.getNumTramite());
                if (Utils.isNotEmptyString(nameTask)) {
                    nameTask = nameTask.toLowerCase();
                }
                //Boolean finalizaTarea = Boolean.FALSE;
                Long oid = null;
                SolicitudServicios sb = manager.find(SolicitudServicios.class, bd.getReferencia());
                oid = rps.guardarArchivo(Base64.getDecoder().decode(rest.getArchivoFirmado()));
                sb.setDocumento(oid);
                map = new HashMap();
               // rps.finalizarTareaFirmaFuncionario(bd);//CONTINUAR TAREA

                manager.persist(sb);
                rest.setDocumento(oid);
                map = new HashMap();
                map.put("codename", "firma_realizada");
                estado = (CtlgItem) manager.findObjectByParameter(CtlgItem.class, map);
                bd.setEstado(estado);
                bd.setDocumento(oid);
                bd.setFechaFirma(new Date());
                manager.persist(bd);

            }
        }
        return rest;
    }

    @GET
    @Path("/documentosPendienteFirma/{usuario}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DocumentoFirma> firmarDocumento(@PathParam("usuario") String usuario) {
        try {
            map = new HashMap();
            map.put("codename", "firma_pendiente");
            CtlgItem ci = (CtlgItem) manager.findObjectByParameter(CtlgItem.class,
                    map);

            List<DocumentoFirma> documentos = manager.findAll(Querys.getDocumentosPendienteFirma, new String[]{"estado", "usuario"}, new Object[]{ci.getId(), usuario});
            if (Utils.isNotEmpty(documentos)) {
                DocumentoFirma fd = documentos.get(0);
                List<DocumentoFirma> documentosFirma = manager.findAll(Querys.getDocumentosUsuarioEstadoTramiteFirma, new String[]{"estado", "usuario", "numTramite"}, new Object[]{ci.getId(), fd.getUsuario(), fd.getNumTramite()});
                return documentosFirma;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @GET
    @Path("/generarPDF/{oid}")
    @Produces("application/pdf")
    public byte[] generarDocumentoPdf(@PathParam("oid") String oid) {
        System.out.println("oid: " + oid);
        byte[] pdfContents = null;
        if (oid != null) {
            File f = vp.generarPDF(Long.valueOf(oid));

            if (f != null) {
                try {
                    pdfContents = Files.readAllBytes(f.toPath());
                    f.delete();
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        }
        return pdfContents;
    }

}
