package semanticDefinition;

import cartago.Artifact;
import cartago.ObsProperty;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class SemanticArtifact extends Artifact {

    private SemanticEnvironmentImpl environment = null;

    private String namespace = "";
    private String artifactClass;
    private String artifactId;

    public void init(Object className, String resourcePrefix, String resourceNamespace, String resourceName, String artifactId){
        environment = SemanticEnvironmentImpl.getInstance();
        environment.addNamespace(resourcePrefix, resourceNamespace);
        this.namespace = resourceNamespace;
        this.artifactClass = resourceName;
        this.artifactId = artifactId;
        environment.createResource(resourceNamespace, artifactClass);
        environment.createInstance(resourceNamespace, artifactId, artifactClass);
        setAvailableOperations(className);
    }

    public void init(Object className, String resourceName, String artifactId){
        environment = SemanticEnvironmentImpl.getInstance();
        this.artifactClass = resourceName;
        this.artifactId = artifactId;
        environment.createResource(artifactClass);
        environment.createInstance(artifactId, artifactClass);
        setAvailableOperations(className);
    }

    public void init(Object className, String artifactId){
        init(className, className.getClass().getSimpleName(), artifactId);
    }

    protected cartago.ObsProperty defineObsProperty(String name, String type, Object value ){
        if(namespace.isEmpty()) {
            environment.defineDataProperty(artifactClass, artifactId, name, type, value);
        } else {
            environment.defineDataProperty(namespace, artifactClass, artifactId, name, type, value);
        }
        return super.defineObsProperty(name, value);
    }

    private static final String RELATIONSHIP ="connectTo";

    protected cartago.ObsProperty defineRelationship(String refId){
        if (namespace.isEmpty()) {
            environment.addObjectProperty(RELATIONSHIP, refId, artifactId, artifactClass);
        } else {
            environment.addObjectProperty(namespace, RELATIONSHIP, refId, artifactId, artifactClass);
        }
        return super.defineObsProperty(RELATIONSHIP, refId);
    }

    protected void addRelationship(String idToConnect){
        if (namespace.isEmpty()) {
            environment.addObjectProperty(RELATIONSHIP, idToConnect, artifactId, artifactClass);
        } else {
            environment.addObjectProperty(namespace, RELATIONSHIP, idToConnect, artifactId, artifactClass);
        }
        ObsProperty connections = super.getObsProperty(RELATIONSHIP);
        String[] newConnectionsList = (String[]) connections.getValues();
        newConnectionsList[connections.getValues().length+1] = idToConnect;
        connections.updateValue(newConnectionsList);
    }

    protected void removeRelationship(String idToConnect){
        if (namespace.isEmpty()) {
            environment.removeObjectProperty(RELATIONSHIP, idToConnect, artifactId, artifactClass);
        } else {
            environment.removeObjectProperty(namespace, RELATIONSHIP, idToConnect, artifactId);
        }
        ObsProperty connections = super.getObsProperty(RELATIONSHIP);
        String[] connectionsList = (String[]) connections.getValues();
        List<String> newConnectionList = new ArrayList<>();
        for(String s: connectionsList){
            if(!s.equals(idToConnect)){
                newConnectionList.add(s);
            }
        }
        connections.updateValue(newConnectionList.toArray());
    }
    public void updateValue(String property, Object value){
        ObsProperty state = getObsProperty(property);
        state.updateValue(value);
        if(namespace.isEmpty()){
            environment.updateDataProperty(this.artifactId, property, value);
        } else {
            environment.updateDataProperty(namespace, this.artifactId, property, value);
        }
    }

    protected void signal(String type, Object... objs){
        if(namespace.isEmpty()){
            environment.addSignaledEvent(type, artifactId, artifactClass);
        } else {
            environment.addSignaledEvent(namespace, type, artifactId, artifactClass);
        }

        super.signal(type, objs);
        environment.printAllStatement();
    }

    private void setAvailableOperations(Object objectDefinition){
        List<Method> methods = Arrays.asList(objectDefinition.getClass().getDeclaredMethods());
        methods.forEach(m -> {
            Annotation[] annotations = m.getAnnotations();
            for(Annotation ann: annotations){
                if(ann.annotationType().getSimpleName().equals("OPERATION")){
                    if(namespace.isEmpty()){
                        environment.addOperation(m.getName(), artifactClass);
                    } else {
                        environment.addOperation(namespace, m.getName(), artifactClass);
                    }
                }
            }
        });
    }

    public void dispose(){
        environment.removeInstance(this.getId().getName());
        environment.printAllStatement();
        super.dispose();
    }

}
