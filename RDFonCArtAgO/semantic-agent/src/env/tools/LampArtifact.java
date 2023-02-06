package tools;

import cartago.*;

public class LampArtifact extends SemanticArtifact {

	/*PROPERTIES*/
	private boolean on;
	private final String statePropertyName = "stateOn";

	public void init(boolean isOn) {
		super.init(this, this.getId().getName());
		this.on = isOn;
		defineObsProperty(statePropertyName, this.on); //define a new property that can be observed by agents
	}

	@OPERATION void lampState(OpFeedbackParam<Boolean> state) {
		state.set(this.on);
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
			String isOnEvent = "is_on";
			signal(isOnEvent);
		} else {
			String isOffEvent = "is_off";
			signal(isOffEvent);
		}
	}

	public void dispose(){
		super.dispose();
	}

}