package RobotRemote.Repositories;

import lejos.utility.Matrix;

public class UserNoGoZoneState {
  private Matrix ngzMatrix;

  UserNoGoZoneState(int ngzRows, int ngzCols) {
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

