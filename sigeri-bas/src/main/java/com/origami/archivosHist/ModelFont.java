/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.archivosHist;

/**
 *
 * @author ANGEL NAVARRO
 */
public class ModelFont {

    private String value;
    private String Height;
    private String Width;
    private String Escapement;
    private String Orientation;
    private String Weight;
    private String Italic;
    private String UnderLine;
    private String StrikeOut;
    private String CharSet;
    private String OutPrecision;
    private String ClipPrecision;
    private String Quality;
    private String PitchAndFamily;

    public ModelFont(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getHeight() {
        return Height;
    }

    public void setHeight(String Height) {
        this.Height = Height;
    }

    public String getWidth() {
        return Width;
    }

    public void setWidth(String Width) {
        this.Width = Width;
    }

    public String getEscapement() {
        return Escapement;
    }

    public void setEscapement(String Escapement) {
        this.Escapement = Escapement;
    }

    public String getOrientation() {
        return Orientation;
    }

    public void setOrientation(String Orientation) {
        this.Orientation = Orientation;
    }

    public String getWeight() {
        return Weight;
    }

    public void setWeight(String Weight) {
        this.Weight = Weight;
    }

    public String getItalic() {
        return Italic;
    }

    public void setItalic(String Italic) {
        this.Italic = Italic;
    }

    public String getUnderLine() {
        return UnderLine;
    }

    public void setUnderLine(String UnderLine) {
        this.UnderLine = UnderLine;
    }

    public String getStrikeOut() {
        return StrikeOut;
    }

    public void setStrikeOut(String StrikeOut) {
        this.StrikeOut = StrikeOut;
    }

    public String getCharSet() {
        return CharSet;
    }

    public void setCharSet(String CharSet) {
        this.CharSet = CharSet;
    }

    public String getOutPrecision() {
        return OutPrecision;
    }

    public void setOutPrecision(String OutPrecision) {
        this.OutPrecision = OutPrecision;
    }

    public String getClipPrecision() {
        return ClipPrecision;
    }

    public void setClipPrecision(String ClipPrecision) {
        this.ClipPrecision = ClipPrecision;
    }

    public String getQuality() {
        return Quality;
    }

    public void setQuality(String Quality) {
        this.Quality = Quality;
    }

    public String getPitchAndFamily() {
        return PitchAndFamily;
    }

    public void setPitchAndFamily(String PitchAndFamily) {
        this.PitchAndFamily = PitchAndFamily;
    }

    @Override
    public String toString() {
        return "ModelFont{" + "value=" + value + ", Height=" + Height + ", Width=" + Width + ", Escapement=" + Escapement + ", Orientation=" + Orientation + ", Weight=" + Weight + ", Italic=" + Italic + ", UnderLine=" + UnderLine + ", StrikeOut=" + StrikeOut + ", CharSet=" + CharSet + ", OutPrecision=" + OutPrecision + ", ClipPrecision=" + ClipPrecision + ", Quality=" + Quality + ", PitchAndFamily=" + PitchAndFamily + '}';
    }

    public Integer getHeightFont() {
        return Math.abs(Integer.valueOf(getHeight()));
    }
}
