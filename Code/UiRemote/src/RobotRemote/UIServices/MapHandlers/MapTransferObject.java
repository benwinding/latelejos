package RobotRemote.UIServices.MapHandlers;

import RobotRemote.Models.MapPoint;

import java.util.ArrayList;

public class MapTransferObject {
    MapPoint currentPosition;
    MapPoint roverLandingSite;
    ArrayList<MapPoint> noGoZones;
    ArrayList<MapPoint> tracks;
    ArrayList<MapPoint> radiation;
    ArrayList<MapPoint> craters;
    ArrayList<MapPoint> boundary;

    MapTransferObject(MapPoint currentPosition, MapPoint roverLandingSite, ArrayList<MapPoint> noGoZones, ArrayList<MapPoint> tracks,
                      ArrayList<MapPoint> radiation, ArrayList<MapPoint> craters, ArrayList<MapPoint> boundary) {
        this.currentPosition = currentPosition;
        this.noGoZones = noGoZones;
        this.tracks = tracks;
        this.radiation = radiation;
        this.craters = craters;
        this.boundary=boundary;
        this.roverLandingSite=roverLandingSite;
    }
}
