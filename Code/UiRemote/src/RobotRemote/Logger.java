package RobotRemote;

import javafx.scene.Scene;
import javafx.scene.control.TextArea;

public class Logger {
  private static Scene uiScene;

  static void Init(Scene scene) {
    uiScene = scene;
  }

  static void Log(String msg) {
    TryToLogConsole(msg);
    TryToWriteToUi(msg);
  }

  private static void TryToLogConsole(String msg) {
    try {
      System.out.println(msg);
    }catch (Exception e) {

    }
  }

  private static void TryToWriteToUi(String msg) {
    try{
      TextArea textArea = (TextArea) uiScene.lookup("#messageDisplayer");
      textArea.appendText(msg + '\n');
    }
    catch (Exception e) {
    }
  }

  public static void LogCrossThread(String s) {
//    System.out.println(s);
  }
}
