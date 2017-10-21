package RobotRemote.RobotServices.Movement.Mocks;

import RobotRemote.Models.MapPoint;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import  javafx.scene.paint.Color;
public final class MockSensor
{
  public static List<MapPoint> Radiation;
  public static List<MapPoint> Apollo;
  private static Path2D apolloPath;
  private static  Path2D radiationPath;

  public static Color GetColor(MapPoint location)
  {
    if (location.y < 0 || location.x < 0 ||
        location.y > 60 || location.x > 85)
      return Color.BLUE;


      if(apolloPath.contains(location.x, location.y))
      {
        return  Color.RED;
      }

      if(radiationPath.contains( location.x, location.y))
      {
        return  Color.GREEN;
      }

      return  Color.WHITE;
  }

  public static void InitMockData()
  {
    Apollo=new ArrayList<>();
    Apollo.add(new MapPoint(18.33,9.55));

    Apollo.add(new MapPoint(21.76,9.41));

    Apollo.add(new MapPoint(21.76,10.84));

    Apollo.add(new MapPoint(20.47,11.84));

    Apollo.add(new MapPoint(17.04,11.70));
    Apollo.add(new MapPoint(18.335,9.55));

    apolloPath = new Path2D.Double();
    int count=0;
    for(MapPoint mapPoint: Apollo) {
      if(count==0)
        apolloPath.moveTo(mapPoint.x,mapPoint.y);
      else
        apolloPath.lineTo(mapPoint.x,mapPoint.y);
      count++;
    }
    //Radiation
    Radiation = new ArrayList<>();
    Radiation.add(new MapPoint(10.0499,10.7));
    Radiation.add(new MapPoint(13.192,6.128));
    Radiation.add(new MapPoint(20.192,3));
    Radiation.add(new MapPoint(29.76,9.98));
    Radiation.add(new MapPoint(26,19.5));
    Radiation.add(new MapPoint(15.33,18.13));
    Radiation.add(new MapPoint(12.1,16));
    Radiation.add(new MapPoint(13.05,12.7));
    radiationPath = new Path2D.Double();
    count=0;
    for(MapPoint mapPoint: Radiation) {
      if(count==0)
        radiationPath.moveTo(mapPoint.x,mapPoint.y);
      else
        radiationPath.lineTo(mapPoint.x,mapPoint.y);
      count++;
    }
  }
}
