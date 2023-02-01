package tools;

import cartago.Artifact;
import cartago.OPERATION;
import cartago.OpFeedbackParam;
import org.apache.jena.query.*;

import java.util.ArrayList;
import java.util.List;

public class ServiceArtifact extends Artifact {

    private static final String prefixesList = "PREFIX owl: <" + SemanticEnvironment.owlNamespace + ">"
            + "PREFIX rdf: <" + SemanticEnvironment.rdfSyntaxNamespace + "> "
            + "PREFIX rdfs: <" + SemanticEnvironment.rdfSchemaNamespace + "> "
            + "PREFIX : <" + SemanticEnvironment.base + "> ";

    private static SemanticEnvironment environment;

    public void init(){
        environment = SemanticEnvironment.getInstance();
    }

    @OPERATION
    public void query(String queryString, OpFeedbackParam<List<QuerySolution>> resultSet){ //

        queryString = prefixesList.concat(queryString);
        Query query = QueryFactory.create(queryString);
        QueryExecution qe = QueryExecutionFactory.create(query, environment.getModel());
        ResultSet results = qe.execSelect();
        List<QuerySolution> querySolutionList = new ArrayList<>();
        while(results.hasNext()){
            querySolutionList.add(results.nextSolution());
        }
        resultSet.set(querySolutionList);

        qe.close();
    }

    @OPERATION
    public void getAtIndex(int index, List<QuerySolution> res, OpFeedbackParam<QuerySolution> elem){
        if(res.size() > index){
            log("ho settato l'index");
            elem.set(res.get(index));
        } else {
            log("ho settato a null");
            elem.set(null);
        }

    }

    @OPERATION
    public void getValue(String var, QuerySolution elem, OpFeedbackParam<String> val){
        log(elem.getResource(var).getLocalName());
        val.set(elem.getResource(var).getLocalName());
    }

    /*
    "SELECT ?id WHERE { ?subject rdfs:subClassOf :Device . ?id rdf:type ?subject}"
    "SELECT ?id WHERE {?id rdf:type :"+ deviceType + "}"
    "SELECT ?first ?second WHERE {?first :connectedTo ?second . }"
    "SELECT ?device WHERE {:" + deviceId + " :connectedTo ?device . }"
    */
}
