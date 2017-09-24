
package RobotRemote.UIServices.MapTranslation.XmlTranslation;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "attribute",
    "boundary",
    "vehicleStatus",
    "apolloLandingSite",
    "roverLandingSite",
    "obstacle",
    "trackToColor",
    "zone",
    "track"
})
@XmlRootElement(name = "lunarovermap")
public class Lunarovermap {
    @XmlElement(required = true)
    public List<Lunarovermap.Attribute> attribute;
    @XmlElement(required = true)
    public Lunarovermap.Boundary boundary;
    @XmlElement(name = "vehicle-status", required = true)
    public Lunarovermap.VehicleStatus vehicleStatus;
    @XmlElement(name = "apollo-landing-site", required = true)
    public Lunarovermap.ApolloLandingSite apolloLandingSite;
    @XmlElement(name = "rover-landing-site", required = true)
    public Lunarovermap.RoverLandingSite roverLandingSite;
    @XmlElement(required = true)
    public Lunarovermap.Obstacle obstacle;
    @XmlElement(name = "track-to-color", required = true)
    public Lunarovermap.TrackToColor trackToColor;
    @XmlElement(required = true)
    public List<Lunarovermap.Zone> zone;
    @XmlElement(required = true)
    public List<Lunarovermap.Track> track;
    @XmlAttribute(name = "units")
    public String units;

    public Lunarovermap(){
        return;
    }

    public List<Lunarovermap.Attribute> getAttribute() {
        if (attribute == null) {
            attribute = new ArrayList<Lunarovermap.Attribute>();
        }
        return this.attribute;
    }

    public Lunarovermap.Boundary getBoundary() {
        return boundary;
    }
    public void setBoundary(Lunarovermap.Boundary value) {
        this.boundary = value;
    }
    public Lunarovermap.VehicleStatus getVehicleStatus() {
        return vehicleStatus;
    }
    public void setVehicleStatus(Lunarovermap.VehicleStatus value) {
        this.vehicleStatus = value;
    }
    public Lunarovermap.ApolloLandingSite getApolloLandingSite() {
        return apolloLandingSite;
    }
    public void setApolloLandingSite(Lunarovermap.ApolloLandingSite value) {
        this.apolloLandingSite = value;
    }
    public Lunarovermap.RoverLandingSite getRoverLandingSite() {
        return roverLandingSite;
    }
    public void setRoverLandingSite(Lunarovermap.RoverLandingSite value) {this.roverLandingSite = value; }
    public Lunarovermap.Obstacle getObstacle() {
        return obstacle;
    }
    public void setObstacle(Lunarovermap.Obstacle value) {
        this.obstacle = value;
    }
    public Lunarovermap.TrackToColor getTrackToColor() {
        return trackToColor;
    }
    public void setTrackToColor(Lunarovermap.TrackToColor value) {
        this.trackToColor = value;
    }
    public List<Lunarovermap.Zone> getZone() {
        if (zone == null) {
            zone = new ArrayList<Lunarovermap.Zone>();
        }
        return this.zone;
    }
    public List<Lunarovermap.Track> getTrack() {
        if (track == null) {
            track = new ArrayList<Lunarovermap.Track>();
        }
        return this.track;
    }
    public String getUnits() {
        return units;
    }
    public void setUnits(String value) {
        this.units = value;
    }

