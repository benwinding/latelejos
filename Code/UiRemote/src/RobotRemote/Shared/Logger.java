package RobotRemote.Shared;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;

public class Logger {
  private static Scene uiScene;

  public static void Init(Scene scene) {
    uiScene = scene;
  }

  public static void log(String msg, int level) {
    for(int i=0; i<level; i++) {
      msg = "-" + msg;
    }

    TryToLogConsole(msg);
    TryToWriteToUi(msg);
  }

  public static void log(String msg) {
    TryToLogConsole(msg);
    TryToWriteToUi(msg);
  }

  public static void warn(String msg) {
    TryToWarnConsole(msg);
    TryToWriteToUi(msg);
  }

  private static void TryToLogConsole(final String msg) {
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        try {
          System.out.println(msg);
        }catch (Exception ignored) {
        }
      }
    });
  }

  private static void TryToWarnConsole(final String msg) {
    Platform.runLater(() -> {
      try {
        System.err.println(msg);
      }catch (Exception ignored) {
      }
    });
  }
  private static void TryToWriteToUi(String msg) {
    Platform.runLater(() -> {
      try {
        TextArea textArea = (TextArea) uiScene.lookup("#messageDisplayer");
        textArea.appendText(msg + '\n');
      } catch(Exception ignored) {
      }
    });
  }
}
