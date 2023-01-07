package it.unibo.tesi.cartago;

import cartago.*;

public class Lamp extends Artifact {

    void init(boolean isOn){
        if(isOn){
        defineObsProperty("state", "on");
        } else {
            defineObsProperty("state", "off");
        }
    }

    @LINK void tourn_on(){
        ObsProperty prop = getObsProperty("state");
        if(prop.getValue().equals("off")) {
            prop.updateValue("on");
            signal("is_on");
        }
    }

    @LINK void tourn_off(){
        ObsProperty prop = getObsProperty("state");
        if(prop.getValue().equals("on")) {
            prop.updateValue("off");
            signal("is_off");
        }
    }

}