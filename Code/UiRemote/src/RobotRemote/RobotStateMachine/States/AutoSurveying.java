package RobotRemote.RobotStateMachine.States;

import RobotRemote.Models.MapPoint;
import RobotRemote.RobotServices.Movement.IMovementService;
import RobotRemote.RobotServices.Sensors.SensorsState;
import RobotRemote.RobotStateMachine.Events.AutoSurvey.EventAutomapDetectedObject;
import RobotRemote.RobotStateMachine.IModeState;
import RobotRemote.Shared.*;
import RobotRemote.UIServices.MapHandlers.NgzUtils;
import com.google.common.eventbus.EventBus;
import javafx.scene.paint.Color;
import lejos.robotics.navigation.Pose;

import java.awt.*;

public class AutoSurveying implements IModeState {
  enum AutoSurveyingInternalState {
    BackToLastPosition,
    ZigZagingSurvey,
    DetectedObject,
    CraterDetected,
    PathDetected,
    BorderDetected,
    BackToHome,
    ApolloDetected,
    NGZDetected
  }

  enum Direction {
    Up,
    Down
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
  private boolean isReverse;
  private MapPoint LastPoint;
  private RobotConfiguration robotConfiguration;
  public AutoSurveying(ServiceManager sm) {
    this.sm = sm;
    this.eventBus = sm.getEventBus();
    this.sensorState = sm.getAppState().getSensorsState();
    this.threadLoop = sm.getRobotStateMachineThread();
    this.moveThread = sm.getMovementService();
    this.config = new RobotConfiguration();
    this.LastPoint = new MapPoint(0,0);
    this.isReverse = false;
    this.direction = Direction.Up;
    this.robotConfiguration = new RobotConfiguration();
  }

  public void Enter() {
    this.eventBus.register(this);
    Logger.log("ENTER AUTOSURVEYING STATE...");
    internalState = AutoSurveyingInternalState.BackToLastPosition;
    this.threadLoop.StartThread(() -> {
      LoopThis();
      return null;
    }, 100);
  }

  public void Leave() {
    this.eventBus.unregister(this);
    this.threadLoop.StopThread();
    this.moveThread.stop();
    this.LastPoint = this.sm.getAppState().getLocationState().GetCurrentPosition();
    Logger.log("LEAVE AUTOSURVEYING STATE...");
  }

  private void LoopThis() throws InterruptedException {
    Logger.specialLog("State: "+internalState);
    switch (internalState) {
      case BackToLastPosition:
        HandleBackToLastPosition();
        break;
      case ZigZagingSurvey:
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
      case NGZDetected:
        HandleDetectedNGZ();
        break;
    }
  }
  private void HandleDetectedNGZ() throws InterruptedException
  {
    int padding = 5;
    int dir =1;
    Rectangle ngzBoundingBox = NgzUtils.getInterceptNgzArea(sm.getAppState(),config);
    MapPoint robotLocation = sm.getAppState().getLocationState().GetCurrentPosition();
   if(direction == Direction.Down){
     dir*=-1;
     ngzBoundingBox.x += ngzBoundingBox.width;
   }

   if(isReverse)
   {
     dir*=-1;
     ngzBoundingBox.y+= ngzBoundingBox.height;
   }

    int yAdding = (int) robotLocation.y - ngzBoundingBox.y;
    if(isReverse)
    {
      yAdding*=-1;
    }
    double moveY  = yAdding + config.robotPhysicalLength/2+padding;
    moveThread.backward(3);
    moveThread.waitWhileMoving();
    moveThread.turn(90*dir);
    moveThread.waitWhileMoving();
    moveThread.forward((float)moveY);
    moveThread.repeatWhileMoving(this::checkSurroundings);
    if(internalState!= AutoSurveyingInternalState.NGZDetected)
      return;
    moveThread.turn(-90*dir);
    moveThread.waitWhileMoving();
    //move over width of NGZ
    double xAdding =Math.abs((robotLocation.x - ngzBoundingBox.x));
    double moveX =  xAdding+ config.robotPhysicalLength/2+padding;
    moveThread.forward((float)moveX);

    //TODO: Change to check subrounding
    moveThread.repeatWhileMoving(this::checkSurroundings);
    if(internalState!= AutoSurveyingInternalState.NGZDetected)
      return;
    moveThread.turn(-90*dir);
    moveThread.waitWhileMoving();
    moveThread.forward((float)moveY);
    moveThread.repeatWhileMoving(this::checkSurroundings);
    if(internalState!= AutoSurveyingInternalState.NGZDetected)
      return;
    moveThread.turn(90*dir);
    moveThread.waitWhileMoving();


      internalState = AutoSurveyingInternalState.ZigZagingSurvey;
  }

