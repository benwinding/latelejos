package RobotRemote.UI;

import RobotRemote.Models.Enums.EnumCommandManual;

public class UiState {
  private EnumCommandManual currentCommand;

  public UiState(EnumCommandManual initialCommand) {
    currentCommand = initialCommand;
  }

  public void setCurrentCommand(EnumCommandManual currentCommand) {
    this.currentCommand = currentCommand;
  }

  public EnumCommandManual getCurrentCommand() {
    return currentCommand;
  }
}
