package tools;

import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.RDFDataMgr;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class SemanticEnvironment {

    public static final String owlNamespace = "http://www.w3.org/2002/07/owl#";
    public static final String rdfSchemaNamespace = "http://www.w3.org/2000/01/rdf-schema#";
    public static final String rdfSyntaxNamespace = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
    public static final String xmlSchemaNamespace =  "http://www.w3.org/2001/XMLSchema#";

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

    //crea una nuova risorsa
    public void addOwlObject(String name, String id){
        Resource classResource = model.getResource(name);
        Resource resourceName = model.createResource(name);
        Property typeProperty = model.createProperty(rdfSyntaxNamespace, "type");

        if(classResource.isAnon()) {
            classResource = model.createProperty(owlNamespace, "Class");
        }

        //l'istanza di una classe deve avere un identificativo
        Resource idResource = model.createResource(id);
        model.add(model.createStatement(resourceName, typeProperty, classResource));
        Resource owlType = model.createResource(owlNamespace + "NamedIndividual");
        model.add(model.createStatement(idResource, typeProperty, resourceName));
        model.add(model.createStatement(idResource, typeProperty, owlType));
    }

    public void addDataProperty(String resourceName, String resourceId, String propertyName, Object propertyValue){
        //definizione della proprietà
        Resource propName = model.createResource(propertyName);
        Property rangeProperty = model.getProperty(rdfSchemaNamespace, "range");
        Resource typePropertyValue = model.createResource(xmlSchemaNamespace + propertyValue.getClass().getSimpleName());
        model.add(model.createStatement(propName, rangeProperty, typePropertyValue));

        Property domainProperty = model.createProperty(rdfSchemaNamespace, "domain");
        Resource classResource = model.getResource(resourceName);
        model.add(model.createStatement(propName, domainProperty, classResource));

        Property typeProperty = model.getProperty(rdfSyntaxNamespace, "type");
        Resource typeResource = model.createResource(owlNamespace + "DatatypeProperty");
        model.add(model.createStatement(propName, typeProperty, typeResource));

        //definizione del valore
        Resource id = model.getResource(resourceId);
        Property artifactProperty = model.getProperty(propName.getNameSpace(), propName.getLocalName());
        Literal value = model.createLiteral(String.valueOf(propertyValue), xmlSchemaNamespace);
        model.add(model.createStatement(id, artifactProperty, value));
        printAllStatement();
    }

    public void addOperation(String operationName, String resourceName){
        Resource operation = model.createResource(operationName);
        Property domainProperty = model.createProperty(rdfSchemaNamespace, "domain");
        Resource classResource = model.getResource(resourceName);
        model.add(model.createStatement(operation, domainProperty, classResource));
    }

    /*private void availableOperations(){

        log("methods number: " + methods.length);
        for (Method method : methods) {
            log("METHOD ->" + method.getName());
            Annotation[] ann = method.getAnnotations();
            for(int i = 0; i < ann.length; i++){
                log("ANN ->" + ann[i].toString());
            }

            //environment.addOperation(method.getName(), this.getClass().getSimpleName());
        }
    }*/

    public void addSignaledEvent(String eventName, String resourceName){
        Resource event = model.createResource(eventName);
        Property domainProperty = model.createProperty(rdfSchemaNamespace, "domain");
        Resource classResource = model.getResource(resourceName);
        model.add(model.createStatement(event, domainProperty, classResource));
    }

    public void removeEvent(String eventName, String resourceName){
        Resource event = model.createResource(eventName);
        Property domainProperty = model.createProperty(rdfSchemaNamespace, "domain");
        Resource classResource = model.getResource(resourceName);
        model.remove(model.createStatement(event, domainProperty, classResource));
    }

    public String printAllStatement(){
        String output = "Gli statement sono: \n ";
        StmtIterator iterator = model.listStatements();

        while(iterator.hasNext()){
            Statement elem = iterator.next();
            output = output + elem.getSubject() + " "
                    + elem.getPredicate() + " "
                    + elem.getObject() + "\n";
        }
        return output;
    }
}
