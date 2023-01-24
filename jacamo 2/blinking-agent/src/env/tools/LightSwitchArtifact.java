package tools;

import cartago.*;

public class LightSwitchArtifact extends SemanticArtifact {

    SemanticEnvironment environment;

    /*PROPERTIES*/
    private final String pressPropertyName = "press";
    private boolean press;

    /*EVENTS*/
    private final String isPressedEvent = "is_pressed";
    private final String isReleasedEvent = "is_released";

    void init(String id, boolean isPressed){
        super.init(this.getClass().getSimpleName(), id);

        log("creating...");
        this.press = isPressed;
        defineObsProperty(pressPropertyName, this.press);
        log("created");

        super.availableOperations(this);
    }

    @OPERATION
    void press() {
        ObsProperty prop = getObsProperty(pressPropertyName);
        this.press = true;
        prop.updateValue(this.press);
        signal(isPressedEvent);
    }

    @OPERATION
    void release(){
        ObsProperty prop = getObsProperty(pressPropertyName);
        this.press = false;
        prop.updateValue(this.press);
        signal(isReleasedEvent);
    }

}
