package RobotRemote.UIServices.UiUpdater;

import RobotRemote.Models.MapPoint;
import RobotRemote.Shared.RobotConfiguration;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.List;

class MapCalculations {
    static double CalculateSurveyedArea(List<MapPoint> points, RobotConfiguration config, int circleSize) {
        Canvas layer = new Canvas(config.mapW,config.mapW);
        GraphicsContext gc = layer.getGraphicsContext2D();
        UpdaterUtils.DrawFilledCirclesOnContext(gc,points,config,Color.BLACK,circleSize);
        WritableImage a = layer.snapshot(null,null);
        PixelReader b = a.getPixelReader();
        int countBlack = 0;
        int countWhite = 0;
        for(int i=0;i<a.getWidth();i++) {
            for(int j=0;j<a.getWidth();j++) {
                Color c = b.getColor(i,j);
                if(c == Color.BLACK)
                    countBlack++;
                else
                    countWhite++;
            }
        }
        double areaCovered = countBlack * 100 / countWhite;
        return areaCovered;
    }
}
