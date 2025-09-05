/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.primefaces.paradise.maps;

public class Marker {
   private LatLong position;
   private String popupMsg;
   private Pulse pulse;

   public Marker(LatLong position) {
      this.position = position;
      this.pulse = new Pulse(false);
   }

   public Marker(LatLong position, String popupMsg) {
      this.position = position;
      this.popupMsg = popupMsg;
      this.pulse = new Pulse(false);
   }

   public Marker(LatLong position, String popupMsg, Pulse pulse) {
      this.position = position;
      this.popupMsg = popupMsg;
      if (pulse == null) {
         this.pulse = new Pulse(false);
      } else {
         this.pulse = pulse;
      }

   }

   public LatLong getPosition() {
      return this.position;
   }

   public Marker setPosition(LatLong position) {
      this.position = position;
      return this;
   }

   public String getPopupMsg() {
      return this.popupMsg;
   }

   public Marker setPopupMsg(String popupMsg) {
      this.popupMsg = popupMsg;
      return this;
   }

   public Pulse getPulse() {
      return this.pulse;
   }

   public void setPulse(Pulse pulse) {
      this.pulse = pulse;
   }

   public String toString() {
      return "Marker [position=" + this.position.toString() + ", popupMsg=" + this.popupMsg + ", pulse=" + this.pulse + "]";
   }
}