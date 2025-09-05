/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.historico.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Origami
 */
@Entity
@Table(name = "cercontrato", schema = "historico")
public class Cercontrato implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "cer_id")
    private Long cerId;
    @Column(name = "cer_proforma")
    private Long cerProforma;
    @Column(name = "cer_fechacel")
    @Temporal(TemporalType.TIMESTAMP)
    private Date cerFechacel;
    @Column(name = "cer_observa")
    private String cerObserva;
    @Column(name = "cer_ventotal")
    private Integer cerVentatotal;
    @Column(name = "cer_mi")
    private Integer cerMi;
    @Column(name = "cer_codigocat")
    private String cerCodigocat;
    @Column(name = "cer_numero")
    private Integer cerNumero;
    /*@Column(name = "cer_fmi")
    @Temporal(TemporalType.TIMESTAMP)
    private Date cerFmi;
    @Column(name = "cer_ffin")
    @Temporal(TemporalType.TIMESTAMP)
    private Date cerFfin;
    @Column(name = "cer_sri")
    private Integer cerSri;
    @Column(name = "cer_compra")
    private String cerCompra;
    @Column(name = "cer_detalle")
    private String cerDetalle;
    @Column(name = "cer_co")
    private String cerCo;
    @Column(name = "cer_historia")
    private String cerHistoria;
    @Column(name = "cer_estado")
    private String cerEstado;
    @Column(name = "cer_estado2")
    private String cerEstado2;
    @Column(name = "cer_testamento")
    private String cerTestamento;
    @Column(name = "cer_resp")
    private String cerResp;
    @Column(name = "cer_estado3")
    private String cerEstado3;
    @Column(name = "cer_estado4")
    private String cerEstado4;
    @Column(name = "cer_estado5")
    private String cerEstado5;
    @Column(name = "cer_estado6")
    private String cerEstado6;
    @Column(name = "cer_estado7")
    private String cerEstado7;
    @Column(name = "cer_estado8")
    private String cerEstado8;
    @Column(name = "cer_estado9")
    private String cerEstado9;
    @Column(name = "cer_estado10")
    private String cerEstado10;
    @Column(name = "cer_fechaemi")
    private String cerFechaemi;
    @Column(name = "cer_margina")
    private String cerMargina;
    @Column(name = "cer_otorgante")
    private String cerOtorgante;
    @Column(name = "cer_estado11")
    private String cerEstado11;
    @Column(name = "cer_estado12")
    private String cerEstado12;
    @Column(name = "cer_estado13")
    private String cerEstado13;*/

    public Cercontrato() {
    }

    /*public void updateEstado() {
        if (cerEstado2 != null) {
            cerEstado = cerEstado.concat(cerEstado2);
        }
        if (cerEstado3 != null) {
            cerEstado = cerEstado.concat(cerEstado3);
        }
        if (cerEstado4 != null) {
            cerEstado = cerEstado.concat(cerEstado4);
        }
        if (cerEstado5 != null) {
            cerEstado = cerEstado.concat(cerEstado5);
        }
        if (cerEstado6 != null) {
            cerEstado = cerEstado.concat(cerEstado6);
        }
        if (cerEstado7 != null) {
            cerEstado = cerEstado.concat(cerEstado7);
        }
        if (cerEstado8 != null) {
            cerEstado = cerEstado.concat(cerEstado8);
        }
        if (cerEstado9 != null) {
            cerEstado = cerEstado.concat(cerEstado9);
        }
        if (cerEstado10 != null) {
            cerEstado = cerEstado.concat(cerEstado10);
        }
        if (cerEstado11 != null) {
            cerEstado = cerEstado.concat(cerEstado11);
        }
        if (cerEstado12 != null) {
            cerEstado = cerEstado.concat(cerEstado12);
        }
        if (cerEstado13 != null) {
            cerEstado = cerEstado.concat(cerEstado13);
        }
    }*/

    public Long getCerId() {
        return cerId;
    }

    public void setCerId(Long cerId) {
        this.cerId = cerId;
    }

    public Long getCerProforma() {
        return cerProforma;
    }

    public void setCerProforma(Long cerProforma) {
        this.cerProforma = cerProforma;
    }

    public Date getCerFechacel() {
        return cerFechacel;
    }

    public void setCerFechacel(Date cerFechacel) {
        this.cerFechacel = cerFechacel;
    }

    public String getCerObserva() {
        return cerObserva;
    }

    public void setCerObserva(String cerObserva) {
        this.cerObserva = cerObserva;
    }

    public Integer getCerVentatotal() {
        return cerVentatotal;
    }

    public void setCerVentatotal(Integer cerVentatotal) {
        this.cerVentatotal = cerVentatotal;
    }

    public Integer getCerMi() {
        return cerMi;
    }

    public void setCerMi(Integer cerMi) {
        this.cerMi = cerMi;
    }

    public String getCerCodigocat() {
        return cerCodigocat;
    }

    public void setCerCodigocat(String cerCodigocat) {
        this.cerCodigocat = cerCodigocat;
    }

    public Integer getCerNumero() {
        return cerNumero;
    }

    public void setCerNumero(Integer cerNumero) {
        this.cerNumero = cerNumero;
    }

    /*public Date getCerFmi() {
        return cerFmi;
    }

    public void setCerFmi(Date cerFmi) {
        this.cerFmi = cerFmi;
    }

    public Date getCerFfin() {
        return cerFfin;
    }

    public void setCerFfin(Date cerFfin) {
        this.cerFfin = cerFfin;
    }

    public Integer getCerSri() {
        return cerSri;
    }

    public void setCerSri(Integer cerSri) {
        this.cerSri = cerSri;
    }

    public String getCerCompra() {
        return cerCompra;
    }

    public void setCerCompra(String cerCompra) {
        this.cerCompra = cerCompra;
    }

    public String getCerDetalle() {
        return cerDetalle;
    }

    public void setCerDetalle(String cerDetalle) {
        this.cerDetalle = cerDetalle;
    }

    public String getCerCo() {
        return cerCo;
    }

    public void setCerCo(String cerCo) {
        this.cerCo = cerCo;
    }

    public String getCerHistoria() {
        return cerHistoria;
    }

    public void setCerHistoria(String cerHistoria) {
        this.cerHistoria = cerHistoria;
    }

    public String getCerEstado() {
        return cerEstado;
    }

    public void setCerEstado(String cerEstado) {
        this.cerEstado = cerEstado;
    }

    public String getCerEstado2() {
        return cerEstado2;
    }

    public void setCerEstado2(String cerEstado2) {
        this.cerEstado2 = cerEstado2;
    }

    public String getCerTestamento() {
        return cerTestamento;
    }

    public void setCerTestamento(String cerTestamento) {
        this.cerTestamento = cerTestamento;
    }

    public String getCerResp() {
        return cerResp;
    }

    public void setCerResp(String cerResp) {
        this.cerResp = cerResp;
    }

    public String getCerEstado3() {
        return cerEstado3;
    }

    public void setCerEstado3(String cerEstado3) {
        this.cerEstado3 = cerEstado3;
    }

    public String getCerEstado4() {
        return cerEstado4;
    }

    public void setCerEstado4(String cerEstado4) {
        this.cerEstado4 = cerEstado4;
    }

    public String getCerEstado5() {
        return cerEstado5;
    }

    public void setCerEstado5(String cerEstado5) {
        this.cerEstado5 = cerEstado5;
    }

    public String getCerEstado6() {
        return cerEstado6;
    }

    public void setCerEstado6(String cerEstado6) {
        this.cerEstado6 = cerEstado6;
    }

    public String getCerEstado7() {
        return cerEstado7;
    }

    public void setCerEstado7(String cerEstado7) {
        this.cerEstado7 = cerEstado7;
    }

    public String getCerEstado8() {
        return cerEstado8;
    }

    public void setCerEstado8(String cerEstado8) {
        this.cerEstado8 = cerEstado8;
    }

    public String getCerEstado9() {
        return cerEstado9;
    }

    public void setCerEstado9(String cerEstado9) {
        this.cerEstado9 = cerEstado9;
    }

    public String getCerEstado10() {
        return cerEstado10;
    }

    public void setCerEstado10(String cerEstado10) {
        this.cerEstado10 = cerEstado10;
    }

    public String getCerFechaemi() {
        return cerFechaemi;
    }

    public void setCerFechaemi(String cerFechaemi) {
        this.cerFechaemi = cerFechaemi;
    }

    public String getCerMargina() {
        return cerMargina;
    }

    public void setCerMargina(String cerMargina) {
        this.cerMargina = cerMargina;
    }

    public String getCerOtorgante() {
        return cerOtorgante;
    }

    public void setCerOtorgante(String cerOtorgante) {
        this.cerOtorgante = cerOtorgante;
    }

    public String getCerEstado11() {
        return cerEstado11;
    }

    public void setCerEstado11(String cerEstado11) {
        this.cerEstado11 = cerEstado11;
    }

    public String getCerEstado12() {
        return cerEstado12;
    }

    public void setCerEstado12(String cerEstado12) {
        this.cerEstado12 = cerEstado12;
    }

    public String getCerEstado13() {
        return cerEstado13;
    }

    public void setCerEstado13(String cerEstado13) {
        this.cerEstado13 = cerEstado13;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int hashCode() {
        return super.hashCode(); //To change body of generated methods, choose Tools | Templates.
    }*/

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.cerId);
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
        final Cercontrato other = (Cercontrato) obj;
        if (!Objects.equals(this.cerId, other.cerId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Cercontrato{cerId=").append(cerId);
        sb.append(", cerProforma=").append(cerProforma);
        sb.append(", cerFechacel=").append(cerFechacel);
        sb.append(", cerObserva=").append(cerObserva);
        sb.append(", cerVentatotal=").append(cerVentatotal);
        sb.append(", cerMi=").append(cerMi);
        sb.append(", cerCodigocat=").append(cerCodigocat);
        sb.append(", cerNumero=").append(cerNumero);
        sb.append('}');
        return sb.toString();
    }

}
