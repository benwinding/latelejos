package RobotRemote.RobotServices.Movement.Mocks;

import RobotRemote.Models.MapPoint;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import RobotRemote.RobotServices.Movement.LocationState;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import lejos.robotics.navigation.Pose;

public final class MockSensor
{
  public static List<MapPoint> Radiation;
  public static List<MapPoint> Apollo;
  public static List<MapPoint> NGZ;
  public static List<MapPoint> Crater;
  public static List<MapPoint> Track;
  public static List<MapPoint> Track1;
  private static Path2D apolloPath;
  private static Path2D radiationPath;
  private static Path2D cratterPath;
  private static Path2D trackPath;
  private static Path2D trackPath1;

  public static Color GetColor(LocationState locationState)
  {
    MapPoint location =locationState.GetCurrentPosition();
    if (location.y < 0 || location.x < 0 || location.y > 60 || location.x > 85)
      return Color.BLUE;


    if (apolloPath.contains(location.x, location.y))
    {
      return Color.RED;
    }

    if (radiationPath.contains(location.x, location.y))
    {
      return Color.GREEN;
    }

    if(cratterPath.contains(location.x, location.y))
    {
      return Color.BLACK;
    }
    Pose pose = locationState.GetCurrentPose();
    pose.moveUpdate(3);
    if( trackPath.contains(pose.getX(),pose.getY()) || trackPath1.contains(pose.getX(),pose.getY()))
    {
      return Color.YELLOW;
    }

    return Color.WHITE;
  }

  public static void InitMockData()
  {
    Apollo = new ArrayList<>();
    Apollo.add(new MapPoint(18.33, 9.55));
    Apollo.add(new MapPoint(21.76, 9.41));
    Apollo.add(new MapPoint(21.76, 10.84));
    Apollo.add(new MapPoint(20.47, 11.84));
    Apollo.add(new MapPoint(17.04, 11.70));
    Apollo.add(new MapPoint(18.335, 9.55));
    apolloPath = new Path2D.Double();
    int count = 0;
    for (MapPoint mapPoint : Apollo)
    {
      if (count == 0)
        apolloPath.moveTo(mapPoint.x, mapPoint.y);
      else
        apolloPath.lineTo(mapPoint.x, mapPoint.y);
      count++;
    }
    //Radiation
    Radiation = new ArrayList<>();
    Radiation.add(new MapPoint(10.0499, 10.7));
    Radiation.add(new MapPoint(13.192, 6.128));
    Radiation.add(new MapPoint(20.192, 3));
    Radiation.add(new MapPoint(29.76, 9.98));
    Radiation.add(new MapPoint(26, 19.5));
    Radiation.add(new MapPoint(15.33, 18.13));
    Radiation.add(new MapPoint(12.1, 16));
    Radiation.add(new MapPoint(13.05, 12.7));
    radiationPath = new Path2D.Double();
    count = 0;
    for (MapPoint mapPoint : Radiation)
    {
      if (count == 0)
        radiationPath.moveTo(mapPoint.x, mapPoint.y);
      else
        radiationPath.lineTo(mapPoint.x, mapPoint.y);
      count++;
    }

    //NGZ
    NGZ =new ArrayList<>();
    NGZ.add(new MapPoint(50.47, 46.12));
    NGZ.add(new MapPoint(53.04, 50.41));
    NGZ.add(new MapPoint(49.19, 52.41));
    NGZ.add(new MapPoint(47.04, 49.55));
    NGZ.add(new MapPoint(49.47, 48.55));
    NGZ.add(new MapPoint(50.47, 46.12));

    //Crater
    Crater = new ArrayList<>();
    Crater.add(new MapPoint(81.33, 17.70));
    Crater.add(new MapPoint(80.76,25.27  ));
    Crater.add(new MapPoint(79.47 ,29.98 ));
    Crater.add(new MapPoint(77.76 ,22.55 ));
    Crater.add(new MapPoint(78.33 ,18.41 ));
    Crater.add(new MapPoint(79.47 ,18.70 ));
    cratterPath = new Path2D.Double();
    count = 0;
    for (MapPoint mapPoint : Crater)
    {
      if (count == 0)
        cratterPath.moveTo(mapPoint.x, mapPoint.y);
      else
        cratterPath.lineTo(mapPoint.x, mapPoint.y);
      count++;
    }


    //Track
    Track = new ArrayList<>();
    Track.add(new MapPoint(18.76, 17.55));
//    Track.add(new MapPoint(21.62, 17.70));
//    Track.add(new MapPoint(21.62, 38.27));
//    Track.add(new MapPoint(18.47, 40.12));
//    Track.add(new MapPoint(18.76, 17.55));
    trackPath = new Path2D.Double();
    count = 0;
    for (MapPoint mapPoint : Track)
    {
      if (count == 0)
        trackPath.moveTo(mapPoint.x, mapPoint.y);
      else
        trackPath.lineTo(mapPoint.x, mapPoint.y);
      count++;
    }


    Track1 = new ArrayList<>();
    Track1.add(new MapPoint( 0.90,3.84 ));
    Track1.add(new MapPoint(13.62,6.70 ));
    Track1.add(new MapPoint(13.33,8.55 ));
    Track1.add(new MapPoint(1.04,5.98 ));
    Track1.add(new MapPoint(0.90, 3.84 ));
    count = 0;
    trackPath1 = new Path2D.Double();
    for (MapPoint mapPoint : Track1)
    {
      if (count == 0)
        trackPath1.moveTo(mapPoint.x, mapPoint.y);
      else
        trackPath1.lineTo(mapPoint.x, mapPoint.y);
      count++;
    }
  }
}
