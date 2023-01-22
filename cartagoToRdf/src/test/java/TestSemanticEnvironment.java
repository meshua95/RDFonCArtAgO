import org.apache.jena.rdf.model.StmtIterator;
import tesi.extension.CArtAgOtoRDF.SemanticEnvironment;

public class TestSemanticEnvironment {

    public static void main(String[] args) {
        SemanticEnvironment se = SemanticEnvironment.getInstance();
        se.addOwlObject("SemanticLamp");

        StmtIterator iter = se.getModel().listStatements();
        iter.forEach(i -> System.out.println(i.getSubject()
                + " + " + i.getPredicate()
                + " + " + i.getObject()));
    }
}
