package RobotRemote;

import javafx.scene.Scene;
import javafx.scene.control.TextArea;

public class Logger {
  private static Scene uiScene;

  static void Init(Scene scene) {
    uiScene = scene;
  }

  static void Log(String msg) {
    System.out.println(msg);
    TryToWriteToUi(msg);
  }

  private static void TryToWriteToUi(String msg) {
    try{
      TextArea textArea = (TextArea) uiScene.lookup("#messageDisplayer");
      textArea.appendText(msg + '\n');
    }
    catch (Exception e) {
      System.out.println("Ui not loaded, could not display message on UI");
    }
  }

  public static void LogCrossThread(String s) {
//    System.out.println(s);
  }
}
