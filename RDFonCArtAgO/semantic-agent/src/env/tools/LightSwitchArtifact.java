package tools;

import cartago.*;
import semanticDefinition.SemanticArtifact;

public class LightSwitchArtifact extends SemanticArtifact {

    private final String pressPropertyName = "statePressed";
    private boolean press;

    void init(boolean isPressed, String idConnection){
        super.init(this, this.getId().getName());

        this.press = isPressed;
        defineObsProperty(pressPropertyName, "boolean", this.press);
        defineRelationship(idConnection);
    }

    @OPERATION void lightSwitchState(OpFeedbackParam<Boolean> state) {
        state.set(this.press);
    }

    @OPERATION void connectTo(String idConnection){
        addRelationship(idConnection);
    }

    @OPERATION void disconnectTo(String idConnection){
        removeRelationship(idConnection);
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
        this.press = p;
        updateValue(pressPropertyName, this.press);
        if(p){
            String isPressedEvent = "is_pressed";
            signal(isPressedEvent);
        } else {
            String isReleasedEvent = "is_released";
            signal(isReleasedEvent);
        }
    }

    public void dispose(){
        super.dispose();
    }

}
