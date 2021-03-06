package RobotRemote.UIServices.UiUpdater;

import RobotRemote.Shared.ColourTranslator;
import RobotRemote.RobotServices.Sensors.SensorsState;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

class SensorsDisplayLayerFactory {
  static Node CreateSensorsGraph(SensorsState sensorsState) {
    VBox vbox = new VBox();
    Canvas graphUltra = CreateUiSensorUltra(sensorsState.getUltraReadingCm());
    Canvas graphColourR = CreateUiSensorColour("Red", sensorsState.getColourReadingR(), Color.RED);
    Canvas graphColourG = CreateUiSensorColour("Green", sensorsState.getColourReadingG(), Color.GREEN);
    Canvas graphColourB = CreateUiSensorColour("Blue", sensorsState.getColourReadingB(), Color.BLUE);
    Canvas outputColourId = CreateUiColourId(sensorsState.getColourId());

    vbox.getChildren().add(graphUltra);
    FlowPane fp = new FlowPane();
    fp.getChildren().add(outputColourId);
    vbox.getChildren().add(fp);
    vbox.getChildren().add(graphColourR);
    vbox.getChildren().add(graphColourG);
    vbox.getChildren().add(graphColourB);
    return vbox;
  }

  private static Canvas CreateUiSensorTouch(boolean isTouched) {
    Canvas layer = new Canvas(110,50);
    GraphicsContext gc = layer.getGraphicsContext2D();
    if(isTouched) {
      gc.setFill(Color.LIGHTGREEN);
      gc.fillRect(0,0,110,40);
      gc.strokeText(("Touch Sensor \nPressed!"),10, 18);
    }
    else {
      gc.setFill(Color.LIGHTGRAY);
      gc.fillRect(0,0,110,40);
      gc.strokeText(("Touch Sensor \nInactive"),10, 16);
    }
    return layer;
  }

  private static Canvas CreateUiColourId(int colourId) {
    Canvas layer = new Canvas(210,50);
    GraphicsContext gc = layer.getGraphicsContext2D();
    String colourName = ColourTranslator.GetColourName(colourId);
    gc.setFill(Color.web(colourName));
    gc.fillRect(10,0,40,40);
    gc.strokeText(("Colour ID: " + colourName),60, 25);
    return layer;
  }

  private static Canvas CreateUiSensorColour(String colourSensorName, double sensorValue, Color barColour) {
    Canvas layer = new Canvas(300,31);
    GraphicsContext gc = layer.getGraphicsContext2D();
    gc.setFill(barColour);
    gc.strokeText(String.format("%s: %.4f", colourSensorName, sensorValue),10, 10);
    gc.fillRect(10,11,sensorValue * 1000,15);
    return layer;
  }

  private static Canvas CreateUiSensorUltra(double ultraReading) {
    Canvas layer = new Canvas(400,60);
    GraphicsContext gc = layer.getGraphicsContext2D();

    double sensorValUltra = ultraReading;
    if(sensorValUltra < 0)
      sensorValUltra = 0;
    gc.setFill(Color.YELLOW);
    gc.fillRect(10,20,sensorValUltra*5,30);
    gc.strokeText(String.format("Ultransonic Value: %.1f cm", sensorValUltra),10, 20);
    return layer;
  }
}
