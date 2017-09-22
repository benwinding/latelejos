package RobotRemote.RobotServices.Movement;

import RobotRemote.RobotServices.Connection.RobotConnectionService;
import RobotRemote.RobotServices.Movement.Factories.PilotFactory;
import RobotRemote.Shared.*;
import lejos.robotics.navigation.ArcRotateMoveController;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

public class MovementService implements IMovementService {
  private ArcRotateMoveController pilot;
  private LocationState locationState;
  private int loopDelay = 33;
  private double linearSpeed;
  private double angularSpeed;

  public void Initialize(RobotConfiguration config, RobotConnectionService robotConnectionService, AppStateRepository app) {
    this.pilot = PilotFactory.GetPilot(config, robotConnectionService);
    this.locationState = app.getLocationState();

    Synchronizer.SerializeRobotCalls(() -> {
      linearSpeed = this.pilot.getLinearSpeed();
      angularSpeed = this.pilot.getAngularSpeed();
    });

    this.stop();
  }

  @Override
  public void forward() {
    stop();
    // Set pilot moving forward async
    Synchronizer.SerializeRobotCalls(() -> {
      Logger.log("MOVE: Start forward");
      this.pilot.forward();
    });
    // Set location-tracking forward async
    double linearDistanceInterval = linearSpeed * (loopDelay*1.0 / 1000);

    Sleep(300);
    this.RepeatForever(() -> {
      this.locationState.GoingStraight(linearDistanceInterval);
      return null;
    }, loopDelay);
  }

  @Override
  public void backward() {
    stop();
    // Set pilot backward async dist
    Synchronizer.SerializeRobotCalls(() -> {
      Logger.log("MOVE: Start backward");
      this.pilot.backward();
    });
    // Set location-tracking backward async dist
    double linearDistanceInterval = linearSpeed * (loopDelay*1.0 / 1000);

    Sleep(300);
    this.RepeatForever(() -> {
      this.locationState.GoingStraight(-linearDistanceInterval);
      return null;
    }, loopDelay);
  }

  @Override
  public void forward(float dist_cm) {
    stop();
    // Set pilot forward async dist, will stop
    Synchronizer.SerializeRobotCalls(() -> {
      Logger.log("MOVE: Start forward: " + dist_cm);
      this.pilot.forward();
    });
    // Set location-tracking forward async dist, will stop
    long timeToTravel = (long)(dist_cm / linearSpeed)*1000;
    double numLoops = timeToTravel / loopDelay;
    double distancePerLoop = dist_cm / numLoops;

    this.RepeatFor(() -> {
      this.locationState.GoingStraight(distancePerLoop);
      return null;
    }, loopDelay, timeToTravel);
  }

  @Override
  public void backward(float dist_cm) {
    stop();
    // Set pilot backward async dist, will stop
    Synchronizer.SerializeRobotCalls(() -> {
      Logger.log("MOVE: Start backward: " + dist_cm);
      this.pilot.backward();
    });
    // Set location-tracking backward async dist, will stop
    double distAbs = Math.abs(dist_cm);
    long timeToTravel = (long)(distAbs/ linearSpeed)*1000;
    double numLoops = timeToTravel / loopDelay;
    double distancePerLoop = distAbs / numLoops;

    this.RepeatFor(() -> {
      this.locationState.GoingStraight(-distancePerLoop);
      return null;
    }, loopDelay, timeToTravel);
  }

  @Override
  public void turn(int degrees) {
    stop();
    // Set pilot turning forward async degrees, will stop
    Synchronizer.SerializeRobotCalls(() -> {
      Logger.log("MOVE: Start rotate: " + degrees);
      pilot.rotate(degrees, true  );
    });
    // Set location-tracking turning async degrees, will stop
    double degreesAbs = Math.abs(degrees);
    long timeToTravel = (long)(degreesAbs/ angularSpeed)*1000;
    double numLoops = timeToTravel / loopDelay;
    double degreesPerLoop = degrees / numLoops;
    float degreesFin = (float) (locationState.GetCurrentPosition().theta + degrees);
    Sleep(300);
    this.RepeatFor(
      () -> {
        locationState.ChangingHeading(degreesPerLoop);
        return null;
      },
      () -> locationState.SetHeading(degreesFin),
      loopDelay,
      timeToTravel
    );
  }

  @Override
  public void repeatWhileMoving(Callable repeatThis) throws InterruptedException {
    while (isMoving() && !Thread.interrupted()) {
      try {
        repeatThis.call();
        Thread.sleep(20);
      } catch (Exception ignored) {
        Logger.log("MOVE: Interrupted repeatWhileMoving");
        throw new InterruptedException();
      }
    }
  }

  @Override
  public void waitWhileMoving() throws InterruptedException {
    while (isMoving() && !Thread.interrupted()) {
      try {
        Thread.sleep(20);
      } catch (InterruptedException ignored) {
        Logger.log("MOVE: Interrupted waitWhileMoving");
        throw new InterruptedException();
      }
    }
  }

  private boolean isMoving;
  @Override
  public boolean isMoving() {
    Synchronizer.SerializeRobotCalls(() -> {
      isMoving = this.pilot.isMoving();
    });
    return isMoving;
  }

  @Override
  public void stop() {
    // Set pilot stopped
    Synchronizer.SerializeRobotCalls(() -> {
      Logger.log("MOVE: Stopping");
      this.pilot.stop();
    });
    // Set location-tracking stopped
    threadLoop.StopThread();
    timer.cancel();
  }

  private void Sleep(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException ignored) {
      Logger.log("MOVE: Thread Interrupted... Stopping");
      Synchronizer.SerializeRobotCalls(() -> {
        this.pilot.stop();
      });
      stop();
    }
  }

  private ThreadLoop threadLoop = new ThreadLoop("Thread: Movement Service");
  private Timer timer = new Timer();

  private void RepeatFor(Callable repeatThis, Runnable onFinish, int loopDelay, long timeTillStopThread) {
    threadLoop.StopThread();
    timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        stop();
        onFinish.run();
      }
    }, timeTillStopThread);
    threadLoop.StartThread(repeatThis, loopDelay);
  }

  private void RepeatFor(Callable repeatThis, int loopDelay, long timeTillStopThread) {
    threadLoop.StopThread();
    timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        stop();
      }
    }, timeTillStopThread);
    threadLoop.StartThread(repeatThis, loopDelay);
  }

  private void RepeatForever(Callable thing, int loopDelay) {
    threadLoop.StopThread();
    threadLoop.StartThread(thing, loopDelay);
  }
}
