package it.unibo.tesi;

import it.unibo.tesi.ttlData.RDFUtils;

import java.util.List;

public class SemanticLamp extends Artifact {

    void init(String fileName){
        List<String> properties = RDFUtils.getClassDataProperties(fileName, this.getClass().getSimpleName());
        properties.forEach(p -> {
            String value = RDFUtils.getPropertyValue(fileName, p);
        });
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