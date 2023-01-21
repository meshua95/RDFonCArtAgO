package it.unibo.tesi.extension.RDFtoCArtAgO.ttlData;

import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.RDFDataMgr;

import java.util.ArrayList;
import java.util.List;

public class RDFUtils {

    public static final String owlNameSpace = "http://www.w3.org/2002/07/owl#";
    public static final String rdfSchemaNamespace = "http://www.w3.org/2000/01/rdf-schema#";
    public static final String rdfSyntax = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";

    public static List<String> getModelNamespaces (String file){
        List<String> namespaces = new ArrayList<>();
        Model model = getModel(file);

        NsIterator nsIter = model.listNameSpaces();
        while (nsIter.hasNext()){
            String namespace = nsIter.nextNs();
            namespaces.add(namespace);
        }

        return namespaces;
    }

    public static List<String> getOwlClasses(String file){
        Model model = getModel(file);
        List<String> classes = new ArrayList<>();

        String owlClass = owlNameSpace + "Class";

        StmtIterator stmtIter = model.listStatements(null, null, model.getResource(owlClass));
        while (stmtIter.hasNext()){
            Statement s = stmtIter.nextStatement();
            classes.add(s.getSubject().getLocalName());
        }

        return classes;
    }

    //la classe si passa con this.getClass().getSimpleName();
    public static List<String> getClassDataProperties(String file, String className){
        Model model = getModel(file);
        String owlClass = owlNameSpace + "Class";
        String owlDataProperties = owlNameSpace + "DatatypeProperty";
        String syntaxType = rdfSyntax + "type";

        String classUri = "";
        List<String> classProperties = new ArrayList<>();

        StmtIterator stmtIter = model.listStatements(null, null, model.getResource(owlClass));
        while (stmtIter.hasNext()){
            Statement s = stmtIter.nextStatement();
            if(s.getSubject().getLocalName().equals(className)) {
                classUri = s.getSubject().getURI();
            }
        }

        if(!classUri.equals("")) {
            String domain = rdfSchemaNamespace + "domain";
            StmtIterator iterator = model.listStatements(null, model.getProperty(domain), model.getResource(classUri));
            while (iterator.hasNext()) {
                Statement s = iterator.nextStatement();
                classProperties.add(s.getSubject().getLocalName());
            }

            StmtIterator dataPropertiesIterator = model.listStatements(null, model.getProperty(syntaxType), model.getResource(owlDataProperties));
            while (dataPropertiesIterator.hasNext()){
                Statement s = dataPropertiesIterator.nextStatement();

            }
        }
        return classProperties;
    }

    public static String getPropertyRange(){
        /* http://www.semanticweb.org/meshuagalassi/ontologies/2022/11/LampAndLightSwitch#isPressed
        http://www.w3.org/2000/01/rdf-schema#range
        http://www.w3.org/2001/XMLSchema#boolean .*/

        return "";

    }

    public static String getPropertyValue(String file, String propertyName){
        Model model = getModel(file);
        return "";
    }

    private static Model getModel(String file){
        String fileName = "src/main/resources/LampAndLightSwitch.ttl";

        Model model = RDFDataMgr.loadModel(fileName);
        return model;
    }
}
