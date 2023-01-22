package tesi.extension.CArtAgOtoRDF;

import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.RDFDataMgr;

public class SemanticEnvironment {

    private static SemanticEnvironment instance = null;
    private Model model;
    private SemanticEnvironment(){
        model = ModelFactory.createDefaultModel();
    }

    public static SemanticEnvironment getInstance(){
        if (instance == null) {
            instance = new SemanticEnvironment();
        }
        return instance;
    }

    public Model getModel(){
        return this.model;
    }

    public void loadModel(String file){
        model = RDFDataMgr.loadModel(file);
    }

    //crea una nuova risorsa
    public void addOwlObject(String name){
        model = ModelFactory.createDefaultModel();
        Resource resource = model.createResource(name);
        printAllStatement();
    }

    public void addDataProperty(String resourceName, String propertyName, Object propertyValue){
        Resource resource = model.getResource(resourceName);
        Property property = model.createProperty("", propertyName);
       // resource.addProperty(property, value.toString());

        printAllStatement();
    }

    private void printAllStatement(){
        StmtIterator iterator = model.listStatements();

        iterator.forEach(i -> {
            System.out.println(i.getSubject());
            System.out.println(i.getPredicate());
            System.out.println(i.getObject());
            System.out.println();
        });
    }
}
