interface Controller
{
	public void connect(Gui gui);
	
	//init the position Delay function for processing next command
	public void init(int time);
	
	public void moveForward();
	
	public void turnLeft();
	
	public void turnRight();
	
	public void movebackward();
	
	public void stop();
	
		
}


