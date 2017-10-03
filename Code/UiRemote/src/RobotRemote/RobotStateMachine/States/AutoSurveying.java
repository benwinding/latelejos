package RobotRemote.RobotStateMachine.States;

import RobotRemote.Models.MapPoint;
import RobotRemote.RobotServices.Movement.IMovementService;
import RobotRemote.RobotServices.Sensors.SensorsState;
import RobotRemote.RobotStateMachine.Events.AutoSurvey.EventAutomapDetectedObject;
import RobotRemote.RobotStateMachine.IModeState;
import RobotRemote.Shared.*;
import com.google.common.eventbus.EventBus;
import javafx.scene.paint.Color;
import lejos.robotics.navigation.Pose;
import sun.java2d.cmm.ColorTransform;

public class AutoSurveying implements IModeState {
   enum AutoSurveyingInternalState {
        BackToLastPosition,
        ZigZagginSurvey,
        DetectedObject,
        CraterDetected,
        PathDetected,
        BorderDetected,
      BackToHome, ApolloDetected
    }

    enum Direction {
        Up,
        Down,
        Left,
        Right
    }

    private boolean IsOnState;
    private IMovementService moveThread;
    private ServiceManager sm;
    private EventBus eventBus;
    private SensorsState sensorState;
    private ThreadLoop threadLoop;
    private RobotConfiguration config;
    private AutoSurveyingInternalState internalState;
    private Direction direction;
    private boolean IsReverse;
    private MapPoint LastPoint;
    public AutoSurveying(ServiceManager sm) {
        this.sm = sm;
        this.eventBus = sm.getEventBus();
        this.sensorState = sm.getAppState().getSensorsState();
        this.threadLoop = sm.getRobotStateMachineThread();
        this.moveThread = sm.getMovementService();
        this.config = new RobotConfiguration();
        this.LastPoint = new MapPoint(0,0);
        this.IsReverse = false;
        this.direction = Direction.Up;
    }

    public void Enter() {
        this.eventBus.register(this);
        Logger.log("ENTER AUTOSURVEYING STATE...");
        internalState = AutoSurveyingInternalState.BackToLastPosition;
        this.threadLoop.StartThread(() -> {
            LoopThis();
            return null;
        }, 100);
        Logger.isDisableLog = true;
    }

    public void Leave() {
        Logger.isDisableLog = false;
        this.eventBus.unregister(this);
        this.threadLoop.StopThread();
        this.moveThread.stop();
        Logger.log("LEAVE AUTOSURVEYING STATE...");
    }

    private void LoopThis() throws InterruptedException {
        Logger.specialLog("LoopThis");
        switch (internalState) {
            case BackToLastPosition:
                HandleBackToLastPosition();
                break;
            case ZigZagginSurvey:
                ZigZagAcrossMap();
                break;
            case DetectedObject:
                HandleDetectedObject();
                break;
            case BorderDetected:
                HandleDetectedBorder();
                break;
            case PathDetected:
                HandleDetectedTrail();
                break;
          case ApolloDetected:
            HandleDetectedTrail();
            break;
        }
    }

    private void HandleBackToLastPosition() throws InterruptedException {
        //for now just turn back to up direction
        Logger.specialLog("Handling Back To Last Posistion");
        //If  last time move up then turn back to move up otherwise move down;
        int initDegree = direction == Direction.Up ? -180 : 0;
        int turnBackDegree = initDegree - (int)moveThread.GetCurrentPose().getHeading();
        moveThread.turn(turnBackDegree);
        moveThread.waitWhileMoving();
        internalState = AutoSurveyingInternalState.ZigZagginSurvey;
    }

    private void ZigZagAcrossMap() throws InterruptedException {
        // Move leftwards across the map
        moveThread.forward();
        moveThread.repeatWhileMoving(this::checkSurroundings);
    }

    private Object checkWhileTurn()throws InterruptedException
    {
      if (isThereABorder()) {
        Logger.specialLog("checkWhileTurn: Detected Border While Turn - Color: "+ sensorState.getColourEnum());
        moveThread.stop();
        moveThread.backward(5);
        IsReverse = !IsReverse;
      }
      return null;
    }

    private Object checkSurroundings() throws InterruptedException {
        if (isThereABorder()) {
          moveThread.stop();
          internalState = AutoSurveyingInternalState.BorderDetected;
          Logger.specialLog("checkSurroundings: Detected Border - Color: "+ sensorState.getColourEnum().toString());
        }

        if (isThereAnObject()) {
          moveThread.stop();
          internalState = AutoSurveyingInternalState.DetectedObject;
          Logger.specialLog("checkSurroundings: Detected Object");
        }

        if(isThereATrail())
        {
          moveThread.stop();
          internalState = AutoSurveyingInternalState.PathDetected;
          Logger.specialLog("checkSurroundings: Trail Detected - Color: "+ sensorState.getColourEnum());
        }

//        if(isThereApollo())
//        {
//          Logger.specialLog("checkSurroundings: Apollo Detected - Color: "+ sensorState.getColourEnum());
//          moveThread.stop();
//          internalState = AutoSurveyingInternalState.ApolloDetected;
//        }
        return null;
    }

