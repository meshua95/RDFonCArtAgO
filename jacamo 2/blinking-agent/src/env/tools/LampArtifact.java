package tools;

import cartago.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class LampArtifact extends SemanticArtifact {

	/*PROPERTIES*/
	private boolean on;
	private final String statePropertyName = "state";

	/*EVENTS*/
	private final String isOnEvent = "is_on";
	private final String isOffEvent = "is_off";


	public void init(String id, boolean isOn) {
		super.init(this.getClass().getSimpleName(), id);
		this.on = isOn;
		defineObsProperty(statePropertyName, this.on); //define a new property that can be observed by agents

		super.availableOperations(this);
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
			signal(isOnEvent);
		} else {
			signal(isOffEvent);
		}
	}

}