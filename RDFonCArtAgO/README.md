# JaCaMo Example

## Getting Started
* `Visual Studio Code` is best for opening JaCaMo projects, once the related extensions are installed.

## JaCaMo Components
* `Agents`: an agent contains part of the business logic of the system. An agent is defined through:
    + `Beliefs`: what the agent knows of the environment. The agent may become out of sync with the environment.
    + `Goals`: what the agent wants to achieve.
    + `Plans`: what the agent is able to do. Each plan is a sequence of actions that are executed in order to get closer to one of the agent's goal.
* `Environment`: the environment contains functionalities and services that are available for the agents to use. These services are defined via `artifacts`.
* `Organizations`: groups of agents with different roles.

## JaCaMo Project Structure
* `src/agt`: contains the definition of the agents of the system
* `src/env`: contains the definition of the artifacts of the system
* `src/org`: contains the definition of the organizations in the system
* `src/<project-name>.jcm`: contains the definition of the Multi-Agent System (MAS).

## Learn
1. Look at `src/multi-agent-system.jcm`. It provides the implementation of a system which controls the blinking of a lamp. This file must declare all the agents of the system. In this case, there is only a single agent: the `agt/blinking_agent.asl`.
2. Look at `agt/blinking_agent.asl`. It provides the implementation of an agent controlling the blinking of a lamp.
    * At the start of the file, there are the `initial beliefs` of the agent, that is what the agent knows of the environment when it is created.
    + Then, there are the `initial goals` of the agent, that is what the agent will do when it is created. These initial goals usually trigger one of the plans of the agent. In this case, the plan `start` is triggered.
    + Finally, there are the `plans` of the agent. The plans have been divided into two categories:
        - `Proactive plans` (+!plan): these plans are triggered willingly by the agent.
            - `+!start`: initializes the agent. In particular, it creates an artifact of type `tools.LampArtifact` with id `lampId_0`. Then, it calls the method `tools.LampArtifact.getCurrentState`, storing the result in the variable `LampState`. Finally, it updates the local belief `isOn` with the value of `LampState`.
            - `+createLampArtifact(I_Id)`: creates an artifact of type `tools.LampArtifact` with id `I_Id`. In particular, it sets the variable `InitialState` to the current value of the belief `isOn`. Then, it creates the artifact by passing `InitialState` as a parameter of the method `tools.LampArtifact.init`, receiving the reference for that artifact in the variable `LampRef`. Finally, it starts observing the lamp `LampRef` and updates the local belief `lamp` to the value of `LampRef`.
        - `Reactive plans` (+event): these plans are triggered when the agent is notified of a certain event or signal.
            - `+state(LampState)`: executed when the property `state` is updated to the value `LampState` (`LampState` contains the new value of `state`). This only reacts to the changes of the property `state` among the artifacts observed by this agent (in this case only the single lamp). When the `state` of the lamp changes, updates the local belief `isOn` of the agent, waits 1 seconds and then switches the lamp to the other state (if it was on then it is switched off and vice-versa).
3. Look at `env/tools/LampArtifact.java`. It provides the implementation of an artifact of type `tools.LampArtifact`.
    * An `Artifact` must provide a `init` method which is used to initialize the artifact. In this case, the initialization creates a new property `state` that can be observed by some agents.
    * An `Artifact` may also provide a set of `OPERATION`s which are methods that can be called by some agents. In this case, the lamp can be switched on and off and can be asked for its current state.

## Deploy
* At the root of the project (`jacamo-example`), execute the following command: `./gradlew :blinking-agent:run` or  `./gradlew.bat :blinking-agent:run`

## Explore
* Feel free to modify the project locally, exploring new solutions.
* Feel free to create a new Multi-Agent System:
    + Start by copying the folder `blinking-agent` into `jacamo-example`, renaming it to your likings (in this example, it will be renamed to `jacamo-experiment`).
    + Include the folder `jacamo-experiment` as a subproject in the file `jacamo-example/settings.gradle.kts`
    + Modify it as needed (if you rename the file `*.jcm`, care to update also the `build.gradle` accordingly)
    + Run it by executing the command `./gradlew :jacamo-experiment:run` or  `./gradlew.bat :jacamo-experiment:run` from the folder `jacamo-example`