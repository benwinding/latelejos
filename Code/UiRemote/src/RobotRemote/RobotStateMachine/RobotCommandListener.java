package RobotRemote.RobotStateMachine;

import RobotRemote.Helpers.Logger;
import RobotRemote.Models.Events.EventChangeRobotCommand;
import RobotRemote.Repositories.AppStateRepository;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class RobotCommandListener {
  private RStateObjectAvoidance RStateObjectAvoidance;
  private RStateAutoMapping RStateAutomapping;
  private RobotCommandState robotCommandState;

  public RobotCommandListener(AppStateRepository appStateRepository, EventBus eventBus) {
    eventBus.register(this);
    this.robotCommandState = appStateRepository.getStateMachineState();
    this.RStateAutomapping = new RStateAutoMapping(eventBus,appStateRepository);
    this.RStateObjectAvoidance = new RStateObjectAvoidance();
  }

  @Subscribe
  public void OnChangeMode(EventChangeRobotCommand event) {
    Logger.log("Received EventChangeRobotCommand: " + event.getOperationMode());
    robotCommandState.setCurrentState(event.getOperationMode());
    switch (event.getOperationMode()) {
      case ManualMode:
        this.RStateObjectAvoidance.kill();
        this.RStateAutomapping.kill();
        break;
      case AutoMode:
        this.RStateAutomapping.start();
        break;
      case AvoidanceMode:
        this.RStateAutomapping.kill();
        this.RStateObjectAvoidance.start();
        break;
      case Waiting:
        this.RStateAutomapping.kill();
        this.RStateObjectAvoidance.start();
      default:
        break;
    }
  }
}
