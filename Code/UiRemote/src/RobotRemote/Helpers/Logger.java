package RobotRemote.Helpers;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;

public class Logger {
  private static Scene uiScene;

  public static void Init(Scene scene) {
    uiScene = scene;
  }

  public static void Log(String msg) {
    TryToLogConsole(msg);
    TryToWriteToUi(msg);
  }

  public static void LogCrossThread(String msg) {
    TryToLogConsole(msg);
    TryToWriteToUi(msg);
  }

  public static void WarnCrossThread(String msg) {
    TryToWarnConsole(msg);
    TryToWriteToUi(msg);
  }

  public static void TryToLogConsole(final String msg) {
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
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        try {
          System.err.println(msg);
        }catch (Exception ignored) {
        }
      }
    });
  }
  public static void TryToWriteToUi(String msg) {
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        try {
          TextArea textArea = (TextArea) uiScene.lookup("#messageDisplayer");
          textArea.appendText(msg + '\n');
        } catch(Exception ignored) {
        }
      }
    });
  }
}
