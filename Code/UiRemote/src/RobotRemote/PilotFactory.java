package RobotRemote;

import lejos.hardware.motor.Motor;
import lejos.remote.ev3.RemoteRequestEV3;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.ArcRotateMoveController;
import lejos.robotics.navigation.MovePilot;

import static lejos.robotics.navigation.MoveController.WHEEL_SIZE_EV3;

public class PilotFactory {
  public static ArcRotateMoveController GetPilot() {
    ArcRotateMoveController pilot;
    try {
      RemoteRequestEV3 brick = RobotConnectionManager.GetBrick();
      pilot = brick.createPilot(WHEEL_SIZE_EV3, 10, "A", "B");
      pilot.setLinearSpeed(5);
      pilot.setAngularSpeed(30);
      pilot.setAngularAcceleration(10);
      return pilot;
    } catch (Exception e) {
      Logger.Log("Pilot Factory: Unable to get pilot from ev3, using test pilot");
      return null;
    }
  }
}
