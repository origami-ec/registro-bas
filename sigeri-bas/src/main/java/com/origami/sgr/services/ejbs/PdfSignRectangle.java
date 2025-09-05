package com.origami.sgr.services.ejbs;

import java.io.Serializable;

public class PdfSignRectangle implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int llx;
	private int lly;
	private int urx;
	private int ury;
	
	public PdfSignRectangle() {
	}

	public PdfSignRectangle(int llx, int lly, int urx, int ury) {
		super();
		this.llx = llx;
		this.lly = lly;
		this.urx = urx;
		this.ury = ury;
	}

	public int getLlx() {
		return llx;
	}

	public void setLlx(int llx) {
		this.llx = llx;
	}

	public int getLly() {
		return lly;
	}

	public void setLly(int lly) {
		this.lly = lly;
	}

	public int getUrx() {
		return urx;
	}

	public void setUrx(int urx) {
		this.urx = urx;
	}

	public int getUry() {
		return ury;
	}

	public void setUry(int ury) {
		this.ury = ury;
	}
	
	
	
}
