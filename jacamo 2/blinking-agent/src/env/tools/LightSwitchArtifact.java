package tools;

import cartago.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@ARTIFACT_INFO(
        outports = {
                @OUTPORT(name = "out-1")
        }
)
public class LightSwitchArtifact extends Artifact {

    SemanticEnvironment environment;

    /*PROPERTIES*/
    private final String pressPropertyName = "press";
    private boolean press;

    /*EVENTS*/
    private final String isPressedEvent = "is_pressed";
    private final String isReleasedEvent = "is_released";

    void init(String id, boolean isPressed){
        environment = SemanticEnvironment.getInstance();
        String className = this.getClass().getSimpleName();

        log("creating...");
        this.press = isPressed;
        defineObsProperty(pressPropertyName, this.press); //define a new property that can be observed by agents
        log("created");

        environment.defineDataProperty(className, id, pressPropertyName, this.press);
        availableOperations();

        log(environment.printAllStatement());
    }

    @OPERATION
    void press() {
        ObsProperty prop = getObsProperty(pressPropertyName);
        this.press = true;
        prop.updateValue(this.press);
        try {
            execLinkedOp("out-1","switchOn");
        } catch (Exception e) {
            e.printStackTrace();
        }
        environment.removeEvent(isReleasedEvent, this.getClass().getSimpleName());
        environment.addSignaledEvent(isPressedEvent, this.getClass().getSimpleName());
        signal(isPressedEvent);
        log(environment.printAllStatement());
    }

    @OPERATION void getCurrentState(OpFeedbackParam<Boolean> state) {
        log("getting the state...");
        state.set(this.press);
        log("state retrieved.");
    }

    @OPERATION
    void release(){
        ObsProperty prop = getObsProperty(pressPropertyName);
        this.press = false;
        prop.updateValue(this.press);
        try {
            execLinkedOp("out-1","switchOff");
        } catch (Exception e) {
            e.printStackTrace();
        }
        environment.removeEvent(isPressedEvent, this.getClass().getSimpleName());
        environment.addSignaledEvent(isReleasedEvent, this.getClass().getSimpleName());
        signal(isReleasedEvent);
        log(environment.printAllStatement());
    }

    private void availableOperations(){
        String className = this.getClass().getSimpleName();
        Method[] methods = this.getClass().getDeclaredMethods();
        for (Method method : methods) {
            Annotation[] annotations = method.getAnnotations();
            for(Annotation ann: annotations){
                if(ann.annotationType().getSimpleName().equals("OPERATION")){
                    environment.addOperation(method.getName(), className);
                }

            }
        }
    }

}
