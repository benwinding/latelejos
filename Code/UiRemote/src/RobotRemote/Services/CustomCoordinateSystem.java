package RobotRemote.Services;

import lejos.robotics.navigation.Pose;

public class CustomCoordinateSystem implements ICustomCoordinateSystem {
    public CustomCoordinateSystem(float xInit, float yInit, float thetaInit) {
        RobotMoveService.IsDirty =true;
        globalPose = new Pose();
        globalPose.setLocation(xInit, yInit);
        globalPose.setHeading(thetaInit);
    }

    @Override
    public Pose GetGlobalPose() {
        return globalPose;
    }
    private Pose globalPose;

    @Override
    public void GoingStraight(float distance) {
        RobotMoveService.IsDirty = true;
        globalPose.moveUpdate(distance);
    }

    @Override
    public void ChangingHeading(float angle) {
        RobotMoveService.IsDirty = true;
        float currentHeading = globalPose.getHeading();
        globalPose.setHeading(currentHeading + angle);
    }
}
