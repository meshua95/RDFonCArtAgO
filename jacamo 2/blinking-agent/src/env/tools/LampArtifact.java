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
		log(environment.printAllStatement());

		availableOperations();
	}

	@OPERATION void getCurrentState(OpFeedbackParam<Boolean> state) {
		log("getting the state...");
		state.set(this.on);
		log("state retrieved.");
	}
	@OPERATION void switchOn() { this.switchTo(true); }
	@OPERATION void switchOff() { this.switchTo(false); }

	private void switchTo(boolean on) {
		log("switching " + (on ? "on" : "off") + "...");
		this.on = on;
		ObsProperty state = getObsProperty(statePropertyName);	//get the object modelling the observable property "state"
		state.updateValue(this.on); 					//update the value of the observable property "state", triggering the plans of the agents observing this property
		log("switched " + (on ? "on" : "off") + ".");
	}

	private void availableOperations(){
		String className = this.getClass().getSimpleName();
		Method[] methods = this.getClass().getDeclaredMethods();
		for (Method method : methods) {
			log("METHOD ->" + method.getName());
			Annotation[] annotations = method.getAnnotations();
			for(Annotation ann: annotations){
				if(ann.annotationType().getSimpleName().equals("OPERATION")){
					environment.addOperation(method.getName(), className);
				}

			}
		}
		/*
		for (Method method : methods) {
			log("METHOD ->" + method.getName());
			Annotation[] ann = method.getAnnotations();
			for(int i = 0; i < ann.length; i++){
				log("ANN ->" + ann[i].toString());
			}

			//
		}*/
	}
}

/*
public class SemanticLamp extends Artifact{

	@OPERATION void getCurrentState(OpFeedbackParam<Boolean> state) {
		log("getting the state...");
		state.set(this.on);
		log("state retrieved.");
	}

	@OPERATION
	void turnOn(){
		turnState(true);
		//semanticTurnOn();
	}

	@OPERATION
	void turnOff(){
		turnState(false);
		//semanticTurnOff();
	}

	private void turnState(boolean newState){
		ObsProperty stateProperty = getObsProperty(porpertyName);
		this.on = newState;
		if(!newState && stateProperty.getValue().equals(true)) {
			stateProperty.updateValue(this.on);
			signal(isOffEvent);
		}

		if(newState && stateProperty.getValue().equals(false)) {
			stateProperty.updateValue(this.on);
			signal(isOffEvent);
		}
	}

}*/
