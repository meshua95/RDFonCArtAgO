package tools;

import cartago.Artifact;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class SemanticArtifact extends Artifact {

    private SemanticEnvironment environment = null;
    private String artifactClass;
    private String artifactId;

    public void init(String className, String artifactId){
        environment = SemanticEnvironment.getInstance();
        this.artifactClass = className;
        this.artifactId = artifactId;
    }

    protected cartago.ObsProperty defineObsProperty(String name, Object... values ){
        environment.defineDataProperty(artifactClass, artifactId, name, values);
        return super.defineObsProperty(name, values);
    }

    protected void signal(String type, Object... objs){
        environment.addSignaledEvent(type, artifactId);
        super.signal(type, objs);
    }

    protected void availableOperations(Object objectDefinition){
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

}
