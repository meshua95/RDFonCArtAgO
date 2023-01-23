/* Initial beliefs */
    lamp(null).
    lightSwitch(null).
    isOn(false).
    isPressed(false).
/* ############### */

/* Initial goals */
!start.
/* ############### */

/* Plans. */
    /* Proactive plans. */
    +!start 
        <- .println("CreateLamp...");
           ?isOn(InitialState);
           makeArtifact(lampId_0, "tools.LampArtifact", [lampId_0, InitialState], LampRef);
           .println("Create Lightswitch...");
           ?isPressed(InitialState);
           makeArtifact(lightSwitch_0, "tools.LightSwitchArtifact", [lightSwitch_0, InitialState], LSRef);
           linkArtifacts(LSRef,"out-1",LampRef);
           focus(LSRef);
           -+lightSwitch(LSRef);
           getCurrentState(LSState);
           -+isPressed(LSState);
           .wait(1000);
           if (LSState) { release; } else { press; }
           -+isPressed(LSState);           //Update the local belief "isOn" of the agent
           .wait(1000);                           //Wait for 1 second
           if (LSState) { release; } else { press; }.


/* ############### */

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
//{ include("$moiseJar/asl/org-obedient.asl") }