    //APOLLO CLASS
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "point"
    })
    public static class ApolloLandingSite {
        @XmlElement(required = true)
        public Lunarovermap.ApolloLandingSite.Point point;
        public Lunarovermap.ApolloLandingSite.Point getPoint() {
            return point;
        }
        public void setPoint(Lunarovermap.ApolloLandingSite.Point value) {
            this.point = value;
        }
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Point {
            @XmlAttribute(name = "x")
            public Integer x;
            @XmlAttribute(name = "y")
            public Integer y;
            public Integer getX() {
                return x;
            }
            public void setX(Integer value) {
                this.x = value;
            }
            public Integer getY() {
                return y;
            }
            public void setY(Integer value) {
                this.y = value;
            }
        }
    }
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "key",
        "value"
    })
    public static class Attribute {

        @XmlElement(required = true)
        public String key;
        @XmlElement(required = true)
        public String value;
        public String getKey() {
            return key;
        }
        public void setKey(String value) {
            this.key = value;
        }
        public String getValue() {
            return value;
        }
        public void setValue(String value) {
            this.value = value;
        }

    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "area"
    })
    public static class Boundary {

        @XmlElement(required = true)
        public Lunarovermap.Boundary.Area area;
        public Lunarovermap.Boundary.Area getArea() {
            return area;
        }
        public void setArea(Lunarovermap.Boundary.Area value) {
            this.area = value;
        }
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "point"
        })

        public static class Area {

            @XmlElement(required = true)
            public List<Lunarovermap.Boundary.Area.Point> point;
            public List<Lunarovermap.Boundary.Area.Point> getPoint() {
                if (point == null) {
                    point = new ArrayList<Lunarovermap.Boundary.Area.Point>();
                }
                return this.point;
            }

            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class Point {

                @XmlAttribute(name = "x")
                public Integer x;
                @XmlAttribute(name = "y")
                public Integer y;
                public Integer getX() {
                    return x;
                }
                public void setX(Integer value) {
                    this.x = value;
                }

                public Integer getY() {
                    return y;
                }

                public void setY(Integer value) {
                    this.y = value;
                }
            }
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "point"
    })
    public static class Obstacle {

        @XmlElement(required = true)
        public List<Lunarovermap.Obstacle.Point> point;

        public List<Lunarovermap.Obstacle.Point> getPoint() {
            if (point == null) {
                point = new ArrayList<Lunarovermap.Obstacle.Point>();
            }
            return this.point;
        }

        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Point {

            @XmlAttribute(name = "x")
            public Integer x;
            @XmlAttribute(name = "y")
            public Integer y;

            public Integer getX() {
                return x;
            }

            public void setX(Integer value) {
                this.x = value;
            }

            public Integer getY() {
                return y;
            }

            public void setY(Integer value) {
                this.y = value;
            }

        }

    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "point"
    })
    public static class RoverLandingSite {
        @XmlElement(required = true)
        public Lunarovermap.RoverLandingSite.Point point;
        public Lunarovermap.RoverLandingSite.Point getPoint() {
            return point;
        }
        public void setPoint(Lunarovermap.RoverLandingSite.Point value) {
            this.point = value;
        }
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Point {
            @XmlAttribute(name = "x")
            public Integer x;
            @XmlAttribute(name = "y")
            public Integer y;
            public Integer getX() {
                return x;
            }
            public void setX(Integer value) {
                this.x = value;
            }
            public Integer getY() {
                return y;
            }
            public void setY(Integer value) {
                this.y = value;
            }
        }
    }


    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "point"
    })
    public static class Track {

        @XmlElement(required = true)
        public List<Lunarovermap.Track.Point> point;
        @XmlAttribute(name = "type")
        public String type;


        public List<Lunarovermap.Track.Point> getPoint() {
            if (point == null) {
                point = new ArrayList<Lunarovermap.Track.Point>();
            }
            return this.point;
        }

        public String getType() {
            return type;
        }

        public void setType(String value) {
            this.type = value;
        }


        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Point {

            @XmlAttribute(name = "x")
            public Integer x;
            @XmlAttribute(name = "y")
            public Integer y;


            public Integer getX() {
                return x;
            }


            public void setX(Integer value) {
                this.x = value;
            }


            public Integer getY() {
                return y;
            }

            public void setY(Integer value) {
                this.y = value;
            }

        }

    }


    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "attribute"
    })
    public static class TrackToColor {

        @XmlElement(required = true)
        public List<Lunarovermap.TrackToColor.Attribute> attribute;

        public List<Lunarovermap.TrackToColor.Attribute> getAttribute() {
            if (attribute == null) {
                attribute = new ArrayList<Lunarovermap.TrackToColor.Attribute>();
            }
            return this.attribute;
        }

        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "key",
            "value"
        })
        public static class Attribute {

            @XmlElement(required = true)
            public String key;
            @XmlElement(required = true)
            public String value;

            public String getKey() {
                return key;
            }

            public void setKey(String value) {
                this.key = value;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

        }

    }


    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "point",
        "attribute"
    })
    public static class VehicleStatus {

        @XmlElement(required = true)
        public Lunarovermap.VehicleStatus.Point point;
        @XmlElement(required = true)
        public Lunarovermap.VehicleStatus.Attribute attribute;


        public Lunarovermap.VehicleStatus.Point getPoint() {
            return point;
        }


        public void setPoint(Lunarovermap.VehicleStatus.Point value) {
            this.point = value;
        }

        public Lunarovermap.VehicleStatus.Attribute getAttribute() {
            return attribute;
        }


        public void setAttribute(Lunarovermap.VehicleStatus.Attribute value) {
            this.attribute = value;
        }

        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "key",
            "value"
        })
        public static class Attribute {

            @XmlElement(required = true)
            public String key;
            public int value;

            public String getKey() {
                return key;
            }


            public void setKey(String value) {
                this.key = value;
            }


            public int getValue() {
                return value;
            }


            public void setValue(int value) {
                this.value = value;
            }

        }


        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Point {

            @XmlAttribute(name = "x")
            public Integer x;
            @XmlAttribute(name = "y")
            public Integer y;
            public Integer getX() {
                return x;
            }
            public void setX(Integer value) {
                this.x = value;
            }
            public Integer getY() {
                return y;
            }
            public void setY(Integer value) {
                this.y = value;
            }
        }

    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "area"
    })
    public static class Zone {

        @XmlElement(required = true)
        public Lunarovermap.Zone.Area area;
        @XmlAttribute(name = "state")
        public String state;

        public Lunarovermap.Zone.Area getArea() {
            return area;
        }

        public void setArea(Lunarovermap.Zone.Area value) {
            this.area = value;
        }

        public String getState() {
            return state;
        }


        public void setState(String value) {
            this.state = value;
        }

        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "point"
        })
        public static class Area {
            @XmlElement(required = true)
            public List<Lunarovermap.Zone.Area.Point> point;
            public List<Lunarovermap.Zone.Area.Point> getPoint() {
                if (point == null) {
                    point = new ArrayList<Lunarovermap.Zone.Area.Point>();
                }
                return this.point;
            }
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class Point {
                @XmlAttribute(name = "x")
                public Integer x;
                @XmlAttribute(name = "y")
                public Integer y;
                public Integer getX() {
                    return x;
                }
                public void setX(Integer value) {
                    this.x = value;
                }
                public Integer getY() {
                    return y;
                }
                public void setY(Integer value) {
                    this.y = value;
                }
            }
        }
    }
}
