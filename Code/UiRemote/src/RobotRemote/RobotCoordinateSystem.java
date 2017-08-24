package RobotRemote;

import lejos.robotics.geometry.Point;
import lejos.robotics.navigation.Pose;

public class RobotCoordinateSystem implements RobotCoordinateSystemInterface {
    @Override
    public Pose GetGlobalPose() {
        return null;
    }

    @Override
    public Point GetForward(float distance) {
        return null;
    }

    @Override
    public Point GetBackward(float distance) {
        return null;
    }

    @Override
    public float GetHeading(float angle) {
        return 0;
    }
}
