package RobotRemote.UIServices.MapHandlers;

import RobotRemote.Models.MapPoint;
import lejos.utility.Matrix;

import java.util.ArrayList;
import java.util.List;

public class UserNoGoZoneState {
  private Matrix ngzMatrix;
  private List<List<MapPoint>> allNgzSets;

  public UserNoGoZoneState(int ngzRows, int ngzCols) {
    ngzMatrix = new Matrix(ngzRows,ngzCols);
    allNgzSets = new ArrayList<>();
  }

  public Matrix getNgzMatrix() {
    return ngzMatrix;
  }

  private void selectNgzCell(int ngzRow, int ngzCol) {
    ngzMatrix.set(ngzRow, ngzCol, 1);
  }

  private void deselectNgzCell(int ngzRow, int ngzCol) {
    ngzMatrix.set(ngzRow, ngzCol, 0);
  }

  void switchNgzCell(int ngzRow, int ngzCol) {
    try {
      double currentValue = ngzMatrix.get(ngzRow, ngzCol);
      if(currentValue == 0)
        selectNgzCell(ngzRow, ngzCol);
      else
        deselectNgzCell(ngzRow, ngzCol);
    } catch (Exception e) {
    }
  }

  int countGridRows() {
    return this.ngzMatrix.getRowDimension();
  }

  int countGridCols() {
    return this.ngzMatrix.getColumnDimension();
  }

  private List<MapPoint> getCurrentNgzPoints() {
    return this.allNgzSets.get(allNgzSets.size()-1);
  }

  void AddNgzStartPoint(MapPoint newNgzPoint) {
    this.allNgzSets.add(new ArrayList<>());
    AddPointToCurrentList(newNgzPoint);
  }

  void AddNgzMiddlePoint(MapPoint newNgzPoint) {
    AddPointToCurrentList(newNgzPoint);
  }

  private void AddPointToCurrentList(MapPoint newNgzPoint) {
    this.getCurrentNgzPoints().add(newNgzPoint);
  }

  public List<List<MapPoint>> GetNgzPoints() {
    if(this.allNgzSets.size() > 0 && this.getCurrentNgzPoints() != null)
      return this.allNgzSets;
    else
      return new ArrayList<>();
  }

  private void CopyFirstPointToLastPosition(List<MapPoint> ngzCopy) {
    if(ngzCopy != null && ngzCopy.size() > 0)
      ngzCopy.add(ngzCopy.get(0));
  }

  void FinishedNgzSet() {
    if(this.allNgzSets.size() > 0 && this.getCurrentNgzPoints() != null)
      this.CopyFirstPointToLastPosition(this.getCurrentNgzPoints());
  }
}

