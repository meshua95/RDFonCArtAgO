package tools;

import cartago.Artifact;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public abstract class SemanticArtifact extends Artifact {

    private SemanticEnvironment environment = null;
    private String artifactClass;
    private String artifactId;

    public void init(Object className, String artifactId){
        environment = SemanticEnvironment.getInstance();
        this.artifactClass = className.getClass().getSimpleName();
        this.artifactId = artifactId;
        setAvailableOperations(className);
    }

    protected cartago.ObsProperty defineObsProperty(String name, Object... values ){
        environment.defineDataProperty(artifactClass, artifactId, name, values);
        return super.defineObsProperty(name, values);
    }

    protected cartago.ObsProperty defineRelationship(String name, Object... values){
        //todo qua devi gestire l'inserimento di una relazione nell'RDF
        return super.defineObsProperty(name, values);
    }

    protected void signal(String type, Object... objs){
       // environment.addSignaledEvent(type, artifactId);
        super.signal(type, objs);

    }

    protected void setAvailableOperations(Object objectDefinition){
        Method[] methods = objectDefinition.getClass().getDeclaredMethods();
        for (Method method : methods) {
            Annotation[] annotations = method.getAnnotations();
            for(Annotation ann: annotations){
                if(ann.annotationType().getSimpleName().equals("OPERATION")){
                    environment.addOperation(method.getName(), artifactClass);
                }
            }
        }

    }

    public String printModel(){
        return environment.printAllStatement();
    }

}
