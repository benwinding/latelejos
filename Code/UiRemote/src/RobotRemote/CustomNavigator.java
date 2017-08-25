package RobotRemote;

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
    moveThread.interrupt();
    pilot.travel(distance);
    cs.GoingStraight(distance);
  }

  @Override
  public void MoveAsync(final float distanceIncrement) {
    Task<Integer> task = new Task<Integer>() {
      @Override protected Integer call() throws Exception {
        while(!isCancelled()) {
          if(pilot != null) {
            pilot.travel(distanceIncrement);
            cs.GoingStraight(distanceIncrement);
          }
          Logger.LogCrossThread("TASK: Moving robot distance:" + distanceIncrement);
          try {
            Thread.sleep(10);
          } catch (InterruptedException interrupted) {
            Logger.LogCrossThread("TASK: interrupted!");
            break;
          }
        }
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
    moveThread.interrupt();
    pilot.rotate(angle);
    cs.ChangingHeading(angle);
  }

  @Override
  public void Stop() {
    moveThread.interrupt();
  }

  @Override
  public Pose GetGlobalPose() {
    return cs.GetGlobalPose();
  }
}
