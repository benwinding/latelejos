package RobotRemote.Services.MapHandlers;

import RobotRemote.Models.MapPoint;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/*<!DOCTYPE lunarrovermap [  <!-- A Luna Rover map contianing one boundary, vehicle status, apollo landing site,
rover landing site and track to color mapping, with any number of obstacles, zones, tracks and additional attributes.
 The Luna Rover map uses a global unit of mesurement for all measurmenets -->

*/

@XmlRootElement(name = "LunarRoverMap")
public class MapTransferObject {
  String units = "millimetres";
  ArrayList<MapPoint> boundary;
  MapPoint currentPosition;
  ArrayList<MapPoint> noGoZones;
  ArrayList<MapPoint> tracks;
  ArrayList<MapPoint> radiation;
  ArrayList<MapPoint> craters;
  vehicleStatus vs;

    public MapTransferObject(){

       return;
   }

   public MapTransferObject(MapPoint currentPosition, ArrayList<MapPoint> noGoZones, ArrayList<MapPoint> tracks, ArrayList<MapPoint> radiation, ArrayList<MapPoint> craters) {
    this.currentPosition = currentPosition;
    this.noGoZones = noGoZones;
    this.tracks = tracks;
    this.radiation = radiation;
    this.craters = craters;
    this.units="millimetres";
    this.vs = new vehicleStatus(currentPosition, "north");
  }

    @XmlAttribute
    public String getUnits() {
        return units;
    }

    @XmlElement(name="Vehicle status")
    public vehicleStatus getvehicleStatuses(){
        return vs;
    }

    @XmlElement
    public ArrayList<MapPoint> getNoGoZones() {
        return noGoZones;
    }

    @XmlElement
    public ArrayList<MapPoint> getTracks() {
        return tracks;
    }

    @XmlElement
    public ArrayList<MapPoint> getCraters() {
        return craters;
    }

    @XmlElement
    public ArrayList<MapPoint> getRadiation() {
        return radiation;
    }

}

class vehicleStatus{
    MapPoint currentPosition;
    String currentDirection;
    xmlPoint thisPoint;
    public vehicleStatus(){
        return;
    }
    public vehicleStatus(MapPoint currentPosition, String direction){
        this.currentPosition=currentPosition;
        this.currentDirection=direction;
        this.thisPoint=new xmlPoint(currentPosition);
    }
    @XmlElement(name="point x")
    public xmlPoint getPoint() {
        return thisPoint;
    }
    @XmlElement
    public xmlElement getCurrentDirection(){
        xmlElement xmlDirection=new xmlElement("attribute","key", "value" );
        return xmlDirection;
    }
    @XmlAttribute
    public double getDegrees(){return currentPosition.theta;}
}

class xmlPoint{
    MapPoint position;
    public xmlPoint(){return;}
    public xmlPoint(MapPoint position ){
        this.position=position;
    }
    @XmlAttribute(name="")
    public double getPointx(){
        //String point = "x = \""+Double.toString(position.x) + "\" y = \"" + Double.toString(position.y)+"\"";
        return position.x;
    }
    @XmlAttribute(name="y")
    public double getPointy(){
        //String point = "x = \""+Double.toString(position.x) + "\" y = \"" + Double.toString(position.y)+"\"";
        return position.y;
    }
}

class xmlElement{
    String attribute;
    String key;
    String value;
    public xmlElement(){
        return;
    }
    public xmlElement(String attribute, String key, String value){
        this.attribute=attribute;
        this.key=key;
        this.value=value;
    }
    @XmlElement
    public String getAttribute(){
        return this.attribute;
    }
    @XmlElement
    public xmlKey getKey(){
        xmlKey thisKey= new xmlKey(key);
        return  thisKey;
    }
    @XmlElement
    public xmlValue getValue(){
        xmlValue thisValue = new xmlValue(value);
        return thisValue;
    }

}
class xmlKey{
    String key;
    public xmlKey(String key){
        this.key=key;
    }
}
class xmlValue{
    String value;
    public xmlValue(String value){
        this.value=value;
    }

}