package tools;

import cartago.*;
import semanticDefinition.SemanticArtifact;

import java.util.Timer;
import java.util.TimerTask;

public class LampArtifact extends SemanticArtifact {

	/*PROPERTIES*/
	private boolean on;
	private final String statePropertyName = "stateOn";

	public void init(boolean isOn) {
		super.init(this, "Lamp", this.getId().getName());
		this.on = isOn;
		defineObsProperty(statePropertyName, "boolean", this.on); //define a new property that can be observed by agents
	}

	@OPERATION void lampState(OpFeedbackParam<Boolean> state) {
		state.set(this.on);
	}

	/*OPERATIONS*/
	@OPERATION void switchOn() {
		this.on = true;
		ObsProperty state = getObsProperty("stateOn");
		state.updateValue(this.on);
		signal("is_on");
	}

	@OPERATION void switchOff() {
		this.on = false;
		ObsProperty state = getObsProperty("stateOn");
		state.updateValue(this.on);
		signal("is_off");
	}

	public void dispose(){
		super.dispose();
	}

}