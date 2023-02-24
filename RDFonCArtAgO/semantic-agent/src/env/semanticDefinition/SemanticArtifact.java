package semanticDefinition;

import cartago.Artifact;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
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

    protected cartago.ObsProperty defineRelationship(String name, String refId){
        if(namespace.isEmpty()){
            environment.addObjectProperty(name, refId, artifactId, artifactClass);
        } else {
            environment.addObjectProperty(namespace, name, refId, artifactId, artifactClass);
        }

        return super.defineObsProperty(name, refId);
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
