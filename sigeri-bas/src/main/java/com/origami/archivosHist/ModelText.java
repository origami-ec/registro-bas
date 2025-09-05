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
public class ModelText {

    private String value;
    private String PenColor;
    private String TextColor;
    private String BackColor;
    private String PenWidth;
    private String HighlightBack;
    private String BackStyle;
    private String Locked;
    private String AutoSize;
    private String Visible;
    private String ShowHandles;
    private String Moveable;
    private String TextOrientation;
    private String Selected;
    private String Sizeable;
    private String CreateTime;
    private String TextLocked;
    private String HatchStyle;
    private String HatchColor;
    private String TextJustification;
    private String UserLong;
    private String GroupNumber;
    private String Fixed;
    private String WangType;
    private ModelFont Font;
    private ModelPoint point;
    private ModelPoint point2;

    public ModelPoint getPoint() {
        return point;
    }

    public void setPoint(ModelPoint point) {
        this.point = point;
    }

    public ModelPoint getPoint2() {
        return point2;
    }

    public void setPoint2(ModelPoint point2) {
        this.point2 = point2;
    }

    public ModelText(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPenColor() {
        return PenColor;
    }

    public void setPenColor(String PenColor) {
        this.PenColor = PenColor;
    }

    public String getTextColor() {
        return TextColor;
    }

    public void setTextColor(String TextColor) {
        this.TextColor = TextColor;
    }

    public String getBackColor() {
        return BackColor;
    }

    public void setBackColor(String BackColor) {
        this.BackColor = BackColor;
    }

    public String getPenWidth() {
        return PenWidth;
    }

    public void setPenWidth(String PenWidth) {
        this.PenWidth = PenWidth;
    }

    public String getHighlightBack() {
        return HighlightBack;
    }

    public void setHighlightBack(String HighlightBack) {
        this.HighlightBack = HighlightBack;
    }

    public String getBackStyle() {
        return BackStyle;
    }

    public void setBackStyle(String BackStyle) {
        this.BackStyle = BackStyle;
    }

    public String getLocked() {
        return Locked;
    }

    public void setLocked(String Locked) {
        this.Locked = Locked;
    }

    public String getAutoSize() {
        return AutoSize;
    }

    public void setAutoSize(String AutoSize) {
        this.AutoSize = AutoSize;
    }

    public String getVisible() {
        return Visible;
    }

    public void setVisible(String Visible) {
        this.Visible = Visible;
    }

    public String getShowHandles() {
        return ShowHandles;
    }

    public void setShowHandles(String ShowHandles) {
        this.ShowHandles = ShowHandles;
    }

    public String getMoveable() {
        return Moveable;
    }

    public void setMoveable(String Moveable) {
        this.Moveable = Moveable;
    }

    public String getTextOrientation() {
        return TextOrientation;
    }

    public void setTextOrientation(String TextOrientation) {
        this.TextOrientation = TextOrientation;
    }

    public String getSelected() {
        return Selected;
    }

    public void setSelected(String Selected) {
        this.Selected = Selected;
    }

    public String getSizeable() {
        return Sizeable;
    }

    public void setSizeable(String Sizeable) {
        this.Sizeable = Sizeable;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }

    public String getTextLocked() {
        return TextLocked;
    }

    public void setTextLocked(String TextLocked) {
        this.TextLocked = TextLocked;
    }

    public String getHatchStyle() {
        return HatchStyle;
    }

    public void setHatchStyle(String HatchStyle) {
        this.HatchStyle = HatchStyle;
    }

    public String getHatchColor() {
        return HatchColor;
    }

    public void setHatchColor(String HatchColor) {
        this.HatchColor = HatchColor;
    }

    public String getTextJustification() {
        return TextJustification;
    }

    public void setTextJustification(String TextJustification) {
        this.TextJustification = TextJustification;
    }

    public String getUserLong() {
        return UserLong;
    }

    public void setUserLong(String UserLong) {
        this.UserLong = UserLong;
    }

    public String getGroupNumber() {
        return GroupNumber;
    }

    public void setGroupNumber(String GroupNumber) {
        this.GroupNumber = GroupNumber;
    }

    public String getFixed() {
        return Fixed;
    }

    public void setFixed(String Fixed) {
        this.Fixed = Fixed;
    }

    public String getWangType() {
        return WangType;
    }

    public void setWangType(String WangType) {
        this.WangType = WangType;
    }

    public ModelFont getFont() {
        return Font;
    }

    public void setFont(ModelFont Font) {
        this.Font = Font;
    }

    @Override
    public String toString() {
        return "ModelText{" + "value=" + value + ", PenColor=" + PenColor + ", TextColor=" + TextColor + ", BackColor=" + BackColor
                + ", PenWidth=" + PenWidth + ", HighlightBack=" + HighlightBack + ", BackStyle=" + BackStyle + ", Locked=" + Locked
                + ", AutoSize=" + AutoSize + ", Visible=" + Visible + ", ShowHandles=" + ShowHandles + ", Moveable=" + Moveable
                + ", TextOrientation=" + TextOrientation + ", Selected=" + Selected + ", Sizeable=" + Sizeable + ", CreateTime=" + CreateTime
                + ", TextLocked=" + TextLocked + ", HatchStyle=" + HatchStyle + ", HatchColor=" + HatchColor + ", TextJustification=" + TextJustification
                + ", UserLong=" + UserLong + ", GroupNumber=" + GroupNumber + ", Fixed=" + Fixed + ", WangType=" + WangType + ", Font=" + Font
                + ", point=" + point + ", point2=" + point2 + '}';
    }

    public Double getX1() {
        return Double.valueOf(point.getX());
    }

    public Double getX2() {
        return Double.valueOf(point2.getX());
    }

    public Double getX() {
        Double x1 = Double.valueOf(point.getX());
        Double x2 = Double.valueOf(point2.getX());
        return Math.abs(x1 - x2);
    }

    public Double getY() {
        return Double.valueOf(point2.getY());
    }

    public Double getY1() {
        return Double.valueOf(point.getY());
    }

    public Double getY2() {
        return Double.valueOf(point2.getY());
    }

    public Double getAlto() {
        return Math.abs(getX1() - getX2());
    }

    public Double getAncho() {
        return Math.abs(getY1() - getY2());
    }

}
