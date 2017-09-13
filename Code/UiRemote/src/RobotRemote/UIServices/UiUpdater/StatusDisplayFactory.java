package RobotRemote.UIServices.UiUpdater;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

class StatusDisplayFactory {
  static Canvas CreateStatusGreen(boolean statusIsEnabled, int circleSize) {
    Canvas layer = new Canvas(circleSize,circleSize);
    GraphicsContext gc = layer.getGraphicsContext2D();
    if (statusIsEnabled)
      gc.setFill(Color.GREEN);
    else
      gc.setFill(Color.RED);

    gc.fillOval(0,0,circleSize,circleSize);
    return layer;
  }
}
