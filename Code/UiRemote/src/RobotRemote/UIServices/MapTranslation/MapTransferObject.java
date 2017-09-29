package RobotRemote.UIServices.MapTranslation;

import RobotRemote.Models.MapPoint;

import java.util.ArrayList;

public class MapTransferObject {
    MapPoint currentPosition;
    MapPoint roverLandingSite;
    ArrayList<MapPoint> noGoZones;
    ArrayList<MapPoint> Landingtracks;
    ArrayList<MapPoint> radiation;
    ArrayList<MapPoint> craters;
    ArrayList<MapPoint> boundary;

    public void setCurrentPosition(MapPoint currentPosition) {
        this.currentPosition = currentPosition;
    }
    public void setNoGoZones(ArrayList<MapPoint> noGoZones) {
        this.noGoZones = noGoZones;
    }
    public void setBoundary(ArrayList<MapPoint> boundary) {
        this.boundary = boundary;
    }
    public void setCraters(ArrayList<MapPoint> craters) {
        this.craters = craters;
    }
    public void setRadiation(ArrayList<MapPoint> radiation) {
        this.radiation = radiation;
    }
    public void setRoverLandingSite(MapPoint roverLandingSite) {
        this.roverLandingSite = roverLandingSite;
    }
    public void setLandingtracks(ArrayList<MapPoint> landingtracks) {
        Landingtracks = landingtracks;
    }

    public ArrayList<MapPoint> getNoGoZones() {
        return noGoZones;
    }
    public ArrayList<MapPoint> getRadiation() {
        return radiation;
    }
    public MapPoint getRoverLandingSite() {
        return roverLandingSite;
    }
    public MapPoint getCurrentPosition() {
        return currentPosition;
    }
    public ArrayList<MapPoint> getCraters() {
        return craters;
    }
    public ArrayList<MapPoint> getLandingtracks() {
        return Landingtracks;
    }
    public ArrayList<MapPoint> getBoundary() {
        return boundary;
    }

}
