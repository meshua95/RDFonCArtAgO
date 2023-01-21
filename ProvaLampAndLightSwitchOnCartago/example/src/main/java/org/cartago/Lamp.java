package org.cartago;

import cartago.*;

public class Lamp extends Artifact {

    private boolean isOn = false;
    void init(){
        defineObsProperty("is_on", "off");
    }

    @OPERATION
    void turn_on(){
        ObsProperty prop = getObsProperty("state");
        if(prop.getValue().equals("off")) {
            prop.updateValue("on");
            signal("is_on");
        }
    }

    @OPERATION
    void turn_off(){
        ObsProperty prop = getObsProperty("state");
        if(prop.getValue().equals("on")) {
            prop.updateValue("off");
            signal("is_off");
        }
    }

}