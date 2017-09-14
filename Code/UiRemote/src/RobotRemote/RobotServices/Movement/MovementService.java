package RobotRemote.RobotServices.Movement;

import RobotRemote.Models.RobotConfiguration;
import RobotRemote.RobotServices.Connection.RobotConnectionService;
import RobotRemote.RobotServices.Movement.Factories.PilotFactory;
import RobotRemote.Shared.AppStateRepository;
import RobotRemote.Shared.Synchronizer;
import RobotRemote.Shared.ThreadLoop;
import lejos.robotics.navigation.ArcRotateMoveController;

import java.util.Timer;
import java.util.TimerTask;

public class MovementService implements IMovementService {
  private ArcRotateMoveController pilot;
  private LocationState locationState;
  private int loopDelay = 50;
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
      this.pilot.forward();
    });
    // Set location-tracking forward async
    double linearDistanceInterval = linearSpeed * (loopDelay*1.0 / 1000);

    Sleep(300);
    this.RepeatForever(() -> {
      this.locationState.GoingStraight(linearDistanceInterval);
    }, loopDelay);
  }

  @Override
  public void backward() {
    stop();
    // Set pilot backward async dist
    Synchronizer.SerializeRobotCalls(() -> {
      this.pilot.backward();
    });
    // Set location-tracking backward async dist
    double linearDistanceInterval = linearSpeed * (loopDelay*1.0 / 1000);

    Sleep(300);
    this.RepeatForever( () -> {
      this.locationState.GoingStraight(-linearDistanceInterval);
    }, loopDelay);
  }

  @Override
  public void forward(float dist_cm) {
    stop();
    // Set pilot forward async dist, will stop
    Synchronizer.SerializeRobotCalls(() -> {
      this.pilot.forward();
    });
    // Set location-tracking forward async dist, will stop
    long timeToTravel = (long)(dist_cm / linearSpeed)*1000;
    double numLoops = timeToTravel / loopDelay;
    double distancePerLoop = dist_cm / numLoops;

    this.RepeatFor(() -> {
      this.locationState.GoingStraight(distancePerLoop);
    }, loopDelay, timeToTravel);
  }

  @Override
  public void backward(float dist_cm) {
    stop();
    // Set pilot backward async dist, will stop
    Synchronizer.SerializeRobotCalls(() -> {
      this.pilot.backward();
    });
    // Set location-tracking backward async dist, will stop
    double distAbs = Math.abs(dist_cm);
    long timeToTravel = (long)(distAbs/ linearSpeed)*1000;
    double numLoops = timeToTravel / loopDelay;
    double distancePerLoop = distAbs / numLoops;

    this.RepeatFor(() -> {
      this.locationState.GoingStraight(-distancePerLoop);
    }, loopDelay, timeToTravel);
  }

  @Override
  public void turn(float degrees) {
    stop();
    // Set pilot turning forward async degrees, will stop
    Synchronizer.SerializeRobotCalls(() -> {
      pilot.rotate(degrees, true  );
    });
    // Set location-tracking turning async degrees, will stop
    double degreesAbs = Math.abs(degrees);
    long timeToTravel = (long)(degreesAbs/ angularSpeed)*1000;
    double numLoops = timeToTravel / loopDelay;
    double degreesPerLoop = degrees / numLoops;
    Sleep(300);
    this.RepeatFor(() -> {
      this.locationState.ChangingHeading(degreesPerLoop);
    }, loopDelay, timeToTravel);
  }

  @Override
  public void doWhileMoving(Runnable runnable) {
    while (isMoving()) {
      runnable.run();
      Sleep(50);
    }
  }

  boolean isMoving;
  @Override
  public boolean isMoving() {
    Synchronizer.SerializeRobotCalls(() -> {
      isMoving = this.pilot.isMoving();
    });
    return isMoving;
  }

  private void Sleep(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException ignored) {
      stop();
    }
  }

  @Override
  public void stop() {
    // Set pilot stopped
    Synchronizer.SerializeRobotCalls(() -> {
      this.pilot.stop();
    });
    // Set location-tracking stopped
    threadLoop.StopThread();
    timer.cancel();
  }

  private ThreadLoop threadLoop = new ThreadLoop();
  private Timer timer = new Timer();

  private void RepeatFor(Runnable thing, int loopDelay, long timeTillStopThread) {
    threadLoop.StopThread();
    timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        stop();
      }
    }, timeTillStopThread);
    threadLoop.StartThread(thing, loopDelay);
  }

  private void RepeatForever(Runnable thing, int loopDelay) {
    threadLoop.StopThread();
    threadLoop.StartThread(thing, loopDelay);
  }
}
