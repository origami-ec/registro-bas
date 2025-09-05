/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.primefaces.paradise.maps;

/**
 *
 * @author ORIGAMI
 */
public class LatLong {
   private String latitude;
   private String longitude;

   public LatLong(String latitude, String longitude) {
      this.latitude = latitude;
      this.longitude = longitude;
   }

   public String getLatitude() {
      return this.latitude;
   }

   public LatLong setLatitude(String latitude) {
      this.latitude = latitude;
      return this;
   }

   public String getLongitude() {
      return this.longitude;
   }

   public LatLong setLongitude(String longitude) {
      this.longitude = longitude;
      return this;
   }

   public String toString() {
      return "LatLong [latitude=" + this.latitude + ", longitude=" + this.longitude + "]";
   }
}
