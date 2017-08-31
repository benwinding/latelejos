package RobotRemote.Services.Synchronous.GuiUpdater;

import RobotRemote.Repositories.RobotRepository;
import RobotRemote.Services.RobotServiceBase;
import RobotRemote.UI.Views.RootController;
import javafx.application.Platform;

public class GuiUpdaterService extends RobotServiceBase {
  private RobotRepository robotRepository;
  private RootController rootController;

  public GuiUpdaterService(RobotRepository robotRepository, RootController rootController) {
    super("GUI Updater Service", 300);
    this.robotRepository = robotRepository;
    this.rootController = rootController;
  }

  @Override
  public void Repeat() {
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        UpdateGuiThreadSafe();
      }
    });
  }

  private void UpdateGuiThreadSafe() {
    double val = robotRepository.getSensorsState().getColourReadingR();
    rootController.messageDisplayer.appendText("Current Val: " + val + "\n");
  }

  @Override
  protected void OnShutdown() {}
}
