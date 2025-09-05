/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.primefaces.paradise.maps;

import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author ORIGAMI
 */

public class Layer {
   private List<Marker> markers = new ArrayList<>();
   private List<Polyline> polylines = new ArrayList();
   private List<Circle> circles = new ArrayList();
   private String label;
   private boolean checked = true;
   private boolean clusterEnabled = false;
   private int clusterDisableAtZoom = 19;
   private int clusterMaxRadius = 80;

   public int getClusterMaxRadius() {
      return this.clusterMaxRadius;
   }

   public Layer setClusterMaxRadius(int clusterMaxRadius) {
      this.clusterMaxRadius = clusterMaxRadius;
      return this;
   }

   public int getClusterDisableAtZoom() {
      return this.clusterDisableAtZoom;
   }

   public Layer setClusterDisableAtZoom(int clusterDisableAtZoom) {
      this.clusterDisableAtZoom = clusterDisableAtZoom;
      return this;
   }

   public boolean isClusterEnabled() {
      return this.clusterEnabled;
   }

   public Layer setClusterEnabled(boolean clusterEnabled) {
      this.clusterEnabled = clusterEnabled;
      return this;
   }

   public List<Circle> getCircles() {
      return this.circles;
   }

   public Layer addCircle(List<Circle> circles) {
      this.circles.addAll(circles);
      return this;
   }

   public Layer addCircle(Circle circle) {
      this.circles.add(circle);
      return this;
   }

   public List<Polyline> getPolylines() {
      return this.polylines;
   }

   public Layer addPolyline(List<Polyline> polylines) {
      this.polylines.addAll(polylines);
      return this;
   }

   public Layer addPolyline(Polyline polyline) {
      this.polylines.add(polyline);
      return this;
   }

   public boolean isChecked() {
      return this.checked;
   }

   public Layer setChecked(boolean checked) {
      this.checked = checked;
      return this;
   }

   public String getLabel() {
      return this.label;
   }

   public Layer setLabel(String label) {
      this.label = label;
      return this;
   }

   public List<Marker> getMarkers() {
      return this.markers;
   }

   public Layer addMarker(List<Marker> markers) {
      this.markers.addAll(markers);
      return this;
   }

   public Layer addMarker(Marker marker) {
      this.markers.add(marker);
      return this;
   }

   public String toString() {
      return "Layer [markers=" + this.markers.toString() + ", polylines=" + this.polylines.toString() + ", circles=" + this.circles.toString() + ", label=" + this.label + ", checked=" + this.checked + ", cluster=" + this.clusterEnabled + ", clusterDisableAtZoom=" + this.clusterDisableAtZoom + ", clusterMaxRadius=" + this.clusterMaxRadius + "]";
   }
}