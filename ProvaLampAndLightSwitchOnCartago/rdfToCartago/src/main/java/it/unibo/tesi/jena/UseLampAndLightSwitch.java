package it.unibo.tesi.jena;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.RDFDataMgr;

public class UseLampAndLightSwitch {

    public static void main(String[] args) {
        String fileName = "src/main/resources/LampAndLightSwitch.ttl";

        Model model = RDFDataMgr.loadModel(fileName);

        /**
         * altro modo per caricare il ttl*/
        /*Model model = ModelFactory.createDefaultModel() ;
        model.read(fileName);
        */

        query1(model);

        query2(model);
    }

    private static void query1(Model model){
        // Create a new query
        String queryString =
                "PREFIX : <http://www.semanticweb.org/meshuagalassi/ontologies/2022/11/LampAndLightSwitch#> " +
                        "PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
                        "SELECT ?class " +
                        "WHERE {" +
                        "      ?class owl:disjointWith :LightSwitch . " +
                        "      }";

        Query query = QueryFactory.create(queryString);

        // Execute the query and obtain results
        QueryExecution qe = QueryExecutionFactory.create(query, model);
        ResultSet results = qe.execSelect();

        // Output query results
        ResultSetFormatter.out(System.out, results, query);

        // Important ‑ free up resources used running the query
        qe.close();
    }


    private static void query2(Model model){
        // Create a new query
        String queryString =
                "PREFIX : <http://www.semanticweb.org/meshuagalassi/ontologies/2022/11/LampAndLightSwitch#> " +
                        "PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                        "SELECT ?x ?y " +
                        "WHERE {" +
                        "      ?x :connectedTo ?y . " +
                        "      }";

        Query query = QueryFactory.create(queryString);

        QueryExecution qe = QueryExecutionFactory.create(query, model);
        ResultSet results = qe.execSelect();

        ResultSetFormatter.out(System.out, results, query);

        qe.close();
    }

}
