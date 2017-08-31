package RobotRemote.Services.Asynchronous.Movement;

import RobotRemote.Models.MoveCommand;
import RobotRemote.Helpers.Logger;
import RobotRemote.Models.RobotConfig;
import RobotRemote.Services.Synchronous.Connection.RobotConnectionService;
import lejos.remote.ev3.RemoteRequestEV3;
import lejos.robotics.navigation.ArcRotateMoveController;
import lejos.robotics.navigation.Pose;

import static RobotRemote.Models.MoveCommand.*;

public class RobotMoveService {
  static private ICustomNavigator navigator;
  public static boolean IsDirty;
  public static MoveCommand PreCommand;
  private RobotConfig robotConfig;
  private RobotConnectionService robotConnectionService;

  public RobotMoveService(RobotConfig robotConfig, RobotConnectionService robotConnectionService) {
    this.robotConfig = robotConfig;
    this.robotConnectionService = robotConnectionService;
  }

  public ArcRotateMoveController GetPilot() {
    ArcRotateMoveController pilot;
    try {
      RemoteRequestEV3 brick = robotConnectionService.GetBrick();
      pilot = brick.createPilot(2.1f, 4.4f, "A", "B");
      pilot.setLinearSpeed(5);
      pilot.setAngularSpeed(30);
      pilot.setAngularAcceleration(10);
      return pilot;
    } catch (Exception e) {
      Logger.Log("Pilot Factory: Unable to get pilot from ev3, using test pilot");
      return null;
    }
  }

  public void InitMotors(RobotConfig config) {
    ICustomCoordinateSystem cs = new CustomCoordinateSystem(config.initX, config.initY, config.initTheta);
    ArcRotateMoveController pilot = GetPilot();
    navigator = new CustomNavigator();
    navigator.Init(cs, pilot, robotConnectionService);
  }

  public void MoveMotors(final MoveCommand command) {
    Logger.Log("Moving motors: " + command);
    if(PreCommand== command && (PreCommand== Forward||PreCommand == Backward))
      return;
    navigator.Stop();
    switch (command) {
      case Forward:
        navigator.MoveAsync();
        break;
      case Backward:
        navigator.MoveAsync(true);
        break;
      case Left:
        navigator.Rotate(90);
        break;
      case Right:
        navigator.Rotate(-90);
        break;
      case  Stop:
        break;
      default:
        Logger.Log("Unknown command: " + command);
    }
  }

  public Pose GetCoords() {
    return navigator.GetGlobalPose();
  }

  public void StopAll() {
    Logger.Log("Stopping All Motors");
    navigator.Stop();
  }
}