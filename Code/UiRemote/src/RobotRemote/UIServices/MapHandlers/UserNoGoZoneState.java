package RobotRemote.UIServices.MapHandlers;

import RobotRemote.Models.MapPoint;
import lejos.utility.Matrix;

import java.util.ArrayList;
import java.util.List;

public class UserNoGoZoneState {
  private Matrix ngzMatrix;
  private List<MapPoint> ngzPointCollections;

  public UserNoGoZoneState(int ngzRows, int ngzCols) {
    ngzMatrix = new Matrix(ngzRows,ngzCols);
    ngzPointCollections = new ArrayList<MapPoint>();
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

  public void AddNgzPoint(MapPoint newNgzPoint) {
    this.ngzPointCollections.add(newNgzPoint);
  }

  public List<MapPoint> GetNgzPoints() {
    List<MapPoint> ngzCopy = new ArrayList<>();
    for(MapPoint ngzPoint: ngzPointCollections) {
      ngzCopy.add(ngzPoint);
    }
    ngzCopy.add(ngzPointCollections.get(0));
    return ngzCopy;
  }

  public void setNgzPoints(List<MapPoint> ngzPoints) {
    this.ngzPointCollections = ngzPoints;
  }
}

