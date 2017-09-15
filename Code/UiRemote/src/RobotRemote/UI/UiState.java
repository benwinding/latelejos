package RobotRemote.UI;

import RobotRemote.Models.Enums.EnumCommandManual;

public class UiState {
  private EnumCommandManual currentCommand;

  public void setCurrentCommand(EnumCommandManual currentCommand) {
    this.currentCommand = currentCommand;
  }

  public EnumCommandManual getCurrentCommand() {
    return currentCommand;
  }
}
