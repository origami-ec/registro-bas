package com.origami.sgr.util;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import com.origami.sgr.bpm.managedbeans.Rpp;

@ApplicationScoped
public class UtilsFactory {
	
	@Produces @Rpp
	public DateUtilRpp getDateUtil(){
		return new DateUtilRpp();
	}
	
	
	
}
