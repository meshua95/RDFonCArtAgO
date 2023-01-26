package tools;

import cartago.*;

public class LampArtifact extends SemanticArtifact {

	/*PROPERTIES*/
	private boolean on;
	private final String statePropertyName = "stateOn";


	public void init(String id, boolean isOn) {
		super.init(this, id);
		this.on = isOn;
		defineObsProperty(statePropertyName, this.on); //define a new property that can be observed by agents

		log("\n \n MODEL \n" + printModel());
	}

	@OPERATION void switchOn() {
		this.switchTo(true);
	}

	@OPERATION void switchOff() {
		this.switchTo(false);
	}

	private void switchTo(boolean on) {
		this.on = on;
		ObsProperty state = getObsProperty(statePropertyName);	//get the object modelling the observable property "state"
		state.updateValue(this.on);
		if(this.on){
			/*EVENTS*/
			String isOnEvent = "is_on";
			signal(isOnEvent);
		} else {
			String isOffEvent = "is_off";
			signal(isOffEvent);
		}
	}

}