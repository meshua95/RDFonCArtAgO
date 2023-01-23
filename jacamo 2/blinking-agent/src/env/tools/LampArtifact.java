package tools;

import cartago.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

public class LampArtifact extends Artifact {

	SemanticEnvironment environment;
	/*PROPERTIES*/
	private boolean on;
	private final String statePropertyName = "state";

	/*EVENTS*/
	private final String isOnEvent = "is_on";
	private final String isOffEvent = "is_off";


	public void init(String id, boolean isOn) throws Exception {
		this.environment = SemanticEnvironment.getInstance();
		String className = this.getClass().getSimpleName();
		environment.addOwlObject(className, id);
		log("creating...");
		this.on = isOn;
		defineObsProperty(statePropertyName, this.on); //define a new property that can be observed by agents
		log("created");

		environment.addDataProperty(className, id, statePropertyName, on);

		availableOperations();
		//log(environment.printAllStatement());
	}

	@OPERATION void getCurrentState(OpFeedbackParam<Boolean> state) {
		log("getting the state...");
		state.set(this.on);
		log("state retrieved.");
	}
	@LINK void switchOn() { this.switchTo(true); }
	@LINK void switchOff() { this.switchTo(false); }

	private void switchTo(boolean on) {
		log("switching " + (on ? "on" : "off") + "...");
		this.on = on;
		ObsProperty state = getObsProperty(statePropertyName);	//get the object modelling the observable property "state"
		state.updateValue(this.on);
		if(this.on){
			environment.removeEvent(isOffEvent, this.getClass().getSimpleName());
			environment.addSignaledEvent(isOnEvent, this.getClass().getSimpleName());
			signal(isOnEvent);
		} else {
			environment.removeEvent(isOnEvent, this.getClass().getSimpleName());
			environment.addSignaledEvent(isOffEvent, this.getClass().getSimpleName());
			signal(isOffEvent);
		}
		log("switched " + (on ? "on" : "off") + ".");
	}

	private void availableOperations(){
		String className = this.getClass().getSimpleName();
		Method[] methods = this.getClass().getDeclaredMethods();
		for (Method method : methods) {
			Annotation[] annotations = method.getAnnotations();
			for(Annotation ann: annotations){
				if(ann.annotationType().getSimpleName().equals("OPERATION")){
					environment.addOperation(method.getName(), className);
				}

			}
		}

	}
}