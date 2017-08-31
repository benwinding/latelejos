package RobotRemote.Services.Listeners.StateMachine;

import lejos.utility.Matrix;

public class UserNoGoZoneState {
  private Matrix ngzMatrix;

  public UserNoGoZoneState(int ngzRows, int ngzCols) {
    ngzMatrix = new Matrix(ngzCols,ngzCols);
  }

  public Matrix getNgzMatrix() {
    return ngzMatrix;
  }

  void selectNgzCell(int ngzRow, int ngzCol) {
    ngzMatrix.set(ngzRow, ngzCol, 1);
  }

  void deselectNgzCell(int ngzRow, int ngzCol) {
    ngzMatrix.set(ngzRow, ngzCol, 0);
  }
}

