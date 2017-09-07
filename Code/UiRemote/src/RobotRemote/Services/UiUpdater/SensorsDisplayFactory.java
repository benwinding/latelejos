package RobotRemote.Services.UiUpdater;

import RobotRemote.Helpers.ColourTranslator;
import RobotRemote.Services.SensorService.SensorsState;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.FlowPane;
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
    Canvas touchGui = CreateGuiTouch(sensorsState.getTouchReading() == 1.0f);

    vbox.getChildren().add(graphUltra);
    FlowPane fp = new FlowPane();
    fp.getChildren().add(outputColourId);
    fp.getChildren().add(touchGui);
    vbox.getChildren().add(fp);
    vbox.getChildren().add(graphColourR);
    vbox.getChildren().add(graphColourG);
    vbox.getChildren().add(graphColourB);
    return vbox;
  }

  private static Canvas CreateGuiTouch(boolean isTouched) {
    Canvas layer = new Canvas(110,50);
    GraphicsContext gc = layer.getGraphicsContext2D();
    if(isTouched) {
      gc.setFill(Color.LIGHTGREEN);
      gc.fillRect(0,0,60,30);
      gc.strokeText(("Pressed!"),10, 15);
    }
    else {
      gc.setFill(Color.LIGHTGRAY);
      gc.fillRect(0,0,60,30);
      gc.strokeText(("Inactive"),10, 15);
    }
    return layer;
  }

  private static Canvas CreateGuiColourId(int colourId) {
    Canvas layer = new Canvas(180,50);
    GraphicsContext gc = layer.getGraphicsContext2D();
    String colourName = ColourTranslator.GetColourName(colourId);
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
    Canvas layer = new Canvas(400,50);
    GraphicsContext gc = layer.getGraphicsContext2D();

    double sensorValUltra = ultraReading*100;
    if(sensorValUltra < 0)
      sensorValUltra = 0;
    gc.setFill(Color.YELLOW);
    gc.fillRect(0,0,sensorValUltra*30,40);
    gc.strokeText(String.format("Ultransonic Value: %.2f", sensorValUltra),10, 25);
    return layer;
  }
}
