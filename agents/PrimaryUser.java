package agents;
import model.Signal;

public class PrimaryUser {
	
	public Signal createAndSend(int length){
		Signal s = new Signal(length);
		return s;
	}
}


