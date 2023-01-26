package tools;

import org.apache.jena.rdf.model.*;

import java.util.Date;
import java.util.List;

public class SemanticEnvironment {

    public static final String owlNamespace = "http://www.w3.org/2002/07/owl#";
    public static final String rdfSchemaNamespace = "http://www.w3.org/2000/01/rdf-schema#";
    public static final String rdfSyntaxNamespace = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
    public static final String xmlSchemaNamespace =  "http://www.w3.org/2001/XMLSchema#";

    private final Resource command;
    private final Resource device;
    private final Resource event;

    private static SemanticEnvironment instance = null;
    private final Model model;

    private SemanticEnvironment(){
        model = ModelFactory.createDefaultModel();
        command = addOwlObject("Command");
        device = addOwlObject("Device");
        event = addOwlObject("Event");
    }

    public static SemanticEnvironment getInstance(){
        if (instance == null) {
            instance = new SemanticEnvironment();
        }
        return instance;
    }

    public void createResource(String resourceName) {
        Resource classResource = model.getResource(resourceName);
        if(!model.containsResource(classResource)){
            classResource = addOwlObject(resourceName);
            setDeviceSubclass(classResource);
        }
    }

    /**
     * aggiunge una proprietà ad un oggetto di una classe
     *
     * @param resourceName  nome della risorsa associata alla classe dell'Artefatto (es: Lamp)
     * @param resourceId    id della specifica istanza (es: lamp:0)
     * @param propertyName  nome della proprietà da associare (es: stateOnOff)
     * @param propertyValue valore della proprietà associata allla specifica istanza (es: false)
     */
    public void defineDataProperty(String resourceName, String resourceId, String propertyName, Object propertyValue) {
        Resource classResource = model.getResource(resourceName);
        Resource resourceInstance = getResourceInstance(resourceId, classResource);
        addDataProperty(classResource, propertyName, propertyValue.getClass().getSimpleName());
        addDataPropertyValue(resourceInstance, propertyName, propertyValue);
    }

    public void addOperation(String operationName, String classResourceName){
        Resource operation = model.createResource(operationName);
        Resource classResource = model.getResource(classResourceName);

        setDomain(operation, classResource);
        setDomain(operation, command);
        setDataType(operation);
    }

    private void addDataPropertyValue(Resource resourceInstance, String propertyName, Object propertyValue){
        Resource propName = model.createResource(propertyName);
        Property property = model.getProperty(propName.getNameSpace(), propName.getLocalName());
        Literal value = model.createLiteral(String.valueOf(propertyValue), xmlSchemaNamespace);
        model.add(model.createStatement(resourceInstance, property, value));
    }

    private void addDataProperty(Resource classResource, String propertyName, String propertyType) {
        Resource propName = model.createResource(propertyName);
        setRange(propName, propertyType);
        setDomain(propName, classResource);
        setDataType(propName);
    }

    private void setDeviceSubclass(Resource classResource) {
        Property subClassProperty = model.createProperty(rdfSchemaNamespace, "subClassOf");
        model.add(model.createStatement(classResource, subClassProperty, device));
    }

    /**
     * restituisce un'istanza della risorsa dichiarata owl:Class. Se non esiste la crea
     * @param resId     è l'id dell'istanza (es: lamp_0)
     * @param classRes  è la risorsa relativa alla classe dell'artefatto (es: Lamp)
     */
    private Resource getResourceInstance(String resId, Resource classRes) {
        Resource resInstance = model.getResource(resId);
        if (resInstance.isAnon()) {
            resInstance = model.createResource(resId);
            Property typeProperty = model.createProperty(rdfSyntaxNamespace, "type");
            Resource owlType = model.createResource(owlNamespace + "NamedIndividual");
            model.add(model.createStatement(resInstance, typeProperty, classRes));
            model.add(model.createStatement(resInstance, typeProperty, owlType));
        }
        return resInstance;
    }

    /**
     * crea una nuova risorsa
     *
     * @param name  nome della nuova risorsa
     * @return      risorsa creata e dichiarata come owl:Class
     */
    private Resource addOwlObject(String name){
        Resource resourceName = model.createResource(name);
        Property typeProperty = model.createProperty(rdfSyntaxNamespace, "type");
        Resource classResource = model.createProperty(owlNamespace, "Class");

        model.add(model.createStatement(resourceName, typeProperty, classResource));
        return resourceName;
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

    public String printAllStatement(){
        String output = "Gli statement sono: \n";
        StmtIterator iterator = model.listStatements();

        while(iterator.hasNext()){
            Statement elem = iterator.next();
            output = output + elem.getSubject() + " "
                        + elem.getPredicate() + " "
                        + elem.getObject() + "\n";
        }
        return output;
    }

    /*
    1 - crea la risorsa evento es: is_on
    2 - prende la risorsa associata relativa all' istanza della classe
    4 - crea la proprietà timestamp
    5 - prende la data
    6 - aggiunge la proprietà timestamp all'evento
     */
    public void addSignaledEvent(String eventName, String resourceId, String artifactClass){
        Resource event = model.getResource(eventName);
        if(!model.containsResource(event)){
            event = model.createResource(eventName);
            setDomain(event, this.event);
            setDomain(event, model.getResource(artifactClass));
            setDataType(event);
            setRange(event, Date.class.getSimpleName());
        }

        Resource resourceInstance = model.getResource(resourceId);
        removePrevEvent(resourceInstance);
        Property prop = model.createProperty(eventName);
        Date timestamp = new Date();
        model.add(model.createStatement(resourceInstance, prop, timestamp.toString()));
    }

    private void removePrevEvent(Resource resInstance){
        Selector selector = new SimpleSelector(null, null, this.event);
        List<Statement> stmtIter = model.listStatements(selector).toList();
        for (Statement s: stmtIter){
            Property prop = model.createProperty(s.getSubject().getNameSpace(), s.getSubject().getLocalName());

            Selector sel = new SimpleSelector(resInstance, prop, (RDFNode) null);
            StmtIterator iter = model.listStatements(sel);
            while (iter.hasNext()){
                Statement st = iter.nextStatement();
                model.remove(st);
            }
        }
    }

}