    private void HandleDetectedObject() throws InterruptedException {
        Logger.specialLog("Handling Detected Object going left");
        RegisterObjectDetected();
        moveThread.turn(90);
        moveThread.waitWhileMoving();
        moveThread.forward(15);
        moveThread.waitWhileMoving();
        moveThread.turn(-90);
        moveThread.waitWhileMoving();
        moveThread.forward(40);
        moveThread.waitWhileMoving();
        moveThread.turn(-90);
        moveThread.waitWhileMoving();
        moveThread.forward(15);
        moveThread.waitWhileMoving();
        moveThread.turn(90);
        moveThread.waitWhileMoving();
        internalState = AutoSurveyingInternalState.ZigZagginSurvey;
    }

  private void RegisterObjectDetected() {
    Pose current = this.sm.getAppState().getLocationState().GetCurrentPose();
    current.moveUpdate((float) this.config.obstacleAvoidDistance);
    this.eventBus.post(new EventAutomapDetectedObject(current));
  }

  private void HandleDetectedBorder() throws InterruptedException {
        Logger.debug("Handling Detected Border going left");
        switch (direction) {
            case Up:
                TurnRobotAround(true);
                internalState = AutoSurveyingInternalState.ZigZagginSurvey;
                direction = Direction.Down;
                break;
            case Down:
                TurnRobotAround(false);
                internalState = AutoSurveyingInternalState.ZigZagginSurvey;
                direction = Direction.Up;
                break;
        }
    }

    private void TurnRobotAround(boolean rightTurn) throws InterruptedException {
        int turnAngle = 90;
        if (rightTurn)
            turnAngle = -90;

        if(IsReverse)
          turnAngle *= -1;


        moveThread.backward(5);
        moveThread.waitWhileMoving();
        moveThread.turn(-turnAngle);
        moveThread.waitWhileMoving();
        moveThread.forward(config.zigzagWidth);
        //Handle detect left right border
        moveThread.repeatWhileMoving(this::checkWhileTurn);
        moveThread.turn(-turnAngle);
        moveThread.waitWhileMoving();
    }


    private void HandleDetectedApollo()throws InterruptedException {
      Logger.specialLog("Handling Detected Trail");
      boolean isFinishApolloMapping =false;
      float moveStep = 3f;
      while(!isFinishApolloMapping)
      {
        while (isThereApollo())
        {
          moveThread.forward(moveStep);
          moveThread.waitWhileMoving();
        }
        //Out of trail turn an find to track

        //Try turn right to find the trail
        Logger.specialLog("Try right");
        float tryDegree=0;
        int turnRightInterval =10;
        float maxTryDegree = 120/turnRightInterval;
        while (!isThereApollo() && tryDegree < maxTryDegree)
        {
          moveThread.turn(turnRightInterval);
          moveThread.waitWhileMoving();
          tryDegree++;
        }

        Logger.specialLog("Try left");
        //try turn left to find the trail
        int turnLeftInterval =-10;
        tryDegree = - maxTryDegree;
        while (!isThereApollo() && tryDegree < maxTryDegree)
        {
          moveThread.turn(turnLeftInterval);
          moveThread.waitWhileMoving();
          tryDegree++;
        }
        isFinishApolloMapping = !isThereApollo();
      }

      if(isFinishApolloMapping)
        internalState = AutoSurveyingInternalState.BackToHome;
    }

  private void HandleDetectedTrail()throws InterruptedException {

        Logger.specialLog("Handling Detected Trail");
        boolean isFinishTrack =false;
        float moveStep = 3f;
        while(!isFinishTrack)
        {
            while (isThereATrail())
            {
                moveThread.forward(moveStep);
                moveThread.waitWhileMoving();
            }
            //Out of trail turn an find to track


            //Try turn to find the trail
            Logger.specialLog("Try find track");

            float tryDegree=0;
            int turnInterval = 10;
            int turnDirection = direction == Direction.Down ? -1:1;
            if(IsReverse)
            {
              turnDirection = - turnDirection;
            }

            float maxTryDegree = 120/ Math.abs(turnInterval);
            while (!isThereATrail() && tryDegree < maxTryDegree)
            {
                moveThread.turn(turnInterval*turnDirection);
                moveThread.waitWhileMoving();
                tryDegree++;
            }

            //try turn back to find the trail
            tryDegree = - maxTryDegree;
            turnDirection=-turnDirection;
            while (!isThereATrail() && tryDegree < maxTryDegree)
            {
                moveThread.turn(turnInterval*turnDirection);
                moveThread.waitWhileMoving();
                tryDegree++;
            }
            isFinishTrack = !isThereATrail();
        }

        if(isFinishTrack)
            internalState = AutoSurveyingInternalState.BackToLastPosition;
    }

    private void HandleDetectedCrater() {
      Logger.debug("Handling Detected Crater");
    }

    private boolean isThereAnObject() {
      return sensorState.getUltraReadingCm() < config.obstacleAvoidDistance;
    }

    private boolean isThereABorder() {
      Color color = sensorState.getColourEnum();
      return color == config.colorBorder;
    }

    private boolean isThereACrater()
    {
      return sensorState.getColourEnum() == config.colorCrater;
    }

    private boolean isThereApollo()
    {
      return sensorState.getColourEnum() == config.colorApollo;
    }

    private boolean isThereATrail() {
      Color color = sensorState.getColourEnum();
      return color == config.colorTrail;
    }

}
