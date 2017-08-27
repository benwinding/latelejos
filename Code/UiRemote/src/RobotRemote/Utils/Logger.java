package RobotRemote.Utils;

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

  public static void TryToLogConsole(String msg) {
    try {
      System.out.println(msg);
    }catch (Exception ignored) {
    }
  }

  public static void TryToWriteToUi(String msg) {
    try{
      TextArea textArea = (TextArea) uiScene.lookup("#messageDisplayer");
      textArea.appendText(msg + '\n');
    }
    catch (Exception ignored) {
    }
  }

  public static void LogCrossThread(String s) {
    System.out.println(s);
  }
}
