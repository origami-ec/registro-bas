/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.documental.models;

import java.io.Serializable;

/**
 *
 * @author Luis Pozo Gonzabay
 */
public class Data implements Serializable {

    private String data;
    private Long id;
    //se usa como Transient
    private String unicode;
    private String status;

    public Data() {
    }

    public Data(String data) {
        this.data = data;
    }

    public Data(String data, Long id) {
        this.data = data;
        this.id = id;
    }

    public Data(Long id, String unicode) {
        this.id = id;
        this.unicode = unicode;
    }

    public Data(String data, Long id, String unicode) {
        this.data = data;
        this.id = id;
        this.unicode = unicode;
    }

    public Data(Long id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUnicode() {
        return unicode;
    }

    public void setUnicode(String unicode) {
        this.unicode = unicode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
