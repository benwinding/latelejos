package RobotRemote.Repositories.State;

import lejos.utility.Matrix;

public class UserNoGoZoneState {
  private Matrix ngzMatrix;

  public UserNoGoZoneState(int ngzRows, int ngzCols) {
    ngzMatrix = new Matrix(ngzCols,ngzCols);
  }

  public Matrix getNgzMatrix() {
    return ngzMatrix;
  }

  public void selectNgzCell(int ngzRow, int ngzCol) {
    ngzMatrix.set(ngzRow, ngzCol, 1);
  }

  public void deselectNgzCell(int ngzRow, int ngzCol) {
    ngzMatrix.set(ngzRow, ngzCol, 0);
  }
}

