package tools;

import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFWriter;
import org.apache.jena.riot.RIOT;

import java.util.Date;
import java.util.List;

public class SemanticEnvironment {

    public static final String

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

    public void createInstance(String artifactId, String artifactClass) {
        Resource resourceName = model.createResource(artifactId);
        Property typeProperty = model.createProperty( "type");
        Resource artifactResource = model.getResource(artifactClass);
        model.add(model.createStatement(resourceName, typeProperty, artifactResource));

        Resource instanceDefinition = model.createProperty( "NamedIndividual");
        model.add(model.createStatement(resourceName, typeProperty, instanceDefinition));
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


    public void addObjectProperty(String name, String refId, String artifactId, String artifactClass) {
        Resource objProperty = model.getResource(name);
        if(!model.containsResource(objProperty)){
            objProperty = model.createResource(name);
            setRange(objProperty, device.toString());
            Resource classArt = model.getResource(artifactClass);
            setDomain(objProperty, classArt);
            setObjecProperty(objProperty);
        }

        if(checkCanRelate(refId )){
            Resource firstInstance = model.getResource(artifactId);
            Resource secondInstance = model.getResource(refId);
            Property prop = model.getProperty(name);
            model.add(model.createStatement(firstInstance, prop, secondInstance));
        }
    }

    public Model getModel(){
        return this.model;
    }

    public void printAllStatement(){
        RDFWriter.source(model)
                .set(RIOT.symTurtleDirectiveStyle, "sparql")
                .lang(Lang.TTL).output("SemanticEnvironment.ttl");
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


    /**
     * restituisce un'istanza della risorsa dichiarata owl:Class. Se non esiste la crea
     * @param resId     è l'id dell'istanza (es: lamp_0)
     * @param classRes  è la risorsa relativa alla classe dell'artefatto (es: Lamp)
     */
    private Resource getResourceInstance(String resId, Resource classRes) {
        Resource resInstance = model.getResource(resId);
        if (resInstance.isAnon()) {
            resInstance = model.createResource(resId);
            Property typeProperty = model.createProperty("type");
            Resource owlType = model.createResource("NamedIndividual");
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
        Property typeProperty = model.createProperty("type");
        Resource classResource = model.createProperty("Class");

        model.add(model.createStatement(resourceName, typeProperty, classResource));
        return resourceName;
    }

    private void setRange(Resource propName, String typeProperty){
        Property rangeProperty = model.getProperty("range");
        Resource typePropertyValue = model.createResource(typeProperty);
        model.add(model.createStatement(propName, rangeProperty, typePropertyValue));
    }

    private void setDomain(Resource propName, Resource classResource) {
        Property domainProperty = model.createProperty("domain");
        model.add(model.createStatement(propName, domainProperty, classResource));
    }

    private void setDataType(Resource propName){
        Property typeProperty = model.getProperty("type");
        Resource typeResource = model.createResource("DatatypeProperty");
        model.add(model.createStatement(propName, typeProperty, typeResource));
    }

    private void removePrevEvent(Resource resInstance){
        Selector selector = new SimpleSelector(null, null, this.event);
        List<Statement> stmtIterator = model.listStatements(selector).toList();
        for (Statement s: stmtIterator){
            Property prop = model.createProperty(s.getSubject().getNameSpace(), s.getSubject().getLocalName());

            selector = new SimpleSelector(resInstance, prop, (RDFNode) null);
            List<Statement> instanceStatement = model.listStatements(selector).toList();
            for(Statement stmt: instanceStatement){
                model.remove(stmt);
            }
        }
    }

    private boolean checkCanRelate(String refId){
        Resource idResource = model.createResource(refId);
        Property type = model.createProperty("type");
        Selector selector = new SimpleSelector(idResource, type, (RDFNode) null);
        List<Statement> stmtIterator = model.listStatements(selector).toList();
        Statement namedIndividualStatement = model.createStatement(idResource, type, model.createResource("NamedIndividual"));
        if(!stmtIterator.contains(namedIndividualStatement)){
            return false;
        } else {
            stmtIterator.removeIf(s -> s.equals(namedIndividualStatement));
        }
        Property subClassProperty = model.createProperty("subClassOf");
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
        Property typeProperty = model.getProperty("type");
        Resource typeResource = model.createResource("ObjectProperty");
        model.add(model.createStatement(propName, typeProperty, typeResource));
    }

    private void addDataPropertyValue(Resource resourceInstance, String propertyName, Object propertyValue){
        Resource propName = model.createResource(propertyName);
        Property property = model.getProperty(propName.getNameSpace(), propName.getLocalName());
        Literal value = model.createLiteral(String.valueOf(propertyValue));
        model.add(model.createStatement(resourceInstance, property, value));
    }

    private void addDataProperty(Resource classResource, String propertyName, String propertyType) {
        Resource propName = model.createResource(propertyName);
        setRange(propName, propertyType);
        setDomain(propName, classResource);
        setDataType(propName);
    }

    private void setDeviceSubclass(Resource classResource) {
        Property subClassProperty = model.createProperty("subClassOf");
        model.add(model.createStatement(classResource, subClassProperty, device));
    }

}
