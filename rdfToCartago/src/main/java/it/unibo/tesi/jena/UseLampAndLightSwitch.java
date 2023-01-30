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

        //device(model);

        query2(model);

        //devicesId(model);

        //deviceInstances(model, "Lamp");
    }

    private static void device(Model model){
        // Create a new query
        String queryString =
                "PREFIX : <http://www.semanticweb.org/ontologies/artifacts#> " +
                        "PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                        "PREFIX xml: <http://www.w3.org/XML/1998/namespace> " +
                        "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
                        "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
                        "SELECT ?subject " +
                        "WHERE {" +
                        "?subject rdf:type owl:Class . " +
                        "?subject rdfs:subClassOf :Device . }";


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
                "PREFIX : <http://www.semanticweb.org/ontologies/artifacts#> " +
                        "SELECT ?x ?z " +
                        "WHERE {:lightSwitch_0 :connectedTo ?z . }";

        Query query = QueryFactory.create(queryString);

        QueryExecution qe = QueryExecutionFactory.create(query, model);
        ResultSet results = qe.execSelect();

        ResultSetFormatter.out(System.out, results, query);

        qe.close();
    }

    private static void devicesId(Model model){
        // Create a new query
        String queryString =
                "PREFIX : <http://www.semanticweb.org/ontologies/artifacts#> " +
                        "PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                        "PREFIX xml: <http://www.w3.org/XML/1998/namespace> " +
                        "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
                        "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
                        "SELECT ?id " +
                        "WHERE {" +
                        "?subject rdfs:subClassOf :Device . " +
                        "?id rdf:type ?subject}";


        Query query = QueryFactory.create(queryString);

        // Execute the query and obtain results
        QueryExecution qe = QueryExecutionFactory.create(query, model);
        ResultSet results = qe.execSelect();

        // Output query results
        ResultSetFormatter.out(System.out, results, query);

        // Important ‑ free up resources used running the query
        qe.close();
    }

    private static void deviceInstances(Model model, String device){
        // Create a new query
        String queryString =
                "PREFIX : <http://www.semanticweb.org/ontologies/artifacts#> " +
                        "PREFIX owl: <http://www.w3.org/2002/07/owl#> " +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                        "PREFIX xml: <http://www.w3.org/XML/1998/namespace> " +
                        "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
                        "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
                        "SELECT ?id " +
                        "WHERE {" +
                        "?id rdf:type :"+ device + "}";


        Query query = QueryFactory.create(queryString);

        // Execute the query and obtain results
        QueryExecution qe = QueryExecutionFactory.create(query, model);
        ResultSet results = qe.execSelect();

        // Output query results
        ResultSetFormatter.out(System.out, results, query);

        // Important ‑ free up resources used running the query
        qe.close();
    }


}
