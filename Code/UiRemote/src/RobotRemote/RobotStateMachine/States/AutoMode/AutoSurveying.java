package RobotRemote.RobotStateMachine.States.AutoMode;

import RobotRemote.Models.MapPoint;
import RobotRemote.RobotServices.Movement.LocationState;
import RobotRemote.RobotServices.Sensors.SensorsState;
import RobotRemote.RobotStateMachine.Events.Shared.EventEmergencySTOP;
import RobotRemote.RobotStateMachine.IModeState;
import RobotRemote.Shared.*;
import RobotRemote.UIServices.MapHandlers.NgzUtils;
import com.google.common.eventbus.EventBus;
import lejos.robotics.navigation.Pose;
import sun.rmi.runtime.Log;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class AutoSurveying implements IModeState
{



  enum AutoSurveyingInternalState
  {
    BackToLastPosition,
    ZigZagingSurvey,
    DetectedObject,
    CraterDetected,
    PathDetected,
    BorderDetected,
    BackToHome,
    ApolloDetected,
    NGZDetected,
    MissionCompleted,
    SurveyRadiation,
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
  private AutoSurveyingInternalState currentState;
  private boolean isMovingToPoint;
  private Direction direction;
  private boolean isReverse;
  private MapPoint LastPoint;
  private AutoSurveyUtil util;
  private MovingServiceWrapper moveThread;
  private LocationState locationState;
  private  boolean IsOnState;
  private boolean missionAccomplish;
  public AutoSurveying(ServiceManager sm)
  {
    this.IsOnState = false;
    this.sm = sm;
    this.eventBus = sm.getEventBus();
    this.sensorState = sm.getAppState().getSensorsState();
    this.locationState = sm.getAppState().getLocationState();
    this.threadLoop = sm.getRobotStateMachineThread();
    this.moveThread = new MovingServiceWrapper(sm.getMovementService());
    this.config = new RobotConfiguration();
    this.LastPoint = new MapPoint(config.initX, config.initY);
    this.isReverse = false;
    this.isMovingToPoint = false;
    this.direction = Direction.Up;
    this.util = new AutoSurveyUtil(sm);
    this.missionAccomplish =false;
   // Logger.isDisableLog =true;
  }

  public void setMoveToWaypoint(MapPoint point)
  {
    this.LastPoint = point;
    if(currentState == AutoSurveyingInternalState.ZigZagingSurvey)
      moveThread.stop();
    moveThread.AllowExecute=false;
    currentState = AutoSurveyingInternalState.BackToLastPosition;

  }

  public void Enter()
  {
    if(this.IsOnState)
      return;

    this.IsOnState = true;
    this.eventBus.register(this);
    Logger.log("ENTER AUTOSURVEYING STATE...");
    currentState = AutoSurveyingInternalState.BackToLastPosition;
    this.threadLoop.StartThread(() ->
    {
      LoopThis();
      return null;
    }, 100);
  }

  public void Leave()
  {
    if(!this.IsOnState)
        return;
    this.IsOnState = false;
    this.eventBus.unregister(this);
    this.threadLoop.StopThread();
    this.moveThread.stop();
    this.LastPoint = this.sm.getAppState().getLocationState().GetCurrentPosition();
    Logger.log("LEAVE AUTOSURVEYING STATE...");
  }

  private void LoopThis() throws InterruptedException
  {
    Logger.specialLog("State: " + currentState);
    moveThread.AllowExecute=true;
    switch (currentState)
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
        handleApolloDetected();
        break;
      case NGZDetected:
        handleDetectedNGZ();
        break;
      case MissionCompleted:
        Logger.log("I'm Home!!!!");
        eventBus.post(new EventEmergencySTOP());
        break;
      case SurveyRadiation:
        handleSurveyRadiation();
      break;
      case CraterDetected:
        handleCraterDetected();
        break;
    }
  }

  private void handleCraterDetected() throws InterruptedException
  {
    moveThread.backward(5);
    int turnAngle = 90;
    if (direction == Direction.Down)
      turnAngle = -90;
    if (isReverse)
      turnAngle *= -1;
    moveThread.turn(turnAngle,this::checkSurroundings);
    moveThread.forward(config.zigzagWidth,this::checkSurroundings);
    moveThread.turn(-turnAngle,this::checkSurroundings);
    setCurrentState(AutoSurveyingInternalState.ZigZagingSurvey);
  }

  private void handleApolloDetected()
  {
    LastPoint = new MapPoint(config.initX,config.initY);
    missionAccomplish=true;
    setCurrentState(AutoSurveyingInternalState.BackToLastPosition);
  }

  private Object checkOutRadiationZone() throws InterruptedException
  {
    if (!util.isThereRadiation())
    {
      moveThread.stop();
    }
    return null;
  }

  private void handleSurveyRadiation() throws InterruptedException
  {
      while (!util.isThereApollo())
      {
        moveThread.forward(this::checkOutRadiationZone);
        if(util.isThereApollo())
        {
          setCurrentState(AutoSurveyingInternalState.ApolloDetected);
          break;
        }
        else {
          moveThread.backward(3);
          int randomNum = ThreadLocalRandom.current().nextInt(0,180);
          moveThread.turn(randomNum);
        }
      }
  }

  private void turnBackToLastDirection(Direction lastDirection) throws InterruptedException
  {
    //TODO: Need to optimize tunrning degree
    int initDegree =  180;
    switch (lastDirection)
    {
      case Left:
        initDegree = 90;
        break;
      case Down:
        initDegree = 0;
        break;
      case Right:
        initDegree = 270;
        break;
    }

    double turnBackDegree = (initDegree - moveThread.GetCurrentPose().getHeading()) % 360;
    moveThread.turn(turnBackDegree,this::checkSurroundings);
  }

  private void turnRobotAround(boolean rightTurn) throws InterruptedException
  {
    int turnAngle = 90;
    if (rightTurn)
      turnAngle = -90;

    if (isReverse)
      turnAngle *= -1;
    moveThread.turn(-turnAngle,this::checkSurroundings);
    moveThread.forward(config.zigzagWidth,this::checkSurroundings);
    moveThread.turn(-turnAngle,this::checkSurroundings);
  }

  private void handleBackToLastPosition() throws InterruptedException
  {
    Pose currentPose = locationState.GetCurrentPose();
    MapPoint currentLocation = locationState.GetCurrentPosition();

    if(!currentLocation.equals(LastPoint))
    {
      //for now just turn back to up direction
      Logger.specialLog("Handling Back To Last Posistion");
      isMovingToPoint = true;
      float angleToPoint = currentPose.relativeBearing(new lejos.robotics.geometry.Point((float) LastPoint.x, (float) LastPoint.y));
      float distanceToPoint = currentPose.distanceTo(new lejos.robotics.geometry.Point((float) LastPoint.x, (float) LastPoint.y));
      moveThread.turn( angleToPoint,this::checkSurroundings);
      moveThread.forward(distanceToPoint, this::checkSurroundings);
      //If  last time move up then turn back to move up otherwise move down;
    }

    currentLocation = locationState.GetCurrentPosition();
    //Recheck again because may be robot meet NGZ or obstacle before reach destination
    if(currentLocation.equals(LastPoint))
    {
      isMovingToPoint = false;
      if(this.missionAccomplish)
      {
        setCurrentState(AutoSurveyingInternalState.MissionCompleted);
        return;
      }
      setCurrentState(AutoSurveyingInternalState.ZigZagingSurvey);
      turnBackToLastDirection(direction);
    }

  }

  private void handleZigZagAcrossMap() throws InterruptedException
  {
    // Move leftwards across the map
    //Correct direction up or down before move
    if(direction != Direction.Up && direction != Direction.Down)
      direction = Direction.Up;
    turnBackToLastDirection(direction);
    moveThread.forward(this::checkSurroundings);
  }

  private void setCurrentState(AutoSurveyingInternalState state)
  {
      currentState = state;
      if(currentState == AutoSurveyingInternalState.ZigZagingSurvey && isMovingToPoint)
      {
        currentState = AutoSurveyingInternalState.BackToLastPosition;
      }
  }

  private Object checkSurroundings() throws InterruptedException
  {

    if (util.isThereABorder())
    {
      moveThread.stopExecuteCommand();
      setCurrentState(AutoSurveyingInternalState.BorderDetected);
      Logger.specialLog("checkSurroundings: Detected Border - Color: " + sensorState.getColourEnum().toString());
    }
    else if(util.isThereApollo() && !missionAccomplish)
    {
      moveThread.stopExecuteCommand();
      setCurrentState(AutoSurveyingInternalState.ApolloDetected);
    }
    else if(util.isThereRadiation() && !missionAccomplish)
    {
      moveThread.stopExecuteCommand();
      setCurrentState(AutoSurveyingInternalState.SurveyRadiation);
    }
    else if (util.isThereAnObject())
    {
      moveThread.stopExecuteCommand();
      setCurrentState(AutoSurveyingInternalState.DetectedObject);
      Logger.specialLog("checkSurroundings: Detected Object");
    }
    else if (currentState != AutoSurveyingInternalState.PathDetected && util.isThereATrail())
    {
      moveThread.stopExecuteCommand();
      setCurrentState(AutoSurveyingInternalState.PathDetected);
      Logger.specialLog("checkSurroundings: Trail Detected - Color: " + sensorState.getColourEnum());
    }
    else if (util.isThereANGZ())
    {
      Logger.specialLog("checkSurroundings: NGZDetected Detected  ");
      moveThread.stopExecuteCommand();
      setCurrentState(AutoSurveyingInternalState.NGZDetected);
    }
    else if(util.isThereACrater())
    {
      Logger.specialLog("checkSurroundings: Crater Detected");
      moveThread.stopExecuteCommand();
      setCurrentState(AutoSurveyingInternalState.CraterDetected);
    }

    return null;
  }

  private void objectAvoidance(int distance) throws InterruptedException
  {
    moveThread.turn(90);
    moveThread.forward(distance,this::checkSurroundings);
    moveThread.turn(-90);

  }

  private boolean lookForObject() throws InterruptedException
  {
    int lookingDegree =20;
    boolean stillSeeObject =false;
    moveThread.turn(lookingDegree);
    if (util.isThereAnObject())
    {
      stillSeeObject = true;
    }
    moveThread.turn(-lookingDegree);
    moveThread.turn(-lookingDegree);
    if (util.isThereAnObject())
    {
      stillSeeObject = true;
    }
    moveThread.turn(lookingDegree);
    return stillSeeObject;
  }

  private void handleDetectedObject() throws InterruptedException
  {
    Logger.specialLog("Handling Detected Object");
    util.RegisterObjectDetected();
    objectAvoidance(15);
    //Try look around be for keep moving
    boolean stillSeeObject=false;
    do
    {
      stillSeeObject = lookForObject();
      if (stillSeeObject)
      {
        objectAvoidance(5);
      }
    }
    while (stillSeeObject);


    while(util.isThereAnObject());
    if(moveThread.AllowExecute)
    {
      setCurrentState(AutoSurveyingInternalState.ZigZagingSurvey);
    }
  }

  private void handleDetectedNGZ() throws InterruptedException
  {
    Direction currentDir = util.getCurrentDirection();
    if(currentDir == Direction.Up || currentDir == Direction.Down)
    {
      int padding = 2;
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

      double yAdding =  robotLocation.y - ngzBoundingBox.y;
      if (isReverse)
      {
        yAdding *= -1;
      }
      double moveY = yAdding + config.robotPhysicalLength / 2 + padding;
      moveThread.backward(3);
      moveThread.turn(90 * dir,this::checkSurroundings);
      moveThread.forward((float) moveY,this::checkSurroundings);
      moveThread.turn(-90 * dir,this::checkSurroundings);
      //move over width of NGZ
      double xAdding = Math.abs((robotLocation.x - ngzBoundingBox.x));
      double moveX = xAdding + config.robotPhysicalLength / 2 + padding;
      moveThread.forward((float) moveX,this::checkSurroundings);
      //TODO: Change to check subrounding
      moveThread.turn(-90 * dir,this::checkSurroundings);
      moveThread.forward((float) moveY,this::checkSurroundings);
      moveThread.turn(90 * dir,this::checkSurroundings);
    }
    else {
      moveThread.backward(3);
      int randomNum = ThreadLocalRandom.current().nextInt(-90,90);
      moveThread.turn(randomNum,this::checkSurroundings);
      randomNum = ThreadLocalRandom.current().nextInt(5,10);
      moveThread.forward(randomNum,this::checkSurroundings);

    }

    if(moveThread.AllowExecute)
    {
      direction = util.getCurrentDirection();
      setCurrentState(AutoSurveyingInternalState.ZigZagingSurvey);
    }
  }

  private void handleDetectedBorder() throws InterruptedException
  {
    Logger.debug("Handling Detected Border");
    moveThread.backward(5);
    direction = util.getCurrentDirection();
    turnBackToLastDirection(direction);
    switch (direction)
    {
      case Up:
        turnRobotAround(true);
        direction = Direction.Down;
        break;
      case Down:
        turnRobotAround(false);
        direction = Direction.Up;
        break;
      case Right:
      case Left:
        turnBackToLastDirection(Direction.Up);
        direction = Direction.Up;
        isReverse = !isReverse;
        break;
    }

    if(moveThread.AllowExecute)
      setCurrentState(AutoSurveyingInternalState.ZigZagingSurvey);
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
        moveThread.forward(moveStep,this::checkSurroundings);
      }

      //Encounter some obstacle while follow track
      if(!moveThread.AllowExecute)
        break;

      //Out of trail turn an find to track


      //Try turn to find the trail
      Logger.specialLog("Try find track");

      float tryDegree = 0;
      int turnInterval = 5;
      int turnDirection = direction == Direction.Down ? -1 : 1;
      if (isReverse)
      {
        turnDirection = -turnDirection;
      }

      float maxTryDegree = 120 / Math.abs(turnInterval);
      while (!util.isThereATrail() && tryDegree < maxTryDegree)
      {
        moveThread.turn(turnInterval * turnDirection,this::checkSurroundings);
        tryDegree++;
      }
      //try turn back to find the trail
      tryDegree = 0;
      turnDirection = -turnDirection;
      moveThread.turn(turnDirection*120,this::checkSurroundings);
      while (!util.isThereATrail() && tryDegree < maxTryDegree)
      {
        moveThread.turn(turnInterval * turnDirection,this::checkSurroundings);
        tryDegree++;
      }
      isFinishTrack = !util.isThereATrail();
    }

    if (isFinishTrack)
    {
      setCurrentState(AutoSurveyingInternalState.ZigZagingSurvey);
    }
  }

