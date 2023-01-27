package tools;

import cartago.*;

public class LightSwitchArtifact extends SemanticArtifact {

    /*PROPERTIES*/
    private final String pressPropertyName = "statePressed";
    private boolean press;

    void init(String id, boolean isPressed, String idConnection){
        super.init(this, id);

        log("creating...");
        this.press = isPressed;
        defineObsProperty(pressPropertyName, this.press);
        defineRelationship("connectedTo", idConnection);
        log("created");

        super.setAvailableOperations(this);

        printModel();
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
