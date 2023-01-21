/* Initial beliefs */
    lamp(null).
    isOn(false).
/* ############### */

/* Initial goals */
!start.
/* ############### */

/* Plans. */
    /* Proactive plans. */
    +!start
        <- .println("Initializing...");             //internal method of the agent
           !createSemanticLamp(lamp_01);           //triggering the plan "createLampArtifact" defined below
           .println("Obtaining artifact state...");
           getCurrentState(LampState);              //calling method "getCurrentState" of the first available artifact (the one created above, since it's the only one)
           .println("Updating local belief...");
           -+isOn(LampState);                       //updating local belief "isOn" of the agent with the value stored in the variable "LampState"
           .println("Initialized.").

    +!createSemanticLamp(I_Id)          //Create a lamp artifact with artifact id equals to "I_Id". The parameters of a plan may be either inputs or outputs (I_: input, O_: output)
        <-  .println("Creating lamp artifact...");
            ?isOn(InitialState);        //Get a value from a local belief. If the current belief is "isOn(X)" then InitialState:=X.
                                        //Create an artifact with id I_Id, of class tools.LampArtifact (from /env),
                                        //with parameters [InitialState] for the init method.
                                        //A reference of the artifact is stored in LampRef as an output.
            makeArtifact(I_Id, "tools.SemanticLamp", [InitialState], LampRef);
            focus(LampRef);             //Start observing the artifact just created (receiving notifications about its observable properties)
            -+lamp(LampRef);            //Update the local belief "lamp" of the agent
            .println("Lamp artifact created: ", I_Id).

    /* Reactive plans. */
    +state(LampState)                   //Triggered when an observable property called "state" in updated among the observed artifacts (in this case there's only one lamp)
        <- .println("Lamp state: ", LampState);
           -+isOn(LampState);           //Update the local belief "isOn" of the agent
           .wait(1000);                 //Wait for 1 second
           if (LampState) { turnOff; } else { turnOn; }.     //If "the lamp is on" then "switch it off" else "switch it on"

/* ############### */

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
//{ include("$moiseJar/asl/org-obedient.asl") }
