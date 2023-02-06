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
           makeArtifact(lampId_0, "tools.LampArtifact", [InitialLampState], LampRef);
           .println("Create Lightswitch...");
           ?isPressed(InitialLSState);
           makeArtifact(lsId_0, "tools.LightSwitchArtifact", [InitialLSState, lampId_0], LSRef);
           press [LSRef];
           release[LSRef];
           press[LSRef];
           release[LSRef];
           .println("Created all");
           makeArtifact(service, "tools.ServiceArtifact", [], ServiceRef);
           .println("Query: SELECT ?id WHERE {?subject rdfs:subClassOf :Device . ?id rdf:type ?subject}");
           query("SELECT ?id WHERE {?subject rdfs:subClassOf :Device . ?id rdf:type ?subject}", ResultSet);
           getAtIndex(1, ResultSet, Element);
           getValue("id", Element, Value);
           .println("Query result: ", Value);
           lampState(BeforeState);
           .println(Value, " artifact state: ", BeforeState);
           lookupArtifact(Value, IdArtifact);
           .println("Switch on...");
           switchOn [artifact_id(IdArtifact)];
           lampState(AfterState);
           .println(Value, " artifact state: ", AfterState);.

/* ############### */

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
//{ include("$moiseJar/asl/org-obedient.asl") }
