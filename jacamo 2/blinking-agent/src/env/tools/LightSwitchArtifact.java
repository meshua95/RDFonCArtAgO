package tools;

import cartago.Artifact;
import cartago.OPERATION;
import cartago.ObsProperty;
import cartago.OperationException;

public class LightSwitchArtifact extends Artifact {

    /*PROPERTIES*/
    private final String propertyName = "press";
    private boolean press;

    /*EVENTS*/
    private final String isPressedEvent = "is_released";
    private final String isReleasedEvent = "is_released";

    void init(String id, boolean isPressed){
        SemanticEnvironment environment = SemanticEnvironment.getInstance();
        String className = this.getClass().getSimpleName();
        environment.addOwlObject(className, id);

        log("creating...");
        this.press = isPressed;
        defineObsProperty(propertyName, this.press); //define a new property that can be observed by agents
        log("created");

        environment.addDataProperty(className, id, propertyName, this.press);
        log(environment.printAllStatement());
    }

    @OPERATION
    void press() {
        ObsProperty prop = getObsProperty("pressed");
        prop.updateValue(true);
        try {
            execLinkedOp("out-1","tourn_on");
        } catch (OperationException e) {
            e.printStackTrace();
        }
        signal("is_pressed");
    }

    @OPERATION
    void release(){
        ObsProperty prop = getObsProperty("pressed");
        prop.updateValue(false);
        try {
            execLinkedOp("out-1","tourn_off");
        } catch (OperationException e) {
            e.printStackTrace();
        }
        signal("is_released");
    }

}
