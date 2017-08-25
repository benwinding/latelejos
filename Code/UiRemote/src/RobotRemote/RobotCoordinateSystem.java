package RobotRemote;

import lejos.robotics.navigation.Pose;

public class RobotCoordinateSystem implements RobotCoordinateSystemInterface {
    @Override
    public Pose GetGlobalPose() {
        return globalPose;
    }
    private Pose globalPose = new Pose();

    @Override
    public void GoingStraight(float distance) {
        globalPose.moveUpdate(distance);
    }

    @Override
    public void ChangingHeading(float angle) {
        float currentHeading = globalPose.getHeading();
        globalPose.setHeading(currentHeading + angle);
    }
}
