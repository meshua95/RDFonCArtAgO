package it.unibo.tesi.cartago;

import cartago.*;

@ARTIFACT_INFO(
        outports = {
                @OUTPORT(name = "out-1")
        }
) public class LightSwitch extends Artifact {

    void init(){
        defineObsProperty("pressed", false);
    }

    @OPERATION void press() {
        ObsProperty prop = getObsProperty("pressed");
        prop.updateValue(true);
        try {
            execLinkedOp("out-1","tourn_on");
        } catch (OperationException e) {
            e.printStackTrace();
        }
        signal("is_pressed");
    }

    @OPERATION void release(){
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
