package RobotRemote.Services.Synchronous.SensorService;

import RobotRemote.Helpers.Logger;
import RobotRemote.Repositories.State.SensorsState;
import RobotRemote.Services.Synchronous.Connection.RobotConnectionService;

public class SensorsService implements Runnable{
  private Thread thread;

  private RobotConnectionService connectionService;
  private SensorsState sensorsState;

  public SensorsService(RobotConnectionService connectionService, SensorsState sensorsState) {
    this.connectionService = connectionService;
    this.sensorsState = sensorsState;
  }

  @Override
  public void run() {
    for(int i=0;i<100;i++) {
      sensorsState.setColourReading(i);
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public void start() {
    String threadName = "Sensors Service";
    Logger.LogCrossThread("THREAD: Starting " + threadName);
    if (thread == null) {
      thread = new Thread (this, threadName);
      thread.start ();
    }
  }

  public void kill() {
    if (thread != null)
      thread.interrupt();
  }
}
