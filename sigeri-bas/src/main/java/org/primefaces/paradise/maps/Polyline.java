/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.primefaces.paradise.maps;

import java.util.List;
import java.util.ArrayList;


public class Polyline {
   private List<LatLong> points = new ArrayList<>();
   private String color = "blue";
   private int weight = 5;
   private String popupMsg;

   public String getPopupMsg() {
      return this.popupMsg;
   }

   public Polyline setPopupMsg(String popupMsg) {
      this.popupMsg = popupMsg;
      return this;
   }

   public String getColor() {
      return this.color;
   }

   public Polyline setColor(String color) {
      this.color = color;
      return this;
   }

   public int getWeight() {
      return this.weight;
   }

   public Polyline setWeight(int weight) {
      this.weight = weight;
      return this;
   }

   public List<LatLong> getPoints() {
      return this.points;
   }

   public Polyline addPoint(List<LatLong> points) {
      this.points.addAll(points);
      return this;
   }

   public Polyline addPoint(LatLong point) {
      this.points.add(point);
      return this;
   }

   public String toString() {
      return "Polyline [points=" + this.points.toString() + ", color=" + this.color + ", weight=" + this.weight + ", popupMsg=" + this.popupMsg + "]";
   }
}