  private void HandleBackToLastPosition() throws InterruptedException {
    //for now just turn back to up direction
    Logger.specialLog("Handling Back To Last Posistion");
    //    //TODO: Need to handle back to previous state after obstacle/NZG/crater/border avoidance finished
    //    moveThread.gotoPoint((int)LastPoint.x,(int)LastPoint.y);
    //    moveThread.repeatWhileMoving(this::checkSurroundings);
    //If  last time move up then turn back to move up otherwise move down;
    int initDegree = direction == Direction.Up ? -180 : 0;
    int turnBackDegree = initDegree - (int)moveThread.GetCurrentPose().getHeading();
    moveThread.turn(turnBackDegree);
    moveThread.waitWhileMoving();
    internalState = AutoSurveyingInternalState.ZigZagingSurvey;
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
      isReverse = !isReverse;
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


    if(isThereANGZ())
    {
      Logger.specialLog("checkSurroundings: NGZDetected Detected - Color: "+ sensorState.getColourEnum());
      moveThread.stop();
      internalState = AutoSurveyingInternalState.NGZDetected;
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
    internalState = AutoSurveyingInternalState.ZigZagingSurvey;
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
        internalState = AutoSurveyingInternalState.ZigZagingSurvey;
        direction = Direction.Down;
        break;
      case Down:
        TurnRobotAround(false);
        internalState = AutoSurveyingInternalState.ZigZagingSurvey;
        direction = Direction.Up;
        break;
    }
  }

  private void TurnRobotAround(boolean rightTurn) throws InterruptedException {
    int turnAngle = 90;
    if (rightTurn)
      turnAngle = -90;

    if(isReverse)
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
      if(isReverse)
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
    if(!sm.getAppState().getSensorsState().getStatusUltra())
      return  false;
    return sensorState.getUltraReadingCm() < config.obstacleAvoidDistance;
  }

  private boolean isThereANGZ()
  {
    Rectangle reg =NgzUtils.getInterceptNgzArea(sm.getAppState(), robotConfiguration);
    if(reg!=null)
    {
      //Check if forward collide
      Pose currentPose =sm.getAppState().getLocationState().GetCurrentPose();
      float currentDistance = currentPose.distanceTo(new lejos.robotics.geometry.Point(reg.x, reg.y));
      currentPose.moveUpdate(3);
      float nexDistance = currentPose.distanceTo(new lejos.robotics.geometry.Point(reg.x, reg.y));
      return  currentDistance>nexDistance;
    }
    return  false;

  }

  private boolean isThereABorder() {


    if(!sm.getAppState().getSensorsState().getStatusColour())
    {
      if(sm.getAppState().getLocationState().GetCurrentPosition().y<0||sm.getAppState().getLocationState().GetCurrentPosition().x<0
          ||sm.getAppState().getLocationState().GetCurrentPosition().y>85||sm.getAppState().getLocationState().GetCurrentPosition().x>60)
        return true;
      return false;
    }

    Color color = sensorState.getColourEnum();
    return color == config.colorBorder;
  }

  private boolean isThereACrater()
  {
    if(!sm.getAppState().getSensorsState().getStatusColour())
      return false;

    return sensorState.getColourEnum() == config.colorCrater;
  }

  private boolean isThereApollo()
  {
    if(!sm.getAppState().getSensorsState().getStatusColour())
      return false;

    return sensorState.getColourEnum() == config.colorApollo;
  }

  private boolean isThereATrail() {

    if(!sm.getAppState().getSensorsState().getStatusColour())
      return false;

    Color color = sensorState.getColourEnum();
    return color == config.colorTrail;
  }

}
