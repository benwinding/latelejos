package RobotRemote.Services.RobotCommander;

import RobotRemote.Helpers.Logger;
import RobotRemote.Models.Events.EventChangeOperationMode;
import RobotRemote.Models.Events.EventRobotmode;
import RobotRemote.Repositories.AppStateRepository;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class RobotCommanderService {
  private ModeObjectAvoidance modeObjectAvoidance;
  private ModeAutoMapping modeAutomapping;
  private RobotCommanderState robotCommanderState;

  public RobotCommanderService(AppStateRepository appStateRepository, EventBus eventBus) {
    eventBus.register(this);
    this.robotCommanderState = appStateRepository.getStateMachineState();
    this.modeAutomapping = new ModeAutoMapping();
    this.modeObjectAvoidance = new ModeObjectAvoidance();
  }

  @Subscribe
  public void OnChangeMode(EventChangeOperationMode event) {
    Logger.log("Received EventChangeOperationMode: " + event.getOperationMode());
    robotCommanderState.setCurrentState(event.getOperationMode());
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

  @Subscribe
  public void OnEventRobotmode(EventRobotmode event) {
    Logger.log("Robot mode changed");
  }
}
