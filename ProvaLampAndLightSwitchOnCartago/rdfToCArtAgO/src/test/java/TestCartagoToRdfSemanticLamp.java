import cartago.*;
import cartago.security.AgentIdCredential;
import it.unibo.tesi.extension.CArtAgOtoRDF.SemanticLamp;

import static org.apache.jena.vocabulary.OWLResults.system;

public class TestCartagoToRdfSemanticLamp {

    public static void main(String[] args) {
        try {
            CartagoService.startNode();
            Artifact a = new DefaultArtifactFactory().createArtifact(SemanticLamp.class.getSimpleName());

            //new TestAgent("Michelangelo").start();

        } catch (CartagoException e) {
            throw new RuntimeException(e);
        }
    }



}