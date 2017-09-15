package RobotRemote.UIServices.MapHandlers;

import RobotRemote.Models.MapPoint;

import java.util.ArrayList;

public class MapTransferObject {
  MapPoint currentPosition;
  ArrayList<MapPoint> noGoZones;
  ArrayList<MapPoint> tracks;
  ArrayList<MapPoint> radiation;
  ArrayList<MapPoint> craters;

  MapTransferObject(MapPoint currentPosition, ArrayList<MapPoint> noGoZones, ArrayList<MapPoint> tracks, ArrayList<MapPoint> radiation, ArrayList<MapPoint> craters) {
    this.currentPosition = currentPosition;
    this.noGoZones = noGoZones;
    this.tracks = tracks;
    this.radiation = radiation;
    this.craters = craters;
  }
}
