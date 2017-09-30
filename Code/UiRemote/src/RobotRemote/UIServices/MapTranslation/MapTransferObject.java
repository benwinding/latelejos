package RobotRemote.UIServices.MapTranslation;

import RobotRemote.Models.MapPoint;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Map;

public class MapTransferObject {
    Color vehicleTrackColor;
    Color footprintTrackColor;
    Color landingTrackColor;
    MapPoint currentPosition;
    MapPoint roverLandingSite;
    MapPoint apolloLandingSite;
    ArrayList<MapPoint> noGoZones;
    ArrayList<MapPoint> Landingtracks;
    ArrayList<MapPoint> radiation;
    ArrayList<MapPoint> craters;
    ArrayList<MapPoint> boundary;
    ArrayList<MapPoint> unexplored;
    ArrayList<MapPoint> explored;
    ArrayList<MapPoint> obstacles;

    public void setApolloLandingSite(MapPoint apolloLandingSite) {
        this.apolloLandingSite = apolloLandingSite;
    }
    public void setObstacles(ArrayList<MapPoint> obstacles) {
        this.obstacles = obstacles;
    }
    public void setExplored(ArrayList<MapPoint> explored) {
        this.explored = explored;
    }
    public void setUnexplored(ArrayList<MapPoint> unexplored) {
        this.unexplored = unexplored;
    }
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
    public void setFootprintTrackColor(Color footprintTrackColor) {
        this.footprintTrackColor = footprintTrackColor;
    }
    public void setLandingTrackColor(Color landingTrackColor) {
        this.landingTrackColor = landingTrackColor;
    }
    public void setVehicleTrackColor(Color vehicleTrackColor) {
        this.vehicleTrackColor = vehicleTrackColor;
    }

    public MapPoint getApolloLandingSite() {
        return apolloLandingSite;
    }
    public ArrayList<MapPoint> getObstacles() {
        return obstacles;
    }
    public ArrayList<MapPoint> getExplored() {
        return explored;
    }
    public ArrayList<MapPoint> getUnexplored() {
        return unexplored;
    }
    public Color getFootprintTrackColor() {
        return footprintTrackColor;
    }
    public Color getLandingTrackColor() {
        return landingTrackColor;
    }
    public Color getVehicleTrackColor() {
        return vehicleTrackColor;
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
