package RobotRemote.Models;

import RobotRemote.Helpers.Logger;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.net.URL;
import java.util.NoSuchElementException;

public class RobotConfig {
  public float initX = 100;
  public float initY = 100;
  public float initTheta = 0;
  public int ngzRows = 10;
  public int ngzCols = 10;
  public float mapInitZoom = 1;
  public float mapW = 300;
  public float mapH = 500;

  public RobotConfig GetConfiguration(String configFilePath) {
    RobotConfig robotConfig = new RobotConfig();
    try
    {
      Configurations configs = new Configurations();
      URL file = getClass().getResource(configFilePath);
      Configuration config = configs.properties(file);
      // access configuration properties
      initX = config.getFloat("location.x");
      initY = config.getFloat("location.y");
      initTheta = config.getFloat("location.heading");
      Logger.Log("Configuration Successfully Read");
    }
    catch (ConfigurationException cex)
    {
      Logger.Log("Some problem with the configuration");
    }
    catch (NoSuchElementException ex) {
      Logger.Log("Configuration Error: " + ex.getLocalizedMessage());
    }
    return robotConfig;
  }
}
