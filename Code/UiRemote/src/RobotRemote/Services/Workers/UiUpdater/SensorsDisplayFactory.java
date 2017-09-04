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
    if(sensorValUltra < 0)
      sensorValUltra = 0;
    gc.setFill(Color.YELLOW);
    gc.fillRect(0,0,sensorValUltra*10,40);
    gc.strokeText(String.format("Ultransonic Value: %.2f", sensorValUltra),0, 25);

    double sensorValColourR = sensorsState.getColourReadingR() * 1000;
    double sensorValColourG = sensorsState.getColourReadingG() * 1000;
    double sensorValColourB = sensorsState.getColourReadingB() * 1000;

    gc.setFill(Color.RED);
    gc.fillRect(0,40,sensorValColourR,20);
    gc.strokeText(String.format("Red Value: %.2f", sensorValColourR),0, 55);

    gc.setFill(Color.GREEN);
    gc.fillRect(0,60,sensorValColourG,20);
    gc.strokeText(String.format("Green Value: %.2f", sensorValColourG),0, 75);

    gc.setFill(Color.BLUE);
    gc.fillRect(0,80,sensorValColourB,20);
    gc.strokeText(String.format("Blue Value: %.2f", sensorValColourB),0, 95);

    return layer;
  }
}
