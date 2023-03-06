package semanticDefinition;

import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFWriter;
import org.apache.jena.riot.RIOT;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface SemanticEnvironment {

    /**Create a resource
     * @param namespace
     * @param resourceName
     */
    void createResource(String namespace, String resourceName);

    /**Create a resource in default namespace
     * @param resourceName
     */
    void createResource(String resourceName);

    /**Create an instance
     * @param namespace
     * @param artifactId
     * @param artifactClass
     */
    void createInstance(String namespace, String artifactId, String artifactClass);

    /**Create an instance in default namespace
     * @param artifactId
     * @param artifactClass
     */
    void createInstance(String artifactId, String artifactClass);

    /**Add data property value to an artifact instance
     * @param resourceName
     * @param resourceId
     * @param propertyName
     * @param propertyValue
     */
    void defineDataProperty(String namespace, String resourceName, String resourceId, String propertyName, String type, Object propertyValue);

    /**Add data property value to an artifact instance and its type
     * @param resourceName
     * @param resourceId
     * @param propertyName
     * @param type
     * @param propertyValue
     */
    void defineDataProperty(String resourceName, String resourceId, String propertyName, String type, Object propertyValue);

    /**
     * Update data property value to an artifact instance
     * @param resourceId
     * @param propertyName
     * @param propertyValue
     */
    void updateDataProperty(String resourceId, String propertyName, Object propertyValue);

    /**
     * Update data property value to an artifact instance
     * @param namespace
     * @param resourceId
     * @param propertyName
     * @param propertyValue
     */
    void updateDataProperty(String namespace, String resourceId, String propertyName, Object propertyValue);

    /**Add an operation to an artifact in specific namespace
     * @param namespace
     * @param operationName
     * @param classResourceName
     */
    void addOperation(String namespace, String operationName, String classResourceName);

    /**Add an operation to an artifact in default namespace
     * @param operationName
     * @param classResourceName
     */
    void addOperation(String operationName, String classResourceName);

    /**Add an object property to an artifact
     * @param namespace
     * @param name
     * @param refId
     * @param artifactId
     * @param artifactClass
     */
    void addObjectProperty(String namespace, String name, String refId, String artifactId, String artifactClass);

    /**Add an object property to an artifact in default namespace
     * @param name
     * @param refId
     * @param artifactId
     * @param artifactClass
     */
    void addObjectProperty(String name, String refId, String artifactId, String artifactClass);

    /**Get created model
     * @return Model
     */
    Model getModel();

    /**Create the event resource and attach the event as a property of the specific artifact
     * @param namespace
     * @param eventName
     * @param resourceId
     * @param artifactClass
     */
    void addSignaledEvent(String namespace, String eventName, String resourceId, String artifactClass);

    /**Create the event resource and attach the event as a property of the specific artifact in default namespace
     * @param eventName
     * @param resourceId
     * @param artifactClass
     */
    void addSignaledEvent(String eventName, String resourceId, String artifactClass);

    /**Add existing namespace and its prefix
     * @param prefix
     * @param namespace
     */
    void addNamespace(String prefix, String namespace);

    /**Remove an artifact instance
     * @param namespace
     * @param resourceId
     */
    void removeInstance(String namespace, String resourceId);

    /**Remove an artifact instance defined in default workspace
     * @param resourceId
     */
    void removeInstance(String resourceId);

    /**
     * remove an object property
     * @param namespace
     * @param name
     * @param refId
     * @param artifactId
     */
    void removeObjectProperty(String namespace, String name, String refId, String artifactId);

    /**
     * remove an object property
     * @param name
     * @param refId
     * @param artifactId
     */
    void removeObjectProperty(String name, String refId, String artifactId);

    /**Create file.ttl with all statement defined in model
     */
    void printAllStatement();


}
