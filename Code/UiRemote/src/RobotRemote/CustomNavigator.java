package RobotRemote;

import RobotRemote.Models.Interfaces.CustomNavigatorInterface;
import RobotRemote.Models.Interfaces.RobotCoordinateSystemInterface;
import RobotRemote.Utils.Logger;
import javafx.concurrent.Task;
import lejos.robotics.navigation.ArcRotateMoveController;
import lejos.robotics.navigation.Pose;

public class CustomNavigator implements CustomNavigatorInterface {
  private static ArcRotateMoveController pilot;
  private static RobotCoordinateSystemInterface cs;
  private Thread moveThread;

  @Override
  public void Init(RobotCoordinateSystemInterface cs, ArcRotateMoveController pilot) {
    CustomNavigator.pilot = pilot;
    CustomNavigator.cs = cs;
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

  @Override
  public void MoveAsync() {
    Stop();
    double linearSpeed = pilot.getLinearSpeed(); //cm per second
    final double mapUpdateInterval = 0.05; //seconds
    final double mapUpdateIntervalMs = mapUpdateInterval * 1000; //milliseconds
    final float distancePerInterval = (float) (linearSpeed * mapUpdateInterval); //cm

    Task<Integer> task = new Task<Integer>() {
      @Override protected Integer call() throws Exception {
        pilot.forward();
        while(!isCancelled()) {
          try {
            Thread.sleep((long) mapUpdateIntervalMs);
          } catch (InterruptedException interrupted) {
            Logger.LogCrossThread("TASK: interrupted!");
            break;
          }
          cs.GoingStraight(distancePerInterval);
        }
        pilot.stop();
        Logger.LogCrossThread("TASK: Exiting call loop!");
        return 0;
      }
    };
    moveThread = new Thread(task);
    moveThread.setDaemon(true);
    moveThread.start();
  }

  @Override
  public void Rotate(float angle) {
    Stop();
    try{
      pilot.rotate(angle);
    } catch (Exception ignored) {
    }
    cs.ChangingHeading(angle);
  }

  @Override
  public void Stop() {
    try{
      moveThread.interrupt();
    } catch (Exception ignored) {
    }
  }

  @Override
  public Pose GetGlobalPose() {
    return cs.GetGlobalPose();
  }
}
