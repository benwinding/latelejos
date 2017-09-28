package RobotRemote.UIServices.Events;

import java.io.File;

public class EventMapExport {
  private File selectedExportMapFile;

  public EventMapExport(File selectedExportMapFile) {
    this.selectedExportMapFile = selectedExportMapFile;
  }

  public File getSelectedExportMapFile() {
    return selectedExportMapFile;
  }
}
