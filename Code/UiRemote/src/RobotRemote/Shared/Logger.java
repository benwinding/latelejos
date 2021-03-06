package RobotRemote.Shared;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;

public class Logger {
  private static Scene uiScene;
  public static boolean isDisableLog = false;
  public static void Init(Scene scene) {
    uiScene = scene;
  }

  public static void log(String msg) {
    logAll(msg);
  }

  public static void debug(String msg) {
    logAll("....." + msg);
  }

  public static void warn(String msg) {
    warnAll("ERROR:" + msg);
  }

  public static void specialLog(String msg)
  {
      TryToLogConsole("....." +msg);
      TryToWriteToUi("....." +msg);
  }
  private static void logAll(String msg) {
      if(isDisableLog)
          return;
    TryToLogConsole(msg);
    TryToWriteToUi(msg);
  }

  private static void warnAll(String msg) {
      if(isDisableLog)
        return;
    TryToWarnConsole(msg);
    TryToWriteToUi(msg);
  }

  private static void TryToLogConsole(final String msg) {
    Platform.runLater(() -> {
      try {
        System.out.println(msg);
      }catch (Exception ignored) {
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
