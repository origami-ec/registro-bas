/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.sgr.managedbeans;

import com.origami.session.UserSession;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.DepartamentoUsuario;
import com.origami.sgr.entities.GeDepartamento;
import com.origami.sgr.entities.RegFicha;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author ORIGAMI
 */
@Named
@ViewScoped
public class AsignarResponsableMB implements Serializable {

    @Inject
    private Entitymanager em;
    @Inject
    private UserSession us;
    private List<GeDepartamento> departamentos;

    private GeDepartamento departamento;

    private List<AclUser> usuarios;

    @PostConstruct
    public void initView() {
        if (!JsfUti.isAjaxRequest()) {
            departamentos = em.findAllEntCopy(Querys.getGeDepartamentos);
            departamento = new GeDepartamento();
        }
    }

    public void initListUsuarios() {
        try {
            if (departamento != null) {
                usuarios = em.findAll(Querys.getAclUserByDepartamento, new String[]{"departamento"}, new Object[]{departamento.getId()});
                if (Utils.isNotEmpty(usuarios)) {
                    System.out.println("usuarios: " + usuarios.size());
                    for (AclUser u : usuarios) {
                        DepartamentoUsuario depUser = (DepartamentoUsuario) em.find(Querys.getResponsableDepartamento, new String[]{"usuario"}, new Object[]{u.getId()});
                        if (depUser != null) {
                            u.setAsistente(depUser.getAsistente());
                            u.setJefe(depUser.getJefe());
                            u.setDirector(depUser.getDirector());
                            u.setLiquidador(depUser.getLiquidador());
                        }
                    }
                } else {
                    JsfUti.messageError(null, "No existen usuarios registrados en el departamento: " + departamento.getNombre(), "");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void editResponsable(AclUser usuario) {
        if (usuario != null) {
            DepartamentoUsuario depUser = (DepartamentoUsuario) em.find(Querys.getResponsableDepartamento, new String[]{"usuario"}, new Object[]{usuario.getId()});
            if (depUser == null) {
                depUser = new DepartamentoUsuario();
                depUser.setDepartamento(departamento);
                depUser.setUsuario(usuario);
                depUser.setFechaCreacion(new Date());
                depUser.setUsuarioCreacion(us.getName_user());
            }
            if (usuario.getAsistente() == null) {
                usuario.setAsistente(Boolean.FALSE);
            }
            
            if (usuario.getJefe() == null) {
                usuario.setJefe(Boolean.FALSE);
            }
            
            if (usuario.getDirector() == null) {
                usuario.setDirector(Boolean.FALSE);
            }
            
            if (usuario.getLiquidador() == null) {
                usuario.setLiquidador(Boolean.FALSE);
            }
            
            depUser.setAsistente(usuario.getAsistente());
            depUser.setJefe(usuario.getJefe());
            depUser.setDirector(usuario.getDirector());
            depUser.setLiquidador(usuario.getLiquidador());
            em.saveAll(depUser);

            JsfUti.messageInfo(null, "Datos actualizados", "");
            JsfUti.update("mainForm");
        }
    }

    public List<GeDepartamento> getDepartamentos() {
        return departamentos;
    }

    public void setDepartamentos(List<GeDepartamento> departamentos) {
        this.departamentos = departamentos;
    }

    public GeDepartamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(GeDepartamento departamento) {
        this.departamento = departamento;
    }

    public List<AclUser> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<AclUser> usuarios) {
        this.usuarios = usuarios;
    }

}
