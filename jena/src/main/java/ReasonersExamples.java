import java.io.PrintWriter;
import java.util.Iterator;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.reasoner.Derivation;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.Rule;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

/* http://jena.apache.org/documentation/inference/index.html*/

public class ReasonersExamples {
    private static String derivationFname = "./src/main/resources/reasoner/derivationExample.ttl";
    //private static String NS = "http://schema.org";
    private static String fnameschema = "./src/main/resources/reasoner/familySchema.rdf";
    private static String fnameinstance = "./src/main/resources/reasoner/familyData.rdf";


    public void subProperty(){
        String NS = "http://schema.org/";
        //Creating an empty model
        Model rdfsExample = ModelFactory.createDefaultModel();
        //Adding 2 properties
        Property p = rdfsExample.createProperty(NS, "memberOf");
        Property q = rdfsExample.createProperty(NS, "sameAs");
        //The first property is a subproperty of the second
        rdfsExample.add(p, RDFS.subPropertyOf, q ) ;
        //Educational organisation member of Organisation
        rdfsExample.createResource(NS+"EducationalOrganisation").addProperty(p,NS+ "Organisation");
        //Apply the inference engine over the model
        InfModel inf = ModelFactory.createRDFSModel(rdfsExample);
        //The reasoner concluded that EducationalOrganisation is same as Organisation because
        //memberof is subPropertyOf sameAs
        Resource a = inf.getResource(NS+"EducationalOrganisation");
        System.out.println("Statement: " + a.getProperty(q));
    }

    public void derivation(){
        Model data = FileManager.get().loadModel("./src/main/resources/reasoner-demo/dataset.n3");
        Reasoner reasoner = new GenericRuleReasoner( Rule.rulesFromURL( "./src/main/resources/reasoner-demo/rules.txt" ) );
        reasoner.setDerivationLogging(true);
        InfModel inf = ModelFactory.createInfModel(reasoner, data);
        PrintWriter out = new PrintWriter(System.out);
        for (StmtIterator i = inf.listStatements(); i.hasNext(); ) {
            Statement s = i.nextStatement();
            System.out.println("Statement is " + s);
            for (Iterator id = inf.getDerivation(s); id.hasNext(); ) {
                Derivation deriv = (Derivation) id.next();
                deriv.printTrace(out, true);        }     }
        out.flush();
    }

    public void rdfs_reasoner(){
         String NS = "urn:x-hp:eg/";
         Model schema = FileManager.get().loadModel(fnameschema);
         Model data = FileManager.get().loadModel(fnameinstance);
         InfModel infmodel = ModelFactory.createRDFSModel(schema, data);

        Resource colin = infmodel.getResource("urn:x-hp:eg/colin");
        System.out.println("colin has types:");
        RDFNode n =  null;
        printStatements(colin, RDF.type, n, infmodel);

        Resource Person = infmodel.getResource("urn:x-hp:eg/Person");
        System.out.println("\nPerson has types:");
        printStatements(Person, RDF.type, n, infmodel); 	}

    public static void printStatements(Resource r, Property p, RDFNode o, Model m)  {
        for (StmtIterator i = m.listStatements(r, p, o ); i.hasNext(); ) {
            Statement s = i.nextStatement();
            System.out.println(s); }
    }


    public void owl_reasoner(){

    }



        public static void main( String[] args ) {
        new ReasonersExamples().subProperty();
        //new ReasonersExamples().derivation();
        //new ReasonersExamples().rdfs_reasoner();

    }
}
