package it.unibo.tesi.examplesJena;

import org.apache.jena.vocabulary.*;
import org.apache.jena.rdf.model.*;

public class Tutorial01 {

    // some definitions
    static String personURI = "http://somewhere/JohnSmith";
    static String givenName    = "John";
    static String familyName   = "Smith";
    static String fullName     = givenName + " " + familyName;

    public static void main(String[] args) {
        // create an empty model
        Model model = ModelFactory.createDefaultModel();

        // create the resource
        //   and add the properties cascading style
        Resource johnSmith = model.createResource(personURI)
                .addProperty(VCARD.FN, fullName)
                .addProperty(VCARD.N, model.createResource()
                        .addProperty(VCARD.Given, givenName)
                        .addProperty(VCARD.Family, familyName));

        /*Resource johnSmith = model.createResource(personURI);
        // add the property
        johnSmith.addProperty(VCARD.FN, fullName);
        System.out.println("Full Name = " + johnSmith.getProperty(VCARD.FN)); */

        // list the statements in the graph
        StmtIterator iter = model.listStatements();

        // print out the predicate, subject and object of each statement
        while (iter.hasNext()) {
            Statement stmt = iter.nextStatement();         // get next statement
            Resource subject = stmt.getSubject();   // get the subject
            Property predicate = stmt.getPredicate(); // get the predicate
            RDFNode object = stmt.getObject();    // get the object

            System.out.print(subject.toString());
            System.out.print(" " + predicate.toString() + " ");
            if (object instanceof Resource) {
                System.out.print(object.toString());
            } else {
                // object is a literal
                System.out.print(" \"" + object.toString() + "\"");
            }
            System.out.println(" .");
        }

        // prendi una risorsa dato il suo uri
        Resource vcard = model.getResource(personURI);
        System.out.println("Full Name = " + vcard.getProperty(VCARD.FN));

        //ottenere il valore di una proprietà, non l'intero statement
        Literal fullName = (Literal) vcard.getProperty(VCARD.FN).getObject();
        System.out.println("Name = " + fullName.toString());

        Resource name = vcard.getProperty(VCARD.N).getResource();
        System.out.println("Nome = " + name);
        String fullName2 = vcard.getProperty(VCARD.FN).getString();
        System.out.println("Full Name = " + fullName2);

        //iterare su più proprietà con lo stesso nome
        johnSmith.addProperty(VCARD.NICKNAME, "Smith");
        johnSmith.addProperty(VCARD.NICKNAME, "Adman");

        StmtIterator iterator = johnSmith.listProperties(VCARD.NICKNAME);
        while (iterator.hasNext()) {
            System.out.println(" " + iterator.nextStatement()
                    .getObject()
                    .toString());
        }

    }
}