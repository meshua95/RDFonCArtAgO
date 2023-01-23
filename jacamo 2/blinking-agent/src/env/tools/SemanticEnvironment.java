package tools;

import org.apache.jena.rdf.model.*;

public class SemanticEnvironment {

    public static final String owlNamespace = "http://www.w3.org/2002/07/owl#";
    public static final String rdfSchemaNamespace = "http://www.w3.org/2000/01/rdf-schema#";
    public static final String rdfSyntaxNamespace = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
    public static final String xmlSchemaNamespace =  "http://www.w3.org/2001/XMLSchema#";

    private static SemanticEnvironment instance = null;
    private final Model model;

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

    public void addDataProperty(String resourceName, String propertyName, String type) {
        //definizione della proprietà
        Resource propName = model.createResource(propertyName);
        setRange(propName, type);
        Resource classResource = model.getResource(resourceName);
        setDomain(propName, classResource);
        setDataType(propName);
    }

    public void addDataPropertyValue(String resourceId, String propertyName, Object propertyValue){
        Resource propName = model.createResource(propertyName);
        Resource id = model.getResource(resourceId);
        Property artifactProperty = model.getProperty(propName.getNameSpace(), propName.getLocalName());
        Literal value = model.createLiteral(String.valueOf(propertyValue), xmlSchemaNamespace);
        model.add(model.createStatement(id, artifactProperty, value));
    }

    public void addOperation(String operationName, String resourceName){
        Resource operation = model.createResource(operationName);
        Resource classResource = model.getResource(resourceName);
        setDomain(operation, classResource);
        addDataProperty(resourceName, operationName, "OPERATION" );
    }

    public void addSignaledEvent(String eventName, String resourceName){
        Resource event = model.createResource(eventName);
        Resource classResource = model.getResource(resourceName);
        setDomain(event, classResource);
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

    private void setRange(Resource propName, String typeProperty){
        Property rangeProperty = model.getProperty(rdfSchemaNamespace, "range");
        Resource typePropertyValue = model.createResource(xmlSchemaNamespace + typeProperty);
        model.add(model.createStatement(propName, rangeProperty, typePropertyValue));
    }

    private void setDomain(Resource propName, Resource classResource) {
        Property domainProperty = model.createProperty(rdfSchemaNamespace, "domain");
        model.add(model.createStatement(propName, domainProperty, classResource));
    }

    private void setDataType(Resource propName){
        Property typeProperty = model.getProperty(rdfSyntaxNamespace, "type");
        Resource typeResource = model.createResource(owlNamespace + "DatatypeProperty");
        model.add(model.createStatement(propName, typeProperty, typeResource));
    }

}
