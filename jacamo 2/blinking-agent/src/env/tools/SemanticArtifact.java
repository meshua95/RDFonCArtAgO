package tools;

import cartago.Artifact;
import com.fasterxml.jackson.databind.util.Annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public abstract class SemanticArtifact extends Artifact {

    private SemanticEnvironment environment = null;
    private String artifactClass;
    private String artifactId;

    public void init(Object className, String artifactId){
        environment = SemanticEnvironment.getInstance();
        this.artifactClass = className.getClass().getSimpleName();
        this.artifactId = artifactId;
        environment.createResource(artifactClass);
        setAvailableOperations(className);
        environment.addSignaledEvent("is_on", artifactId, artifactClass);
    }

    protected cartago.ObsProperty defineObsProperty(String name, Object... values ){
        environment.defineDataProperty(artifactClass, artifactId, name, values);
        return super.defineObsProperty(name, values);
    }

    protected cartago.ObsProperty defineRelationship(String name, String refId){
        //todo qua devi gestire l'inserimento di una relazione nell'RDF
        environment.addObjectProperty(name, refId, artifactId, artifactClass);
        return super.defineObsProperty(name, refId);
    }

    protected void signal(String type, Object... objs){
        environment.addSignaledEvent(type, artifactId, artifactClass);
        super.signal(type, objs);

    }

    protected void setAvailableOperations(Object objectDefinition){
        List<Method> methods = Arrays.asList(objectDefinition.getClass().getDeclaredMethods());
        methods.forEach(m -> {
            Annotation[] annotations = m.getAnnotations();
            for(Annotation ann: annotations){
                if(ann.annotationType().getSimpleName().equals("OPERATION")){
                    environment.addOperation(m.getName(), artifactClass);
                }
            }
        });
    }

    public String printModel(){
        return environment.printAllStatement();
    }

}
