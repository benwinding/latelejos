package RobotRemote.Services.Asynchronous.Movement;

import RobotRemote.Helpers.Logger;
import RobotRemote.Services.Synchronous.Connection.RobotConnectionService;
import javafx.concurrent.Task;
import lejos.robotics.navigation.ArcRotateMoveController;
import lejos.robotics.navigation.Pose;

public class CustomNavigator implements ICustomNavigator {
  //Configuration
  private double linearSpeed ; //cm per second
  private double mapUpdateInterval ; //seconds
  private double mapUpdateIntervalMs; //milliseconds
  private float distancePerInterval; //cm
  private static ArcRotateMoveController pilot;
  private static ICustomCoordinateSystem cs;
  public static Thread moveThread = new Thread();

  @Override
  public void Init(ICustomCoordinateSystem cs, ArcRotateMoveController pilot, RobotConnectionService robotConnectionService) {
    if(robotConnectionService.IsConnected()) {
      CustomNavigator.pilot = pilot;
      CustomNavigator.cs = cs;

      linearSpeed = robotConnectionService.IsConnected()? pilot.getLinearSpeed():0; //cm per second
      mapUpdateInterval = 0.05; //seconds
      mapUpdateIntervalMs = mapUpdateInterval * 1000; //milliseconds
      distancePerInterval = ((float) (-linearSpeed * mapUpdateInterval)); //cm
    }
  }

  @Override
  public void MoveStraight(float distance) {
    Stop();
    try{
      pilot.travel(distance);
    } catch (Exception ignored) {
    }
    cs.GoingStraight(distance);
  }
  Task<Integer> task;

  @Override
  public void MoveAsync(boolean ...backward) {
    final boolean isBackward = backward.length >= 1;
    if (isBackward) {
      pilot.backward();
      distancePerInterval = -1*Math.abs(distancePerInterval);
    } else {

      distancePerInterval = Math.abs(distancePerInterval);
      pilot.forward();
    }
    if(task==null)
    {
      task = new Task<Integer>() {
        @Override
        protected Integer call() throws Exception {
          while (true)
          {
            Logger.LogCrossThread("TASK: In call loop!");
            try {
              Thread.sleep((long) mapUpdateIntervalMs);
            } catch (InterruptedException interrupted) {
              Logger.LogCrossThread("TASK: interrupted!");
              break;
            }
            cs.GoingStraight(distancePerInterval); // Update coordinate system
          }
          return 0;
        }
      };
      moveThread = new Thread(task);
      moveThread.setDaemon(true);
      moveThread.start();
    }
  }

  @Override
  public void Rotate(float angle) {
    try{
      pilot.rotate(angle*0.955);
    } catch (Exception ignored) {
    }
    cs.ChangingHeading(-angle);
  }

  @Override
  public void Stop() {
    try{
      pilot.stop();
      task.cancel();
      task =null;
    } catch (Exception ignored) {
      Logger.LogCrossThread("Exception: Unable to stop! Reason:"+ ignored.getMessage());
    }
  }

  @Override
  public Pose GetGlobalPose() {
    return cs.GetGlobalPose();
  }
}
