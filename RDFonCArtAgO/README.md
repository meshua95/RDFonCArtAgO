# CArtAgO Example

## JaCaMo Components
* `Agents`: an agent contains part of the business logic of the system.
* `Environment`: the environment contains functionalities and services that are available for the agents to use. These services are defined via `artifacts`.
* `Organizations`: groups of agents with different roles.

## JaCaMo Project Structure
* `src/agt`: contains the definition of the agents of the system
* `src/env`: contains the definition of the artifacts of the system
* `src/<project-name>.jcm`: contains the definition of the Multi-Agent System (MAS).

## Learn
1. Look at `src/multi-agent-system.jcm`. It provides the implementation of a system. This file must declare all the agents of the system. In this case, there is only a single agent: the `agt/semantic_agent.asl`.
2. Look at `agt/semantic_agent.asl`. It provides the implementation of an agent creating three artifact and query them.
    * At the start of the file, there are the `initial beliefs` of the agent, that is what the agent knows of the environment when it is created.
    + Then, there are the `initial goals` of the agent, that is what the agent will do when it is created. These initial goals usually trigger one of the plans of the agent. In this case, the plan `start` is triggered.
    + Finally, there are the `plans` of the agent.
        - `+!start`: initializes the agent. In particular, it creates an artifact of type `tools.LampArtifact` with id `lampId_0`, an artifact of type `tools.LightSwitchArtifact` with id `lsId_0` and an artifact of type `tools.ServiceArtifact` with id `ServiceRef`. Then, it calls the method `tools.ServiceArtifact.query` to get LampArtifact name. Finally, it calls the method `tools.LampArtifact.switchOn` to change `lampId_0` state.
3. Look at `env/tools/LampArtifact.java`. It provides the implementation of an artifact. It extends abstract class SemantifArtifact that wrap Artifact and provide some methods which is used to create and handle a knowledge graph that that describes the environment at the knowledge level.


## Deploy
* At the root of the project (`RDFonCArtAgO`), execute the following command: `./gradlew :semantic-agent:run` or  `./gradlew.bat :semantic-agent:run`