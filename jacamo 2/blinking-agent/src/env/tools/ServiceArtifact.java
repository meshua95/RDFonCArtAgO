package tools;

import cartago.Artifact;
import cartago.OPERATION;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

import java.util.List;
import java.util.Set;

public class ServiceArtifact extends Artifact {

    SemanticEnvironment environment;
    String prefixQuery = "";

    public void init(){
        environment = SemanticEnvironment.getInstance();
    }

    @OPERATION
    public void environmentQuery(){
        environment.getModel().listStatements().toList().forEach(s -> log(s.toString()));
        //String queryString = "SELECT ?class WHERE {?class ?p ?o . FILTER (substr(str(?p), (strlen(str(?p)) - 11)) = 'subClassOf')}";
        //String queryString = "SELECT ?y WHERE {?y a ?x. FILTER regex(str(?x), 'Device$')}";
        String queryString = "SELECT ?class WHERE {?class ?x ?y . FILTER regex(str(?x), 'subClassOf$') FILTER regex(str(?y), 'Device$') }";
        //String queryString = "SELECT ?instance WHERE {?instance ?p ?o . FILTER regex(str(?p), 'type$') FILTER regex(str(?o), 'LampArtifact$') }";
        /*String queryString = "SELECT ?instance " +
                "WHERE {{ " +
                "SELECT ?class WHERE {?class ?p ?o . FILTER regex(str(?p), 'subClassOf$') FILTER regex(str(?o), 'Device$') }} " +
                "?instance a ?class }"; */
//.FILTER EXISTS {?instance ?p ?o . FILTER regex(str(?p), 'type$') }
        //String queryString = "SELECT ?x ?p ?o WHERE {?x ?p ?o . FILTER regex(str(?p), 'subClassOf$') FILTER regex(str(?o), 'Device$') } ";
        Query query = QueryFactory.create(queryString);

        QueryExecution qe = QueryExecutionFactory.create(query, environment.getModel());
        ResultSet results = qe.execSelect();

        while (results.hasNext()) {
            QuerySolution result = results.next();
            //log(ResultSetFormatter.asText(results));
        }

//        ResultSetFormatter.out(System.out, results, query));
log("fine query");
        qe.close();
    }

    @OPERATION
    public void artifactQuery(){

    }
}
