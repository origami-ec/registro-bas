/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.models;

/**
 *
 * @author origami
 */
public enum ActividadesTransaccionales {
    GENERACION_FICHA            (1,"GENERACION DE FICHA"),                      // CREACION DE FICHA - DATOS DE FICHA Y ORDEN
    AGREGAR_REFERENCIA          (2,"AGREGAR REFERENCIA"),                        // AGREGAR MOVIMIENTO A FICHA - DATOS DE FICHA Y MOVIMIENTO
    ELIMINAR_REFERENCIA         (3,"ELIMINACION DE REFERENCIA"),                // ELIMINAR MOVIMIENTO DE FICHA - DATOS DE FICHA Y MOVIMIENTO
    DATOS_REGISTRALES           (4,"ACEPTAR DATOS REGISTRALES PREDIO/FOLIO"),   // MODIFICACION DE FICHA - GUARDA LINDEROS 
    IMPRESION_FICHA             (5,"IMPRESION DE FICHA REGISTRAL"),             // IMPRESION DE FICHA - DATOS DE FICHA y ORDEN
    ASOCIAR_FICHA_ORDEN         (6,"ASOCIAR FICHA A ORDEN"),                    // CREACION DE FICHA - DATOS DE FICHA Y ORDEN
    MODIFICAR_FICHA             (15,"MODIFICACION FICHA"),                      // MODIFICACION FICHA - DATOS DE FICHA    	
    ACTIVAR_FICHA               (7,"ACTIVAR FICHA"),                            // ACTIVAR PREDIO - DATOS DE FICHA    	
    INACTIVAR_FICHA             (8,"INACTIVAR FICHA - "),                       // INACTIVAR FICHA - DATOS DE FICHA
    ELIMINAR_FICHA              (9,"ELIMINAR FICHA"),                           // DATOS DE FICHA Y ORDEN
    AGREGAR_PROPIETARIO         (16,"AGREGAR PROPIETARIO"),                     // AGREGAR PROPIETARIO A FICHA REGISTRAL
    COMPLETAR_FOLIOS            (10,"COMPLETAR FOLIOS"),                        // ??? - DATOS DEL MOVIMIENTO y ORDEN
    ANULACION_INSCRIPCION       (11,"ANULACION DE INSCRIPCION"),                // ??? - DATOS DEL MOVIMIENTO
    INGRESO_INSCRIPCION         (12,"INGRESO DE INSCRIPCION"),                  // ??? - DATOS DEL MOVIMIENTO
    REALIZACION_INSCRIPCION     (13,"REALIZACION INSRIPCION"),                  // ??? - DATOS DEL MOVIMIENTO - FICHA Y ORDEN
    MODIFICACION_INSCRIPCION    (14,"MODIFICACION INSCRIPCION"),
    ELIMINAR_PROPIETARIO        (17,"ELIMINAR PROPIETARIO"),
    AGREGAR_INTERVINIENTE       (18,"AGREGAR INTERVINIENTE"),
    ELIMINAR_INTERVINIENTE      (19,"ELIMINAR INTERVINIENTE"),
    AGREGAR_FICHA               (20,"ASOCIAR FICHA"),
    AGREGAR_MARGINACION         (21,"AGREGAR MARGINACION"),
    APROBAR_EDICION_CERTIFICADO (22,"APROBAR EDICION CERTIFICADO"),
    MODIFICACION_CERTIFICADO    (23,"CERTIFICADO MODIFICADO O ACTUALIZADO"),
    MODIFICAR_INTERVINIENTE     (24,"MODIFICACION INTERVINIENTE");

    private final int codigo;
    private final String descripcion;

    private ActividadesTransaccionales(int codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }
    
    
}
