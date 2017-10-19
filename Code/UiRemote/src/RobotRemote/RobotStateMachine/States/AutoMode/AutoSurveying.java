package RobotRemote.RobotStateMachine.States.AutoMode;

import RobotRemote.Models.MapPoint;
import RobotRemote.RobotServices.Sensors.SensorsState;
import RobotRemote.RobotStateMachine.IModeState;
import RobotRemote.Shared.*;
import RobotRemote.UIServices.MapHandlers.NgzUtils;
import com.google.common.eventbus.EventBus;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class AutoSurveying implements IModeState
{
  enum AutoSurveyingInternalState
  {
    BackToLastPosition, ZigZagingSurvey, DetectedObject, CraterDetected, PathDetected, BorderDetected, BackToHome, ApolloDetected, NGZDetected
  }

  enum Direction
  {
    Up, Down, Left, Right
  }

  private ServiceManager sm;
  private EventBus eventBus;
  private SensorsState sensorState;
  private ThreadLoop threadLoop;
  private RobotConfiguration config;
  private AutoSurveyingInternalState internalState;
  private Direction direction;
  private boolean isReverse;
  private MapPoint LastPoint;
  private AutoSurveyUtil util;
  private MovingServiceWrapper moveThread;

  public AutoSurveying(ServiceManager sm)
  {
    this.sm = sm;
    this.eventBus = sm.getEventBus();
    this.sensorState = sm.getAppState().getSensorsState();
    this.threadLoop = sm.getRobotStateMachineThread();
    this.moveThread = new MovingServiceWrapper(sm.getMovementService());
    this.config = new RobotConfiguration();
    this.LastPoint = new MapPoint(0, 0);
    this.isReverse = false;
    this.direction = Direction.Up;
    this.util = new AutoSurveyUtil(sm);
  }

  public void Enter()
  {
    this.eventBus.register(this);
    Logger.log("ENTER AUTOSURVEYING STATE...");
    internalState = AutoSurveyingInternalState.BackToLastPosition;
    this.threadLoop.StartThread(() ->
    {
      LoopThis();
      return null;
    }, 100);
  }

  public void Leave()
  {
    this.eventBus.unregister(this);
    this.threadLoop.StopThread();
    this.moveThread.stop();
    this.LastPoint = this.sm.getAppState().getLocationState().GetCurrentPosition();
    Logger.log("LEAVE AUTOSURVEYING STATE...");
  }

  private void LoopThis() throws InterruptedException
  {
    Logger.specialLog("State: " + internalState);
    moveThread.AllowExecute=true;
    switch (internalState)
    {
      case BackToLastPosition:
        handleBackToLastPosition();
        break;
      case ZigZagingSurvey:
        handleZigZagAcrossMap();
        break;
      case DetectedObject:
        handleDetectedObject();
        break;
      case BorderDetected:
        handleDetectedBorder();
        break;
      case PathDetected:
        handleDetectedTrail();
        break;
      case ApolloDetected:
        handleDetectedTrail();
        break;
      case NGZDetected:
        handleDetectedNGZ();
        break;
    }
  }

  private void turnBackToLastDirection(Direction lastDirection) throws InterruptedException
  {
    int initDegree = lastDirection == Direction.Up ? -180 : 0;
    int turnBackDegree = (initDegree - (int) moveThread.GetCurrentPose().getHeading()) % 360;
    moveThread.turn(turnBackDegree);
    internalState = AutoSurveyingInternalState.ZigZagingSurvey;
  }

  private void handleBackToLastPosition() throws InterruptedException
  {
    //for now just turn back to up direction
    Logger.specialLog("Handling Back To Last Posistion");
    //    TODO: Need to handle back to previous state after obstacle/NZG/crater/border avoidance finished
    //    moveThread.gotoPoint((int)LastPoint.x,(int)LastPoint.y);
    //    moveThread.repeatWhileMoving(this::checkSurroundings);
    //If  last time move up then turn back to move up otherwise move down;
    turnBackToLastDirection(direction);
  }

  private void handleZigZagAcrossMap() throws InterruptedException
  {
    // Move leftwards across the map
    moveThread.forward(this::checkSurroundings);
  }

  private Object checkSurroundings() throws InterruptedException
  {

    if (util.isThereABorder())
    {
      moveThread.stop();
      moveThread.AllowExecute =false;
      internalState = AutoSurveyingInternalState.BorderDetected;
      Logger.specialLog("checkSurroundings: Detected Border - Color: " + sensorState.getColourEnum().toString());
    }
    else if (util.isThereAnObject())
    {
      moveThread.stop();
      moveThread.AllowExecute =false;
      internalState = AutoSurveyingInternalState.DetectedObject;
      Logger.specialLog("checkSurroundings: Detected Object");
    }
    else if (util.isThereATrail())
    {
      moveThread.stop();
      moveThread.AllowExecute =false;
      internalState = AutoSurveyingInternalState.PathDetected;
      Logger.specialLog("checkSurroundings: Trail Detected - Color: " + sensorState.getColourEnum());
    }
    else if (util.isThereANGZ())
    {
      Logger.specialLog("checkSurroundings: NGZDetected Detected - Color: " + sensorState.getColourEnum());
      moveThread.stop();
      moveThread.AllowExecute =false;
      internalState = AutoSurveyingInternalState.NGZDetected;
    }

    return null;
  }

  private void handleDetectedObject() throws InterruptedException
  {
    Logger.specialLog("Handling Detected Object going left");
    util.RegisterObjectDetected();
    moveThread.turn(90);
    moveThread.forward(15);
    moveThread.turn(-90);
    moveThread.forward(40);
    moveThread.turn(-90);
    moveThread.forward(15);
    moveThread.turn(90);
    internalState = AutoSurveyingInternalState.ZigZagingSurvey;
  }

  private void handleDetectedNGZ() throws InterruptedException
  {
    Direction currentDir = util.getCurrentDirection();
    if(currentDir == Direction.Up || currentDir == Direction.Down)
    {
      int padding = 5;
      int dir = 1;
      Rectangle ngzBoundingBox = NgzUtils.getInterceptNgzArea(sm.getAppState(), config);
      MapPoint robotLocation = sm.getAppState().getLocationState().GetCurrentPosition();
      if (ngzBoundingBox == null)
        return;

      if (direction == Direction.Down)
      {
        dir *= -1;
        ngzBoundingBox.x += ngzBoundingBox.width;
      }

      if (isReverse)
      {
        dir *= -1;
        ngzBoundingBox.y += ngzBoundingBox.height;
      }

      int yAdding = (int) robotLocation.y - ngzBoundingBox.y;
      if (isReverse)
      {
        yAdding *= -1;
      }
      double moveY = yAdding + config.robotPhysicalLength / 2 + padding;
      moveThread.backward(3);
      moveThread.turn(90 * dir);
      moveThread.forward((float) moveY,this::checkSurroundings);
      moveThread.turn(-90 * dir);
      //move over width of NGZ
      double xAdding = Math.abs((robotLocation.x - ngzBoundingBox.x));
      double moveX = xAdding + config.robotPhysicalLength / 2 + padding;
      moveThread.forward((float) moveX,this::checkSurroundings);
      //TODO: Change to check subrounding
      moveThread.turn(-90 * dir);
      moveThread.forward((float) moveY,this::checkSurroundings);
      moveThread.turn(90 * dir);
    }
    else {
      moveThread.backward(3);
      int randomNum = ThreadLocalRandom.current().nextInt();
      if(randomNum%2==0) //turn right
      {
        moveThread.turn(90);
      }
      else
      {

        moveThread.turn(-90);
      }
    }
    direction = util.getCurrentDirection();
    internalState = AutoSurveyingInternalState.ZigZagingSurvey;
  }

  private void handleDetectedBorder() throws InterruptedException
  {
    Logger.debug("Handling Detected Border going left");
    direction = util.getCurrentDirection();
    switch (direction)
    {
      case Up:
        turnRobotAround(true);
        internalState = AutoSurveyingInternalState.ZigZagingSurvey;
        direction = Direction.Down;
        break;
      case Down:
        turnRobotAround(false);
        internalState = AutoSurveyingInternalState.ZigZagingSurvey;
        direction = Direction.Up;
        break;
      case Right:
      case Left:
        moveThread.backward(5);
        turnBackToLastDirection(Direction.Up);
        internalState = AutoSurveyingInternalState.ZigZagingSurvey;
        direction = Direction.Up;
        isReverse = !isReverse;
        break;
    }
  }

  private void turnRobotAround(boolean rightTurn) throws InterruptedException
  {
    int turnAngle = 90;
    if (rightTurn)
      turnAngle = -90;

    if (isReverse)
      turnAngle *= -1;

    moveThread.backward(5);
    moveThread.turn(-turnAngle);
    moveThread.forward(config.zigzagWidth,this::checkSurroundings);
    //Handle detect left right border
    moveThread.turn(-turnAngle);
  }

  private void handleDetectedTrail() throws InterruptedException
  {

    Logger.specialLog("Handling Detected Trail");
    boolean isFinishTrack = false;
    float moveStep = 3f;
    while (!isFinishTrack)
    {
      while (util.isThereATrail())
      {
        moveThread.forward(moveStep);
      }
      //Out of trail turn an find to track


      //Try turn to find the trail
      Logger.specialLog("Try find track");

      float tryDegree = 0;
      int turnInterval = 10;
      int turnDirection = direction == Direction.Down ? -1 : 1;
      if (isReverse)
      {
        turnDirection = -turnDirection;
      }

      float maxTryDegree = 120 / Math.abs(turnInterval);
      while (!util.isThereATrail() && tryDegree < maxTryDegree)
      {
        moveThread.turn(turnInterval * turnDirection);
        tryDegree++;
      }

      //try turn back to find the trail
      tryDegree = -maxTryDegree;
      turnDirection = -turnDirection;
      while (!util.isThereATrail() && tryDegree < maxTryDegree)
      {
        moveThread.turn(turnInterval * turnDirection);
        tryDegree++;
      }
      isFinishTrack = !util.isThereATrail();
    }

    if (isFinishTrack)
      internalState = AutoSurveyingInternalState.BackToLastPosition;
  }

  private void HandleDetectedCrater()
  {
    Logger.debug("Handling Detected Crater");
  }

  private void HandleDetectedApollo() throws InterruptedException
  {
    Logger.specialLog("Handling Detected Trail");
    boolean isFinishApolloMapping = false;
    float moveStep = 3f;
    while (!isFinishApolloMapping)
    {
      while (util.isThereApollo())
      {
        moveThread.forward(moveStep);
      }
      //Out of trail turn an find to track

      //Try turn right to find the trail
      Logger.specialLog("Try right");
      float tryDegree = 0;
      int turnRightInterval = 10;
      float maxTryDegree = 120 / turnRightInterval;
      while (!util.isThereApollo() && tryDegree < maxTryDegree)
      {
        moveThread.turn(turnRightInterval);
        tryDegree++;
      }

      Logger.specialLog("Try left");
      //try turn left to find the trail
      int turnLeftInterval = -10;
      tryDegree = -maxTryDegree;
      while (!util.isThereApollo() && tryDegree < maxTryDegree)
      {
        moveThread.turn(turnLeftInterval);
        tryDegree++;
      }
      isFinishApolloMapping = !util.isThereApollo();
    }

    if (isFinishApolloMapping)
      internalState = AutoSurveyingInternalState.BackToHome;
  }

}
