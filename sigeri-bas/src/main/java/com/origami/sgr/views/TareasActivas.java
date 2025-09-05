/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.sgr.views;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.Formula;

/**
 *
 * @author eduar
 */
@Entity
@Table(name = "tareas_activas", schema = "flow")
public class TareasActivas implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "num_tramite")
    private Long numTramite;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Column(name = "fecha_entrega")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEntrega;
    @Column(name = "nombre_propietario")
    private String nombrePropietario;
    @Column(name = "id_proceso_temp")
    private String idProcesoTemp;
    @Column(name = "entregado")
    private Boolean entregado;
    @Column(name = "blocked")
    private Boolean blocked;
    @Column(name = "carpeta_rep")
    private String carpetaRep;
    @Column(name = "id_tipo_tramite")
    private Long idTipoTramite;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "task_id")
    private String taskId;
    @Column(name = "proc_inst_id")
    private String procInstId;
    @Column(name = "task_def_key")
    private String taskDefKey;
    @Column(name = "name_")
    private String name;
    @Column(name = "assignee")
    private String assignee;
    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @Column(name = "form_key")
    private String formKey;
    @Column(name = "priority")
    private Integer priority;
    @Column(name = "candidate")
    private String candidate;
    @Formula(value = "(select string_agg(ac.nombre, '\r\n') from flow.regp_liquidacion_detalles de "
            + "left join flow.regp_liquidacion li on li.id = de.liquidacion "
            + "left join app.reg_acto ac on ac.id = de.acto "
            + "where li.num_tramite_rp = {alias}.num_tramite)")
    private String contratos;
    @Formula(value = "(select max(nd.fecha_reingreso) from flow.regp_nota_devolutiva nd "
            + "left join flow.historico_tramites ht on ht.id = nd.tramite "
            + "where ht.num_tramite = {alias}.num_tramite)")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaReingreso;

    public TareasActivas() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(Long numTramite) {
        this.numTramite = numTramite;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getNombrePropietario() {
        return nombrePropietario;
    }

    public void setNombrePropietario(String nombrePropietario) {
        this.nombrePropietario = nombrePropietario;
    }

    public String getIdProcesoTemp() {
        return idProcesoTemp;
    }

    public void setIdProcesoTemp(String idProcesoTemp) {
        this.idProcesoTemp = idProcesoTemp;
    }

    public Boolean getEntregado() {
        return entregado;
    }

    public void setEntregado(Boolean entregado) {
        this.entregado = entregado;
    }

    public Boolean getBlocked() {
        return blocked;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    public String getCarpetaRep() {
        return carpetaRep;
    }

    public void setCarpetaRep(String carpetaRep) {
        this.carpetaRep = carpetaRep;
    }

    public Long getIdTipoTramite() {
        return idTipoTramite;
    }

    public void setIdTipoTramite(Long idTipoTramite) {
        this.idTipoTramite = idTipoTramite;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getFormKey() {
        return formKey;
    }

    public void setFormKey(String formKey) {
        this.formKey = formKey;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getCandidate() {
        return candidate;
    }

    public void setCandidate(String candidate) {
        this.candidate = candidate;
    }

    public String getContratos() {
        return contratos;
    }

    public void setContratos(String contratos) {
        this.contratos = contratos;
    }

    public Date getFechaReingreso() {
        return fechaReingreso;
    }

    public void setFechaReingreso(Date fechaReingreso) {
        this.fechaReingreso = fechaReingreso;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TareasActivas other = (TareasActivas) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TareasActivas{");
        sb.append("id=").append(id);
        sb.append(", numTramite=").append(numTramite);
        sb.append(", fechaIngreso=").append(fechaIngreso);
        sb.append(", fechaEntrega=").append(fechaEntrega);
        sb.append(", nombrePropietario=").append(nombrePropietario);
        sb.append(", idProcesoTemp=").append(idProcesoTemp);
        sb.append(", entregado=").append(entregado);
        sb.append(", blocked=").append(blocked);
        sb.append(", carpetaRep=").append(carpetaRep);
        sb.append(", idTipoTramite=").append(idTipoTramite);
        sb.append(", descripcion=").append(descripcion);
        sb.append(", taskId=").append(taskId);
        sb.append(", procInstId=").append(procInstId);
        sb.append(", taskDefKey=").append(taskDefKey);
        sb.append(", name=").append(name);
        sb.append(", assignee=").append(assignee);
        sb.append(", createTime=").append(createTime);
        sb.append(", formKey=").append(formKey);
        sb.append(", priority=").append(priority);
        sb.append(", candidate=").append(candidate);
        sb.append(", fechaReingreso=").append(fechaReingreso);
        sb.append('}');
        return sb.toString();
    }

}
