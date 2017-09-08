package RobotRemote.Services.RobotCommander;

import RobotRemote.Helpers.Logger;
import RobotRemote.Services.RobotServiceBase;

public class ModeAutoMapping extends RobotServiceBase {
  public ModeAutoMapping() {
    super("Mode Automatic", 100);
  }

  @Override
  protected void OnStart() {
    Logger.log("Mode: Auto Mapping Started ...");
  }

  @Override
  protected void Repeat() {
    Logger.log("Auto Mapping Loop ...");
  }

  @Override
  protected void OnShutdown() {
    Logger.log("Mode: Auto Mapping ending ...");
  }
}
