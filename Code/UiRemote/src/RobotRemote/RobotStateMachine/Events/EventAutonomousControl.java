package RobotRemote.RobotStateMachine.Events;

import RobotRemote.Models.Enums.EnumCommandManual;

public class EventAutonomousControl {
    private EnumCommandManual command;
    private int distance;
    public EventAutonomousControl(EnumCommandManual command, int distance) {
        this.command = command;
        this.distance = distance;
    }

    public EnumCommandManual getCommand() {
        return command;
    }
    public int getDistance() {
        return distance;
    }
}
