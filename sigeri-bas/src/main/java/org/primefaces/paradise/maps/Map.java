/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.primefaces.paradise.maps;


import java.util.ArrayList;
import java.util.List;

public class Map {
   List<Layer> layers = new ArrayList();
   private LatLong center;
   private String width = "300px";
   private String height = "200px";
   private String attribution = "Map data &copy; <a href=\"http://openstreetmap.org\">OpenStreetMap</a> contributors,<a href=\"http://creativecommons.org/licenses/by-sa/2.0/\">CC-BY-SA</a>";
   private int zoom = 1;
   private int minZoom = 1;
   private int maxZoom = 19;
   private boolean zoomControl = true;
   private boolean zoomEnabled = true;
   private boolean draggingEnabled = true;
   private boolean layerControl = true;
   private boolean miniMap = true;
   private int miniMapWidth = 100;
   private int miniMapHeight = 100;
   private String miniMapPosition = "bottomright";
   private String urlTemplate = "http://{s}.tile.osm.org/{z}/{x}/{y}.png";

   public List<Layer> getLayers() {
      return this.layers;
   }

   public Map addLayer(List<Layer> layers) {
      this.layers.addAll(layers);
      return this;
   }

   public Map addLayer(Layer layer) {
      this.layers.add(layer);
      return this;
   }

   public boolean isLayerControl() {
      return this.layerControl;
   }

   public Map setLayerControl(boolean layerControl) {
      this.layerControl = layerControl;
      return this;
   }

   public String getWidth() {
      return this.width;
   }

   public Map setWidth(String width) {
      this.width = width;
      return this;
   }

   public String getHeight() {
      return this.height;
   }

   public Map setHeight(String height) {
      this.height = height;
      return this;
   }

   public LatLong getCenter() {
      return this.center;
   }

   public Map setCenter(LatLong center) {
      this.center = center;
      return this;
   }

   public String getAttribution() {
      return this.attribution;
   }

   public Map setAttribution(String attribution) {
      this.attribution = attribution;
      return this;
   }

   public int getZoom() {
      return this.zoom;
   }

   public Map setZoom(int zoom) {
      this.zoom = zoom;
      return this;
   }

   public int getMinZoom() {
      return this.minZoom;
   }

   public Map setMinZoom(int minZoom) {
      this.minZoom = minZoom;
      return this;
   }

   public int getMaxZoom() {
      return this.maxZoom;
   }

   public Map setMaxZoom(int maxZoom) {
      this.maxZoom = maxZoom;
      return this;
   }

   public boolean isZoomControl() {
      return this.zoomControl;
   }

   public Map setZoomControl(boolean zoomControl) {
      this.zoomControl = zoomControl;
      return this;
   }

   public boolean isZoomEnabled() {
      return this.zoomEnabled;
   }

   public Map setZoomEnabled(boolean zoomEnabled) {
      this.zoomEnabled = zoomEnabled;
      return this;
   }

   public boolean isDraggingEnabled() {
      return this.draggingEnabled;
   }

   public Map setDraggingEnabled(boolean draggingEnabled) {
      this.draggingEnabled = draggingEnabled;
      return this;
   }

   public boolean isMiniMap() {
      return this.miniMap;
   }

   public void setMiniMap(boolean miniMap) {
      this.miniMap = miniMap;
   }

   public int getMiniMapWidth() {
      return this.miniMapWidth;
   }

   public void setMiniMapWidth(int miniMapWidth) {
      this.miniMapWidth = miniMapWidth;
   }

   public int getMiniMapHeight() {
      return this.miniMapHeight;
   }

   public void setMiniMapHeight(int miniMapHeight) {
      this.miniMapHeight = miniMapHeight;
   }

   public String getMiniMapPosition() {
      return this.miniMapPosition;
   }

   public void setMiniMapPosition(String miniMapPosition) {
      this.miniMapPosition = miniMapPosition;
   }

   public String getUrlTemplate() {
      return this.urlTemplate;
   }

   public void setUrlTemplate(String urlTemplate) {
      this.urlTemplate = urlTemplate;
   }

   public String toString() {
      return "Map [layers=" + this.layers.toString() + ", center=" + this.center + ", width=" + this.width + ", height=" + this.height + ", attribution=" + this.attribution + ", zoom=" + this.zoom + ", minZoom=" + this.minZoom + ", maxZoom=" + this.maxZoom + ", zoomControl=" + this.zoomControl + ", zoomEnabled=" + this.zoomEnabled + ", dragging=" + this.draggingEnabled + ", layerControl=" + ", miniMap=" + this.miniMap + ", miniMapWidth=" + this.miniMapWidth + ", miniMapHeight=" + this.miniMapHeight + ", miniMapPosition=" + this.miniMapPosition + ", urlTemplate=" + this.urlTemplate + this.layerControl + "]";
   }
}