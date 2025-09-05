/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.util;

/**
 *
 * @author ANGEL NAVARRO
 */
public class WhereClause {

    private Object value;
    private Object value1;
    private String fieldOr;
    private String condition = "equal";
    private String funtion;

    public WhereClause() {
    }

    public WhereClause(Object value, String condition) {
        this.value = value;
        this.condition = condition;
    }

    public WhereClause(Object value, String fieldOr, Object value1, String condition) {
        this.value = value;
        this.value1 = value1;
        this.condition = condition;
        this.fieldOr = fieldOr;
    }

    public WhereClause(Object value, String condition, String funtion) {
        this.value = value;
        this.condition = condition;
        this.funtion = funtion;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getValue1() {
        return value1;
    }

    public void setValue1(Object value1) {
        this.value1 = value1;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getFuntion() {
        return funtion;
    }

    public void setFuntion(String funtion) {
        this.funtion = funtion;
    }

    public String getFieldOr() {
        return fieldOr;
    }

    public void setFieldOr(String fieldOr) {
        this.fieldOr = fieldOr;
    }

    @Override
    public String toString() {
        return "WhereClause{" + "value=" + value + ", condition=" + condition + ", funtion=" + funtion + '}';
    }

}
