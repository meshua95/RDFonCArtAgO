import org.apache.jena.rdf.model.*;
import tools.SemanticEnvironment;

public class SemanticEnvironmentTest {

    public static void main(String[] args) {
        SemanticEnvironment environment = SemanticEnvironment.getInstance();
        Resource res = environment.getOwlObject("ClasseDiProva");
        System.out.println(environment.printAllStatement());
        System.out.println(res.toString());
    }


}
