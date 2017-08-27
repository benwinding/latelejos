package RobotRemote.Models.Interfaces;

import lejos.robotics.navigation.Pose;

/**
 * RobotCoordinateSystemInterface
 *
 * This interface is to be used by the robot controller to
 * calculate the local movements in relation to the global
 * coordinate system.
 */

public interface RobotCoordinateSystemInterface {
    // Returns the global position of the robot in a Pose object
    Pose GetGlobalPose();
    // Called when the robot is going forward some distance
    void GoingStraight(float distance);
    // Called when the robot is changing heading some degree
    void ChangingHeading(float angle);
}
