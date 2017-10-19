package RobotRemote.RobotStateMachine.States.AutoMode;

import RobotRemote.RobotServices.Connection.RobotConnectionService;
import RobotRemote.RobotServices.Movement.IMovementService;
import RobotRemote.Shared.AppStateRepository;
import RobotRemote.Shared.RobotConfiguration;
import lejos.robotics.navigation.Pose;

import java.util.concurrent.Callable;

public class MovingServiceWrapper
{
  IMovementService movementService;
  public Boolean AllowExecute;
  public MovingServiceWrapper(IMovementService movementService)
  {
    this.movementService = movementService;
    AllowExecute = true;
  }

  public void stop()
  {
    movementService.stop();
  }

  public void forward(Callable repeatThis) throws InterruptedException
  {
    if(AllowExecute)
    {
      movementService.forward();
      movementService.repeatWhileMoving(repeatThis);
    }

  }

  public void forward(float dist_cm) throws InterruptedException
  {
    if(AllowExecute)
    {
      movementService.forward(dist_cm);
      movementService.waitWhileMoving();
    }
  }

  public void forward(float dist_cm, Callable repeatThis) throws InterruptedException
  {
    if(AllowExecute)
    {
      movementService.forward(dist_cm);
      movementService.repeatWhileMoving(repeatThis);
    }
  }
  public void backward(float dist_cm,Callable repeatThis) throws InterruptedException
  {

    if(AllowExecute)
    {
      movementService.backward(dist_cm);
      movementService.repeatWhileMoving(repeatThis);
    }
  }

  public void backward(float dist_cm) throws InterruptedException
  {

    if(AllowExecute)
    {
      movementService.backward(dist_cm);
      movementService.waitWhileMoving();
    }
  }
  public void turn(int degrees) throws InterruptedException
  {
    if(AllowExecute)
    {
      movementService.turn(degrees);
      movementService.waitWhileMoving();
    }
  }

  public  Pose GetCurrentPose()
  {
    return  movementService.GetCurrentPose();
  }

}
