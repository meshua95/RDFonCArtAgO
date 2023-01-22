package it.unibo.tesi.cartago.examplesCArtAgO;

import cartago.*;

public class Counter extends Artifact {

	@OPERATION void init(){
		defineObsProperty("count",0);
	}
	    
	@OPERATION void inc(){
		ObsProperty prop = getObsProperty("count");
		prop.updateValue(prop.intValue()+1);
		signal("tick");
	}
}