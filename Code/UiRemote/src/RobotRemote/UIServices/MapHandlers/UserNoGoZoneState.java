package RobotRemote.UIServices.MapHandlers;

import lejos.utility.Matrix;

public class UserNoGoZoneState {
  private Matrix ngzMatrix;

  public UserNoGoZoneState(int ngzRows, int ngzCols) {
    ngzMatrix = new Matrix(ngzRows,ngzCols);
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

  void switchNgzCell(int ngzRow, int ngzCol) {
    try {
      double currentValue = ngzMatrix.get(ngzRow, ngzCol);
      if(currentValue == 0)
        selectNgzCell(ngzRow, ngzCol);
      else
        deselectNgzCell(ngzRow, ngzCol);
    }catch (Exception e) {
    }
  }

  public int countGridRows() {
    return this.ngzMatrix.getRowDimension();
  }

  public int countGridCols() {
    return this.ngzMatrix.getColumnDimension();
  }
}

