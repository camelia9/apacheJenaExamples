import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.util.FileManager;

public class JenaMain {

    public static final String TRAVEL_NAMESPACE = "http://www.owl-ontologies.com/travel.owl#";

    public static void main(String[] args) {
        helloWorld();
        extendedJenaReasoningExample();
    }

    private static void extendedJenaReasoningExample() {


        Model ontology = FileManager.get().loadModel("./src/main/resources/travel.owl");

        Resource fourSeasons = ontology.getResource(TRAVEL_NAMESPACE + "FourSeasons");
        Property hasRating = ontology.getProperty(TRAVEL_NAMESPACE + "hasRating");
        System.out.println("hasrating direct property: " + fourSeasons.getProperty(hasRating));


        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        InfModel infOntology = ModelFactory.createInfModel(reasoner, ontology);
        Resource infFourSeasons = infOntology.getResource(TRAVEL_NAMESPACE + "FourSeasons");
        Property infHasRating = infOntology.getProperty(TRAVEL_NAMESPACE + "hasRating");
        System.out.println("FourSeasons is a LuxuryHotel, hence: " + infFourSeasons.getProperty(infHasRating));
    }

    private static void helloWorld() {

        String NAMESPACE = "http://wade-test/";
        Model ontology = ModelFactory.createDefaultModel();

        Resource person = ontology.createResource(NAMESPACE + "Person");
        Resource cat = ontology.createResource(NAMESPACE + "Cat");
        Property hasPet = ontology.createProperty(NAMESPACE, "has_pet");
        person.addProperty(hasPet, cat);
        System.out.println("Statement: " + person.getProperty(hasPet));
    }

}

