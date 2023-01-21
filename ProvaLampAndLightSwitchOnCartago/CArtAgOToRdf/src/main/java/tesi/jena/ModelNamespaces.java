package tesi.jena;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.NsIterator;
import org.apache.jena.riot.RDFDataMgr;

public class ModelNamespaces {

    public static void main(String[] args) {
        String fileName = "src/main/resources/LampAndLightSwitch.ttl";

        Model model = RDFDataMgr.loadModel(fileName);
        model.listNameSpaces();

        NsIterator nsIter = model.listNameSpaces();
        // System.out.println("--------------->" + stmtIter.toList().size());
        while (nsIter.hasNext()){
            String namespace = nsIter.nextNs();
            System.out.println(namespace);
            // do something to the busdriver (only nice things, everybody likes busdrivers)
        }
        System.out.println();

    }
}
