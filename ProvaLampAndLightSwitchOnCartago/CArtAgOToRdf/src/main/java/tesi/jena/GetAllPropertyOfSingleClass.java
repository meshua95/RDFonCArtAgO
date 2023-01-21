package tesi.jena;

import org.apache.jena.riot.RDFDataMgr;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GetAllPropertyOfSingleClass {

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

        Optional<Resource> resource = Optional.empty();

        StmtIterator stmtIter = model.listStatements(null, null, model.getResource( owlNameSpace + "Class"));
        while (stmtIter.hasNext()){
            Statement s = stmtIter.nextStatement();
            if(s.getSubject().getLocalName().equals("Lamp")) {
                resource = Optional.of(s.getSubject());
            }
        }

        List<Resource> classProperties = new ArrayList<>();
        if(resource.isPresent()){
            Resource res = resource.get();
            StmtIterator iterator = model.listStatements(null, model.getProperty(rdfSchemaNameSpace + "domain"), model.getResource( res.getURI()));
            while(iterator.hasNext()){
                Statement s = iterator.nextStatement();
                classProperties.add(s.getSubject());
            }
        }

        classProperties.forEach(p -> System.out.println(p.getLocalName()));

    }
}
