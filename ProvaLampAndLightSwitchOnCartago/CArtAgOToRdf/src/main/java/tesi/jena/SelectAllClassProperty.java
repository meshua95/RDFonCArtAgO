package tesi.jena;

import org.apache.jena.riot.RDFDataMgr;

import java.util.ArrayList;
import java.util.List;

public class SelectAllClassProperty {

    public static void main(String[] args) {
        String fileName = "src/main/resources/LampAndLightSwitch.ttl";

        String owlNameSpace = "";
        String rdfSchemaNameSpace = "";

        Model model = RDFDataMgr.loadModel(fileName);

        model.listNameSpaces();
        NsIterator nsIter = model.listNameSpaces();
        while (nsIter.hasNext()){
            String namespace = nsIter.nextNs();
            if(namespace.contains("owl")){
                owlNameSpace = namespace;
            } else if (namespace.contains("rdf-schema")){
                rdfSchemaNameSpace = namespace;
            }
        }

        List<Resource> resources = new ArrayList<>();

        StmtIterator stmtIter = model.listStatements(null, null, model.getResource( owlNameSpace + "Class"));
        while (stmtIter.hasNext()){
            Statement s = stmtIter.nextStatement();

            resources.add(s.getSubject());
        }

        //prendo tutte le proprietà di ogni classe
        for(Resource r: resources){
            StmtIterator iterator = model.listStatements(null, model.getProperty(rdfSchemaNameSpace + "domain"), model.getResource( r.getURI()));
            while(iterator.hasNext()){
                Statement s = iterator.nextStatement();
                System.out.println(s.toString());
            }
        }

    }
}
