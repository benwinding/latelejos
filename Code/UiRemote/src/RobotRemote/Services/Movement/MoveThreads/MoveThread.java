package RobotRemote.Services.Movement.MoveThreads;

import RobotRemote.Helpers.Synchronizer;
import RobotRemote.Models.RobotConfiguration;
import RobotRemote.Repositories.AppStateRepository;
import RobotRemote.Services.Connection.RobotConnectionService;
import RobotRemote.Services.Movement.Factories.PilotFactory;
import lejos.robotics.navigation.ArcRotateMoveController;

import java.util.Timer;
import java.util.TimerTask;

public class MoveThread implements IMoveThread {
  private ArcRotateMoveController pilot;
  private LocationState locationState;
  private int loopDelay = 50;

  public void Initialize(RobotConfiguration config, RobotConnectionService robotConnectionService, AppStateRepository app) {
    this.pilot = PilotFactory.GetPilot(config, robotConnectionService);
    this.locationState = app.getLocationState();
    this.Stop();
  }

  @Override
  public void Forward() {
    // Set pilot moving forward async
    Synchronizer.RunNotConcurrent(() -> {
      this.pilot.stop();
      this.pilot.forward();
    });
    // Set location-tracking forward async
    double speed = this.pilot.getLinearSpeed();
    double linearDistanceInterval = speed * (loopDelay*1.0 / 1000);
    this.RepeatForever(() -> {
      this.locationState.GoingStraight(linearDistanceInterval);
    }, loopDelay);
  }

  @Override
  public void Backward() {
    // Set pilot backward async dist
    Synchronizer.RunNotConcurrent(() -> {
      this.pilot.stop();
      this.pilot.backward();
    });
    // Set location-tracking backward async dist
    double speed = this.pilot.getLinearSpeed();
    double linearDistanceInterval = speed * (loopDelay*1.0 / 1000);
    this.RepeatForever( () -> {
      this.locationState.GoingStraight(-linearDistanceInterval);
    }, loopDelay);
  }

  @Override
  public void Stop() {
    // Set pilot stopped
    Synchronizer.RunNotConcurrent(() -> {
      this.pilot.stop();
    });
    // Set location-tracking stopped
    if(this.moveThread != null && !this.moveThread.isInterrupted())
      this.moveThread.interrupt();
  }

  @Override
  public void Forward(float dist_cm) {
    // Set pilot forward async dist, will stop
    Synchronizer.RunNotConcurrent(() -> {
      this.pilot.stop();
      this.pilot.forward();
    });
    // Set location-tracking forward async dist, will stop
    double speed = this.pilot.getLinearSpeed();
    long timeToTravel = (long)(dist_cm/speed)*1000;
    double numLoops = timeToTravel / loopDelay;
    double distancePerLoop = dist_cm / numLoops;

    this.RepeatFor(() -> {
      this.locationState.GoingStraight(distancePerLoop);
    }, loopDelay, timeToTravel);
  }

  @Override
  public void Backward(float dist_cm) {
    // Set pilot backward async dist, will stop
    Synchronizer.RunNotConcurrent(() -> {
      this.pilot.stop();
      this.pilot.backward();
    });
    // Set location-tracking backward async dist, will stop
    double speed = this.pilot.getLinearSpeed();
    double distAbs = Math.abs(dist_cm);
    long timeToTravel = (long)(distAbs/speed)*1000;
    double numLoops = timeToTravel / loopDelay;
    double distancePerLoop = distAbs / numLoops;

    this.RepeatFor(() -> {
      this.locationState.GoingStraight(-distancePerLoop);
    }, loopDelay, timeToTravel);
  }

  @Override
  public void Turn(float degrees) {
    // Set pilot turning forward async degrees, will stop
    Synchronizer.RunNotConcurrent(() -> {
      this.pilot.stop();
      if(degrees > 0)
        this.pilot.rotateRight();
      else
        this.pilot.rotateLeft();
    });
    // Set location-tracking turning async degrees, will stop
    double speed = this.pilot.getAngularSpeed();
    double degreesAbs = Math.abs(degrees);
    long timeToTravel = (long)(degreesAbs/speed)*1000;
    double numLoops = timeToTravel / loopDelay;
    double degreesPerLoop = degrees / numLoops;

    this.RepeatFor(() -> {
      this.locationState.ChangingHeading(degreesPerLoop);
    }, loopDelay, timeToTravel);
  }

  private Thread moveThread;

  private void RepeatFor(Runnable thing, int loopDelay, long timeTillStopThread) {
    if(moveThread != null)
      moveThread.interrupt();

    Timer timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        if(moveThread != null && !moveThread.isInterrupted())
          moveThread.interrupt();
      }
    }, timeTillStopThread);
    StartThread(thing, loopDelay);
  }

  private void RepeatForever(Runnable thing, int loopDelay) {
    if(moveThread != null && !moveThread.isInterrupted())
      moveThread.interrupt();
    StartThread(thing, loopDelay);
  }

  private void StartThread(Runnable runThis, int loopDelay) {
    moveThread = new Thread(() -> {
      while(!moveThread.isInterrupted()) {
        runThis.run();
        try {
          Thread.sleep(loopDelay);
        } catch (InterruptedException e) {
          break;
        }
      }
      this.Stop(); // After thread is exited
    });
    moveThread.start();
  }
}
