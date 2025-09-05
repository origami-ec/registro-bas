/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.sgr.util;

/**
 *
 * @author eduar
 */
public enum NroTomoEnum {

    VACIO(0),
    LV(2020),
    LVI(2021),
    LVII(2022),
    LVIII(2023),
    LIX(2024),
    LX(2025),
    LXI(2026),
    LXII(2027),
    LXIII(2028),
    LXIV(2029),
    LXV(2030),
    LXVI(2031),
    LXVII(2032),
    LXVIII(2033),
    LXIX(2034),
    LXX(2035),
    LXXI(2036),
    LXXII(2037),
    LXXIII(2038),
    LXXIV(2039),
    LXXV(2040);

    private final Integer uiParam;

    NroTomoEnum(Integer uiParam) {
        this.uiParam = uiParam;
    }

    public static NroTomoEnum fromUiParam(Integer uiParam) {
        try {
            for (NroTomoEnum temp : values()) {
                if (temp.uiParam.equals(uiParam)) {
                    return temp;
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("No MatchMode found for " + uiParam);
        }
        return NroTomoEnum.VACIO;
    }

}
