package tools;

import cartago.*;

public class LightSwitchArtifact extends SemanticArtifact {

    /*PROPERTIES*/
    private final String pressPropertyName = "press";
    private boolean press;

    void init(String id, boolean isPressed){
        super.init(this.getClass().getSimpleName(), id);

        log("creating...");
        this.press = isPressed;
        defineObsProperty(pressPropertyName, this.press);
        log("created");

        super.setAvailableOperations(this);
    }

    @OPERATION
    void press() {
        setPress(true);
    }

    @OPERATION
    void release(){
        setPress(false);
    }

    private void setPress(boolean p){
        ObsProperty prop = getObsProperty(pressPropertyName);
        this.press = p;
        prop.updateValue(this.press);
        if(p){
            String isPressedEvent = "is_pressed";
            signal(isPressedEvent);
        } else {
            String isReleasedEvent = "is_released";
            signal(isReleasedEvent);
        }

    }

}
