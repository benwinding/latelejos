package RobotRemote.RobotStateMachine;

import RobotRemote.Helpers.Logger;
import RobotRemote.Models.Events.EventChangeRobotCommand;
import RobotRemote.Repositories.AppStateRepository;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class RobotCommandListener {
  private ModeObjectAvoidance modeObjectAvoidance;
  private ModeAutoMapping modeAutomapping;
  private RobotCommandState robotCommandState;

  public RobotCommandListener(AppStateRepository appStateRepository, EventBus eventBus) {
    eventBus.register(this);
    this.robotCommandState = appStateRepository.getStateMachineState();
    this.modeAutomapping = new ModeAutoMapping(eventBus,appStateRepository);
    this.modeObjectAvoidance = new ModeObjectAvoidance();
  }

  @Subscribe
  public void OnChangeMode(EventChangeRobotCommand event) {
    Logger.log("Received EventChangeRobotCommand: " + event.getOperationMode());
    robotCommandState.setCurrentState(event.getOperationMode());
    switch (event.getOperationMode()) {
      case ManualMode:
        this.modeObjectAvoidance.kill();
        this.modeAutomapping.kill();
        break;
      case AutoMode:
        this.modeAutomapping.start();
        break;
      case AvoidanceMode:
        this.modeAutomapping.kill();
        this.modeObjectAvoidance.start();
        break;
      case Waiting:
        this.modeAutomapping.kill();
        this.modeObjectAvoidance.start();
      default:
        break;
    }
  }
}
