package RobotRemote.Services.Workers.UiUpdater;

import RobotRemote.Services.Workers.SensorService.SensorsState;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

class SensorsDisplayFactory {
  static Node CreateSensorsGraph(SensorsState sensorsState) {
    VBox vbox = new VBox();
    Canvas graphUltra = CreateGuiSensorUltra(sensorsState.getUltraReading());
    Canvas graphColourR = CreateGuiSensorColour("Red", sensorsState.getColourReadingR(), Color.RED);
    Canvas graphColourG = CreateGuiSensorColour("Green", sensorsState.getColourReadingG(), Color.GREEN);
    Canvas graphColourB = CreateGuiSensorColour("Blue", sensorsState.getColourReadingB(), Color.BLUE);
    Canvas outputColourId = CreateGuiColourId(sensorsState.getColourId());

    vbox.getChildren().add(graphUltra);
    vbox.getChildren().add(outputColourId);
    vbox.getChildren().add(graphColourR);
    vbox.getChildren().add(graphColourG);
    vbox.getChildren().add(graphColourB);
    return vbox;
  }

  private static Canvas CreateGuiColourId(int colourId) {
    Canvas layer = new Canvas(300,50);
    GraphicsContext gc = layer.getGraphicsContext2D();
    String colourName = GetColourName(colourId);
    gc.setFill(Color.web(colourName));
    gc.fillRect(0,0,30,30);
    gc.strokeText(("Detecting Colour: " + colourName),40, 15);
    return layer;
  }

  private static Canvas CreateGuiSensorColour(String colourSensorName, double colourReadingR, Color barColour) {
    Canvas layer = new Canvas(300,30);
    GraphicsContext gc = layer.getGraphicsContext2D();
    double sensorValColourR = colourReadingR * 1000;
    gc.setFill(barColour);
    gc.fillRect(0,0,sensorValColourR,20);
    gc.strokeText(String.format("%s Value: %.2f", colourSensorName, sensorValColourR),10, 15);
    return layer;
  }

  private static Canvas CreateGuiSensorUltra(double ultraReading) {
    Canvas layer = new Canvas(300,50);
    GraphicsContext gc = layer.getGraphicsContext2D();

    double sensorValUltra = ultraReading*100;
    if(sensorValUltra < 0)
      sensorValUltra = 0;
    gc.setFill(Color.YELLOW);
    gc.fillRect(0,0,sensorValUltra*10,40);
    gc.strokeText(String.format("Ultransonic Value: %.2f", sensorValUltra),10, 25);
    return layer;
  }

  static String GetColourName(int colourId) {
    switch(colourId) {
      case 0: return "RED";
      case 1: return "GREEN";
      case 2: return "BLUE";
      case 3: return "YELLOW";
      case 4: return "MAGENTA";
      case 5: return "ORANGE";
      case 6: return "WHITE";
      case 7: return "BLACK";
      case 8: return "PINK";
      case 9: return "GRAY";
      case 10: return "LIGHT_GRAY";
      case 11: return "DARK_GRAY";
      case 12: return "CYAN";
      case 13: return "BROWN";
      default: return "NONE";
    }
  }
}
