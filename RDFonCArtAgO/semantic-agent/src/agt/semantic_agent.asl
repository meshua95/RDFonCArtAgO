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
           ?isOn(InitialLampState);
           makeArtifact(lampId_0, "tools.LampArtifact", [lampId_0, InitialLampState], LampRef);
           .println("Create Lightswitch...");
           ?isPressed(InitialLSState);
           makeArtifact(lsId_0, "tools.LightSwitchArtifact", [lsId_0, InitialLSState, lampId_0], LSRef);
           .println("Created all");
           makeArtifact(service, "tools.ServiceArtifact", [], ServiceRef);
           query("SELECT ?id WHERE {?subject rdfs:subClassOf :Device . ?id rdf:type ?subject}", ResultSet);
           getAtIndex(1, ResultSet, Element);
           getValue("id", Element, Value);
           println(Value);
           lookupArtifact(Value, IdArtifact);
           switchOn [artifact_id(IdArtifact)].

/* ############### */

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
//{ include("$moiseJar/asl/org-obedient.asl") }
