/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.primefaces.paradise.maps;

public class Circle {
   private LatLong position;
   private int radius = 200;
   private int weight = 5;
   private double fillOpacity = 0.2D;
   private String color = "black";
   private String fillColor = "blue";
   private String popupMsg;

   public String getPopupMsg() {
      return this.popupMsg;
   }

   public Circle setPopupMsg(String popupMsg) {
      this.popupMsg = popupMsg;
      return this;
   }

   public int getWeight() {
      return this.weight;
   }

   public Circle setWeight(int weight) {
      this.weight = weight;
      return this;
   }

   public String getColor() {
      return this.color;
   }

   public Circle setColor(String color) {
      this.color = color;
      return this;
   }

   public String getFillColor() {
      return this.fillColor;
   }

   public Circle setFillColor(String fillColor) {
      this.fillColor = fillColor;
      return this;
   }

   public double getFillOpacity() {
      return this.fillOpacity;
   }

   public Circle setFillOpacity(double fillOpacity) {
      this.fillOpacity = fillOpacity;
      return this;
   }

   public LatLong getPosition() {
      return this.position;
   }

   public Circle setPosition(LatLong position) {
      this.position = position;
      return this;
   }

   public int getRadius() {
      return this.radius;
   }

   public Circle setRadius(int radius) {
      this.radius = radius;
      return this;
   }

   public String toString() {
      return "Circle [position=" + this.position.toString() + ", radius=" + this.radius + ", color=" + this.color + ", fillColor=" + this.fillColor + ", fillOpacity=" + this.fillOpacity + ", weight=" + this.weight + ", popupMsg=" + this.popupMsg + "]";
   }
}
