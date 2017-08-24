package RobotRemote;

import lejos.robotics.geometry.Point;
import lejos.robotics.navigation.Pose;

/**
 * RobotCoordinateSystemInterface
 *
 * This interface is to be used by the robot controller to
 * calculate the local movements in relation to the global
 * coordinate system.
 */

interface RobotCoordinateSystemInterface {
    Pose GetGlobalPose();

    Point GetForward(float distance);
    Point GetBackward(float distance);
    // Get the relative heading to the robot
    float GetHeading(float angle);
}
