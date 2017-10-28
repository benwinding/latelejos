package RobotRemote.UIServices.UiUpdater;

import RobotRemote.Models.MapPoint;
import RobotRemote.Shared.ColourTranslator;
import RobotRemote.Shared.RobotConfiguration;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

class MapCalculations {
    private static double cycles = 0;
    private static double cachedArea = 0;

    static double CalculateSurveyedArea(List<MapPoint> points, RobotConfiguration config, int circleSize) {
        cycles++;
        if(cycles < 30)
            return cachedArea;
        cycles=0;
        Canvas layer = new Canvas(config.mapW,config.mapH);
        GraphicsContext gc = layer.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        for (MapPoint point : points) {
            double p1x = point.x - circleSize / 2;
            double p1y = point.y - circleSize / 2;
            gc.fillOval(p1x, p1y, circleSize, circleSize);
        }

        WritableImage a = layer.snapshot(null,null);
        PixelReader b = a.getPixelReader();
        // SaveFile(a); // Testing colours
        double width = a.getWidth();
        double height = a.getHeight();
        int countBlack = 1;
        int countAllPixels = (int) (width*height);
        for(int i=0;i<width;i++) {
            for(int j=0;j<height;j++) {
                Color c = b.getColor(i,j);
                int id = ColourTranslator.GetColourId(c);
                if(id == 6)
                    countBlack++;
            }
        }
        cachedArea = 100 - (countBlack * 100) / countAllPixels;
        return cachedArea;
    }

    private static int count = 0;
    private static void SaveFile(WritableImage writableImage) {
        count++;
        File file = new File("test"+count+".png");
        RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
        try {
            ImageIO.write(
                renderedImage,
                "png",
                file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
