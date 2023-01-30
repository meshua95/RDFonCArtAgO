package tools;

import cartago.Artifact;
import cartago.OPERATION;
import cartago.OpFeedbackParam;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ServiceArtifact extends Artifact {

    private static final String prefixesList = "PREFIX owl: <" + SemanticEnvironment.owlNamespace + ">"
            + "PREFIX rdf: <" + SemanticEnvironment.rdfSyntaxNamespace + "> "
            + "PREFIX rdfs: <" + SemanticEnvironment.rdfSchemaNamespace + "> "
            + "PREFIX : <" + SemanticEnvironment.base + "> ";

    SemanticEnvironment environment;

    public void init(){
        environment = SemanticEnvironment.getInstance();
    }

    @OPERATION
    public void genericQuery(String queryString,  OpFeedbackParam<QuerySolution[]> resultSet){ //

        queryString = prefixesList.concat(queryString);

        Query query = QueryFactory.create(queryString);

        // Execute the query and obtain results
        QueryExecution qe = QueryExecutionFactory.create(query, environment.getModel());
        ResultSet results = qe.execSelect();

        // Output query results
        log(ResultSetFormatter.asText(results));

        // Important ‑ free up resources used running the query
        qe.close();

        //todo imposta il resultSet
    }

    @OPERATION
    public void availableDevices(OpFeedbackParam<QuerySolution[]> resultSet){
        String queryString = prefixesList +
                "SELECT ?id " +
                "WHERE {" +
                "?subject rdfs:subClassOf :Device . " +
                "?id rdf:type ?subject}";


        Query query = QueryFactory.create(queryString);

        // Execute the query and obtain results
        QueryExecution qe = QueryExecutionFactory.create(query, environment.getModel());
        ResultSet results = qe.execSelect();

        // Output query results
        log(ResultSetFormatter.asText(results));

        // Important ‑ free up resources used running the query
        qe.close();

        //todo imposta il resultSet
    }

    @OPERATION
    public void specificDeviceInstance(String deviceType, OpFeedbackParam<QuerySolution[]> resultSet){
        String queryString = prefixesList +
                "SELECT ?id " +
                "WHERE {" +
                "?id rdf:type :"+ deviceType + "}";


        Query query = QueryFactory.create(queryString);

        // Execute the query and obtain results
        QueryExecution qe = QueryExecutionFactory.create(query, environment.getModel());
        ResultSet results = qe.execSelect();

        // Output query results
        log(ResultSetFormatter.asText(results));

        // Important ‑ free up resources used running the query
        qe.close();

        //todo imposta il resultSet
    }

    @OPERATION
    public void allRelationsQuery(OpFeedbackParam<QuerySolution[]> resultSet){
        String queryString = prefixesList +
                "SELECT ?first ?second " +
                "WHERE {?first :connectedTo ?second . }";

        Query query = QueryFactory.create(queryString);

        // Execute the query and obtain results
        QueryExecution qe = QueryExecutionFactory.create(query, environment.getModel());
        ResultSet results = qe.execSelect();

        // Output query results
        log(ResultSetFormatter.asText(results));

        // Important ‑ free up resources used running the query
        qe.close();

        //todo imposta il resultSet
    }

    @OPERATION
    public void deviceRelationQuery(String deviceId, OpFeedbackParam<QuerySolution[]> resultSet){
        String queryString = prefixesList +
                "SELECT ?device " +
                "WHERE {:" + deviceId + " :connectedTo ?device . }";

        Query query = QueryFactory.create(queryString);

        // Execute the query and obtain results
        QueryExecution qe = QueryExecutionFactory.create(query, environment.getModel());
        ResultSet results = qe.execSelect();

        // Output query results
        log(ResultSetFormatter.asText(results));

        // Important ‑ free up resources used running the query
        qe.close();

        //todo imposta il resultSet
    }
}
