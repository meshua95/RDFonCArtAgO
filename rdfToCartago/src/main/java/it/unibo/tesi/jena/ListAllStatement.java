package it.unibo.tesi.jena;

import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.RDFDataMgr;

public class ListAllStatement {

    public static void main(String[] args) {
        String fileName = "src/main/resources/LampAndLightSwitch.ttl";

        Model model = RDFDataMgr.loadModel(fileName);

        /*
        Resource lamp = model.createResource(":Lamp")
                .addProperty(model.getProperty(":is_on"), "true");
        StmtIterator iterator = lamp.listProperties(model.getProperty(":is_on"));
        while (iterator.hasNext()) {
            System.out.println(" " + iterator.nextStatement()
                    .getObject()
                    .toString());
        }
        */

        /**
         * prende la lista degli statement con l'oggetto indicato
         * */
   /*     StmtIterator stmtIter = model.listStatements(null, null, model.getResource( "http://www.w3.org/2002/07/owl#" + "Class"));
        // System.out.println("--------------->" + stmtIter.toList().size());
        while (stmtIter.hasNext()){
            Statement s = stmtIter.nextStatement();
            System.out.println(s.toString());
            // do something to the busdriver (only nice things, everybody likes busdrivers)
        }
 */
        /**
         * prende tutti gli statement nel model
         * */
        StmtIterator iter = model.listStatements();
        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement();      // prende lo statement
            Resource subject = stmt.getSubject();       // prende il soggetto
            Property predicate = stmt.getPredicate();   // prende il predicato
            RDFNode object = stmt.getObject();          // prende l'oggetto

            System.out.print(subject.toString());
            System.out.print(" " + predicate.toString() + " ");
            if (object instanceof Resource) {
                System.out.println("RESOURCE");
                System.out.print(object.toString());
            } else {
                // object is a literal
                System.out.println("LITERAL");
                System.out.print(" \"" + object.toString() + "\"");
            }
            System.out.println(" .");
            System.out.println(" ");
            System.out.println(" ");
        }

    }
}
