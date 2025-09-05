/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.util;

import java.io.File;
import java.io.FilenameFilter;

/**
 *
 * @author Anyelo
 */
public class FilterName implements FilenameFilter{

    protected String value;
    protected boolean start;

    public FilterName(String value, boolean start) {
        this.value = value;
        this.start = start;
    }
    
    @Override
    public boolean accept(File dir, String name) {
        if (start){
            return name.startsWith(value);
        } else {
            return name.endsWith(value);
        }
    }
    
}
