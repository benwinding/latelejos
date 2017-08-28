package RobotRemote;

import RobotRemote.Models.Interfaces.RobotCoordinateSystemInterface;
import lejos.robotics.navigation.Pose;

public class RobotCoordinateSystem implements RobotCoordinateSystemInterface {
    public RobotCoordinateSystem(float xInit, float yInit) {
        RobotMotorManager.IsDirty =true;
        globalPose = new Pose();
        globalPose.setLocation(xInit, yInit);
    }

    @Override
    public Pose GetGlobalPose() {
        return globalPose;
    }
    private Pose globalPose;

    @Override
    public void GoingStraight(float distance) {
        RobotMotorManager.IsDirty =true;
        globalPose.moveUpdate(distance);
    }

    @Override
    public void ChangingHeading(float angle) {
        RobotMotorManager.IsDirty =true;
        float currentHeading = globalPose.getHeading();
        globalPose.setHeading(currentHeading - angle);
    }
}
