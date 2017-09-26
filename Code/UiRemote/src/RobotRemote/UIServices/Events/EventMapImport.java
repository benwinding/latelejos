package RobotRemote.UIServices.Events;

import java.io.File;

public class EventMapImport {
  private File selectedMapFile;
  public EventMapImport(File selectedMapFile) {
    this.selectedMapFile = selectedMapFile;
  }
  public File getSelectedMapFile() {
    return selectedMapFile;
  }

}
