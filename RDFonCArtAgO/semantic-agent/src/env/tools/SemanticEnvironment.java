package tools;

import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFWriter;
import org.apache.jena.riot.RIOT;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SemanticEnvironment {

    private static final String owlNamespace = "http://www.w3.org/2002/07/owl#";
    private static final String rdfSchemaNamespace = "http://www.w3.org/2000/01/rdf-schema#";
    private static final String rdfSyntaxNamespace = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";

    private static final String base = "http://www.semanticweb.org/ontologies/artifacts#";

    public static final Map<String, String> namespaces = new HashMap<>();

    private final Resource command;
    private final Resource device;
    private final Resource event;

    private static SemanticEnvironment instance = null;
    private final Model model;

    private SemanticEnvironment(){
        model = ModelFactory.createDefaultModel();
        namespaces.put("owl", owlNamespace);
        namespaces.put("rdfs", rdfSchemaNamespace);
        namespaces.put("rdf", rdfSyntaxNamespace);
        namespaces.put("", base);
        command = addOwlObject(base, "Command");
        device = addOwlObject(base, "Device");
        event = addOwlObject(base, "Event");
    }

    public static SemanticEnvironment getInstance(){
        if (instance == null) {
            instance = new SemanticEnvironment();
        }
        return instance;
    }

    public void createResource(String namespace, String resourceName) {
        Resource classResource = model.getResource(namespace + resourceName);
        if(!model.containsResource(classResource)){
            classResource = addOwlObject(namespace, resourceName);
            setDeviceSubclass(classResource);
        }
    }

    public void createResource(String resourceName) {
        createResource(base, resourceName);
    }

    public void createInstance(String namespace, String artifactId, String artifactClass) {
        Resource resourceName = model.createResource(namespace + artifactId);
        Property typeProperty = model.createProperty(rdfSyntaxNamespace, "type");
        Resource artifactResource = model.getResource(namespace + artifactClass);
        model.add(model.createStatement(resourceName, typeProperty, artifactResource));

        Resource instanceDefinition = model.createProperty(owlNamespace, "NamedIndividual");
        model.add(model.createStatement(resourceName, typeProperty, instanceDefinition));
    }

    public void createInstance(String artifactId, String artifactClass) {
        createInstance(base, artifactId, artifactClass);
    }

    /**
     * aggiunge una proprietà ad un oggetto di una classe
     *
     * @param resourceName  nome della risorsa associata alla classe dell'Artefatto (es: Lamp)
     * @param resourceId    id della specifica istanza (es: lamp:0)
     * @param propertyName  nome della proprietà da associare (es: stateOnOff)
     * @param propertyValue valore della proprietà associata allla specifica istanza (es: false)
     */
    public void defineDataProperty(String namespace, String resourceName, String resourceId, String propertyName, String type, Object propertyValue) {
        Resource classResource = model.getResource(namespace + resourceName);
        Resource resourceInstance = getResourceInstance(namespace, resourceId, classResource);
        addDataProperty(namespace, classResource, propertyName, type);
        addDataPropertyValue(resourceInstance, propertyName, propertyValue);
    }

    public void defineDataProperty(String resourceName, String resourceId, String propertyName, String type, Object propertyValue) {
        defineDataProperty(base, resourceName, resourceId, propertyName, type, propertyValue);
    }

    public void addOperation(String namespace, String operationName, String classResourceName){
        Resource operation = model.createResource(namespace + operationName);
        Resource classResource = model.getResource(namespace + classResourceName);

        setDomain(operation, classResource);
        setDomain(operation, command);
        setDataType(operation);
    }

    public void addOperation(String operationName, String classResourceName){
        addOperation(base, operationName, classResourceName);
    }

    public void addObjectProperty(String namespace, String name, String refId, String artifactId, String artifactClass) {
        Resource objProperty = model.getResource(namespace + name);
        if(!model.containsResource(objProperty)){
            objProperty = model.createResource(namespace + name);
            setDevice(objProperty);
            Resource classArt = model.getResource(namespace + artifactClass);
            setDomain(objProperty, classArt);
            setObjecProperty(objProperty);
        }

        if(checkCanRelate(namespace, refId)){
            Resource firstInstance = model.getResource(namespace + artifactId);
            Resource secondInstance = model.getResource(namespace + refId);
            Property prop = model.getProperty(namespace, name);
            model.add(model.createStatement(firstInstance, prop, secondInstance));
        }
    }

    public void addObjectProperty(String name, String refId, String artifactId, String artifactClass) {
        addObjectProperty(base, name, refId, artifactId, artifactClass);
    }

    public Model getModel(){
        return this.model;
    }

    /*
    1 - crea la risorsa evento es: is_on
    2 - prende la risorsa associata relativa all' istanza della classe
    4 - crea la proprietà timestamp
    5 - prende la data
    6 - aggiunge la proprietà timestamp all'evento
     */
    public void addSignaledEvent(String namespace, String eventName, String resourceId, String artifactClass){
        Resource event = model.getResource(namespace + eventName);
        if(!model.containsResource(event)){
            event = model.createResource(namespace + eventName);
            setDomain(event, this.event);
            setDomain(event, model.getResource(namespace + artifactClass));
            setDataType(event);
            setRange(event, namespace, Date.class.getSimpleName());
        }

        Resource resourceInstance = model.getResource(namespace + resourceId);
        //removePrevEvent(resourceInstance);
        Property prop = model.createProperty(namespace + eventName);
        Date timestamp = new Date();
        model.add(model.createStatement(resourceInstance, prop, timestamp.toString()));
    }

    public void addSignaledEvent(String eventName, String resourceId, String artifactClass){
        addSignaledEvent(base, eventName, resourceId, artifactClass);
    }

    public void printAllStatement(){
        RDFWriter.source(model)
                .set(RIOT.symTurtleDirectiveStyle, "sparql")
                .lang(Lang.TTL).output("SemanticEnvironment.ttl");
    }

    /**
     * restituisce un'istanza della risorsa dichiarata owl:Class. Se non esiste la crea
     * @param resId     è l'id dell'istanza (es: lamp_0)
     * @param classRes  è la risorsa relativa alla classe dell'artefatto (es: Lamp)
     */
    private Resource getResourceInstance(String namespace, String resId, Resource classRes) {
        Resource resInstance = model.getResource(namespace + resId);
        if (resInstance.isAnon()) {
            resInstance = model.createResource(namespace + resId);
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
    private Resource addOwlObject(String namespace, String name){
        Resource resourceName = model.createResource(namespace + name);
        Property typeProperty = model.createProperty(rdfSyntaxNamespace, "type");
        Resource classResource = model.createProperty(owlNamespace, "Class");

        model.add(model.createStatement(resourceName, typeProperty, classResource));
        return resourceName;
    }

    private void setRange(Resource propName, String propertyNamespace, String typeProperty){
        Property rangeProperty = model.getProperty(rdfSchemaNamespace + "range");
        Resource typePropertyValue = model.createResource(propertyNamespace + typeProperty);
        model.add(model.createStatement(propName, rangeProperty, typePropertyValue));
    }

    private void setDevice(Resource propName){
        Property rangeProperty = model.getProperty(rdfSchemaNamespace + "range");
        model.add(model.createStatement(propName, rangeProperty, this.device));
    }

    private void setDomain(Resource propName, Resource classResource) {
        Property domainProperty = model.createProperty(rdfSchemaNamespace + "domain");
        model.add(model.createStatement(propName, domainProperty, classResource));
    }

    private void setDataType(Resource propName){
        Property typeProperty = model.getProperty(rdfSyntaxNamespace, "type");
        Resource typeResource = model.createResource(owlNamespace + "DatatypeProperty");
        model.add(model.createStatement(propName, typeProperty, typeResource));
    }

    private boolean checkCanRelate(String namespace, String refId){
        Resource idResource = model.createResource(namespace + refId);
        Property type = model.createProperty(rdfSyntaxNamespace, "type");
        Selector selector = new SimpleSelector(idResource, type, (RDFNode) null);
        List<Statement> stmtIterator = model.listStatements(selector).toList();
        Statement namedIndividualStatement = model.createStatement(idResource, type, model.createResource(owlNamespace + "NamedIndividual"));
        if(!stmtIterator.contains(namedIndividualStatement)){
            return false;
        } else {
            stmtIterator.removeIf(s -> s.equals(namedIndividualStatement));
        }
        Property subClassProperty = model.createProperty(rdfSchemaNamespace, "subClassOf");
        for (Statement s: stmtIterator){
            Resource artifactClass = (Resource) s.getObject();
            selector = new SimpleSelector(artifactClass, subClassProperty, device);
            List<Statement> item = model.listStatements(selector).toList();
            if(!item.isEmpty()){
                return true;
            }
        }
        return false;
    }

    private void setObjecProperty(Resource propName){
        Property typeProperty = model.getProperty(rdfSyntaxNamespace, "type");
        Resource typeResource = model.createResource(owlNamespace + "ObjectProperty");
        model.add(model.createStatement(propName, typeProperty, typeResource));
    }

    private void addDataPropertyValue(Resource resourceInstance, String propertyName, Object propertyValue){
        Resource propName = model.createResource(propertyName);
        Property property = model.getProperty(propName.getNameSpace(), propName.getLocalName());
        Literal value = model.createLiteral(String.valueOf(propertyValue));
        model.add(model.createStatement(resourceInstance, property, value));
    }

    private void addDataProperty(String namespace, Resource classResource, String propertyName, String propertyType) {
        Resource propName = model.createResource(propertyName);
        setRange(propName, namespace, propertyType);
        setDomain(propName, classResource);
        setDataType(propName);
    }

    private void setDeviceSubclass(Resource classResource) {
        Property subClassProperty = model.createProperty(rdfSchemaNamespace, "subClassOf");
        model.add(model.createStatement(classResource, subClassProperty, device));
    }

    public void addNamespace(String prefix, String namespace){
        if (!namespaces.containsKey(prefix)){
            namespaces.put(prefix, namespace);
        }
    }

    public void removeInstance(String namespace, String resourceId){
        SimpleSelector selector = new SimpleSelector(model.getResource(namespace+resourceId), null, (RDFNode) null);
        List<Statement> subjectStatements = model.listStatements(selector).toList();
        for(Statement s: subjectStatements){
            model.remove(s);
        }
        selector = new SimpleSelector(null, null, model.getResource(namespace + resourceId));
        List<Statement> objectStatements = model.listStatements(selector).toList();
        for(Statement s: objectStatements){
            model.remove(s);
        }
    }

    public void removeInstance(String resourceId){
        removeInstance(base, resourceId);
    }

}