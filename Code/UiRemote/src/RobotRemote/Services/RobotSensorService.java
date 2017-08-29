package RobotRemote.Services;


import RobotRemote.Utils.Logger;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;

public class RobotSensorService extends Thread{
    private EV3ColorSensor color;
    private SensorMode colorsensor;
    boolean cancel;

    public void init(){
        color=new EV3ColorSensor(SensorPort.S4);
        colorsensor= color.getRGBMode();
        cancel=false;
    }
    @Override
    public void run() {

        while(!cancel){
            float[] sample=new float[colorsensor.sampleSize()];
            colorsensor.fetchSample(sample, 0);


            Logger.TryToWriteToDemo("R: " + sample[0]+" G: " + sample[1]+" B: " + sample[2]);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        color.close();
    }


    public void terminate() {
        cancel=true;
    }
}