//  private void handleDetectedCrater()
//  {
//    Logger.debug("Handling Detected Crater");
//  }
//
//  private void handleDetectedApollo() throws InterruptedException
//  {
//    Logger.specialLog("Handling Detected Trail");
//    boolean isFinishApolloMapping = false;
//    float moveStep = 3f;
//    while (!isFinishApolloMapping)
//    {
//      while (util.isThereApollo())
//      {
//        moveThread.forward(moveStep);
//      }
//      //Out of trail turn an find to track
//
//      //Try turn right to find the trail
//      Logger.specialLog("Try right");
//      float tryDegree = 0;
//      int turnRightInterval = 10;
//      float maxTryDegree = 120 / turnRightInterval;
//      while (!util.isThereApollo() && tryDegree < maxTryDegree)
//      {
//        moveThread.turn(turnRightInterval);
//        tryDegree++;
//      }
//
//      Logger.specialLog("Try left");
//      //try turn left to find the trail
//      int turnLeftInterval = -10;
//      tryDegree = -maxTryDegree;
//      while (!util.isThereApollo() && tryDegree < maxTryDegree)
//      {
//        moveThread.turn(turnLeftInterval);
//        tryDegree++;
//      }
//      isFinishApolloMapping = !util.isThereApollo();
//    }
//
//    if (isFinishApolloMapping)
//    {
//      setCurrentState(AutoSurveyingInternalState.ZigZagingSurvey);
//    }
//  }

}
