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
           .println("Create Lightswitch...").


/* ############### */

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
//{ include("$moiseJar/asl/org-obedient.asl") }
