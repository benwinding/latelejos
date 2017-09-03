package RobotRemote.Services.Workers.UiUpdater;

import RobotRemote.Services.Workers.SensorService.SensorsState;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

class SensorsDisplayFactory {
  static Canvas CreateSensorsGraph(SensorsState sensorsState) {
    Canvas layer = new Canvas(300,100);
    GraphicsContext gc = layer.getGraphicsContext2D();

    double sensorValUltra = sensorsState.getUltraReading()*100;
    gc.setFill(Color.LIGHTYELLOW);
    gc.fillRect(0,0,sensorValUltra*10,40);
    gc.strokeText("Value: " + sensorValUltra,0, 25);

    double sensorValColourR = sensorsState.getColourReadingR()* 100;
    double sensorValColourG = sensorsState.getColourReadingG()* 100;
    double sensorValColourB = sensorsState.getColourReadingB()* 100;

    gc.setFill(Color.RED);
    gc.fillRect(0,40,sensorValColourR,20);
    gc.strokeText("Value: " + sensorValColourR,0, 55);

    gc.setFill(Color.GREEN);
    gc.fillRect(0,60,sensorValColourG,20);
    gc.strokeText("Value: " + sensorValColourG,0, 75);

    gc.setFill(Color.BLUE);
    gc.fillRect(0,80,sensorValColourB,20);
    gc.strokeText("Value: " + sensorValColourB,0, 95);

    return layer;
  }
}
