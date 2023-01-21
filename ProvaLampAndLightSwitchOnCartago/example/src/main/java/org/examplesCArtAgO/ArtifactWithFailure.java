package it.unibo.tesi.cartago.examplesCArtAgO;

import cartago.*;

public class ArtifactWithFailure extends Artifact {

	@OPERATION void testFail(){		
		log("executing testFail..");
		failed("Failure msg", "reason", "test",303);
	}
	
}
