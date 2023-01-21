package it.unibo.tesi.extension.CArtAgOtoRDF;

import cartago.Artifact;
import cartago.OPERATION;
import cartago.ObsProperty;
import cartago.OpFeedbackParam;
import org.apache.jena.rdf.model.*;

public class SemanticLamp extends Artifact{

    private boolean on;
    private final String isOnEvent = "is_on";
    private final String isOffEvent = "is_off";
    private final String porpertyName = "state";

    void init(boolean isOn){
        SemanticEnvironment environment = SemanticEnvironment.getInstance();

        //crea l'oggetto owl SemanticLamp prima di definire qualsiasi proprietà
        environment.addOwlObject(this.getClass().getSimpleName());

        //comincia ad aggiungere le proprietà
        this.on = isOn;
        defineObsProperty(porpertyName, on);

        //per creare l'istanza dell'oggetto uso this.getId().getId(); che prende il valore dell'ArtifactId
        environment.addDataProperty(this.getClass().getSimpleName(), porpertyName, on);
    }

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

/*
    //owl:ObjectProperty
    private void semanticTurnOn(){
        Resource resource = model.getResource(this.getClass().getSimpleName());
        Property onProperty = model.createProperty(isOnEvent);
        Property offProperty = model.createProperty(isOffEvent);

        Statement statement = resource.getProperty(offProperty);
        if(statement != null){
            model.remove(statement);
        }
       // resource.addProperty(onProperty);
    }

    private void semanticTurnOff(){
        Resource resource = model.getResource(this.getClass().getSimpleName());
        Property onProperty = model.createProperty(isOnEvent);
        Property offProperty = model.createProperty(isOffEvent);

        Statement statement = resource.getProperty(onProperty);
        if(statement != null){
            model.remove(statement);
        }
       // resource.addProperty(offProperty);
    }
*/


}
