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
        <-  makeArtifact(service, "tools.ServiceArtifact", [], ServiceRef);
            .println("Query: SELECT ?id WHERE {?subject rdfs:subClassOf :Device . ?id rdf:type ?subject}");
            query("SELECT ?id WHERE { ?id rdf:type :Lamp }", ResultSet);
            getAtIndex(0, ResultSet, Element);
            getValue("id", Element, Value);
            .println("Query result: ", Value);
            lookupArtifact(Value, IdArtifact);
            lampState(FirstState) [artifact_id(IdArtifact)];
            .println("State: ", FirstState);
            .println("Switch on...");
            switchOn [artifact_id(IdArtifact)];
            lampState(SecondState) [artifact_id(IdArtifact)];
            .println("State: ", SecondState);.

/* ############### */

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
//{ include("$moiseJar/asl/org-obedient.asl") }