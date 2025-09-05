/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.primefaces.paradise.maps;


public class Pulse {
   private boolean pulsing;
   private int size;
   private String color;

   public Pulse(boolean pulsing) {
      this.pulsing = pulsing;
   }

   public Pulse(boolean pulsing, int size) {
      this.pulsing = pulsing;
      this.size = size;
   }

   public Pulse(boolean pulsing, int size, String color) {
      this.pulsing = pulsing;
      this.size = size;
      this.color = color;
   }

   public boolean isPulsing() {
      return this.pulsing;
   }

   public void setPulsing(boolean pulsing) {
      this.pulsing = pulsing;
   }

   public int getSize() {
      return this.size;
   }

   public void setSize(int size) {
      this.size = size;
   }

   public String getColor() {
      return this.color;
   }

   public void setColor(String color) {
      this.color = color;
   }

   public String toString() {
      return "Pulse [pulsing=" + this.pulsing + ", size=" + this.size + ", color=" + this.color + "]";
   }
}