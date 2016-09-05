package uk.soton.examples.nlp;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import edu.knowitall.ollie.Ollie;
import edu.knowitall.ollie.OllieExtraction;
import edu.knowitall.ollie.OllieExtractionInstance;
import edu.knowitall.tool.parse.MaltParser;
import edu.knowitall.tool.parse.graph.DependencyGraph;

/** This is an example class that shows one way of using Ollie from Java. */
public class JavaOllieWrapper {
    // the extractor itself
    private Ollie ollie;

    // the parser--a step required before the extractor
    private MaltParser maltParser;

    // the path of the malt parser model file
    private static final String MALT_PARSER_FILENAME = "engmalt.linear-1.7.mco";

    public JavaOllieWrapper() throws MalformedURLException {
        // initialize MaltParser
    File modelfile=new File("model",MALT_PARSER_FILENAME);
    if(!modelfile.exists())
    {
    	try {
			downloadFile(new URL("http://www.maltparser.org/mco/english_parser/engmalt.linear-1.7.mco"), "model/engmalt.linear-1.7.mco");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
    	
    }
        maltParser = new MaltParser(modelfile);

        // initialize Ollie
        ollie = new Ollie();
    }

    /**
     * Gets Ollie extractions from a single sentence.
     * @param sentence
     * @return the set of ollie extractions
     */
    public Iterable<OllieExtractionInstance> extract(String sentence) {
        // parse the sentence
        DependencyGraph graph = maltParser.dependencyGraph(sentence);

        // run Ollie over the sentence and convert to a Java collection
        Iterable<OllieExtractionInstance> extrs = scala.collection.JavaConversions.asJavaIterable(ollie.extract(graph));
        return extrs;
    }

    public static void main(String args[]) throws MalformedURLException {
        // initialize
        JavaOllieWrapper ollieWrapper = new JavaOllieWrapper();

        // extract from a single sentence.
        String sentence = "President Obama will meet with Congressional leaders on Friday, and House Republicans summoned lawmakers back for a Sunday session, in a last-ditch effort to avert a fiscal crisis brought on by automatic tax increases and spending cuts scheduled to hit next week.";
        Iterable<OllieExtractionInstance> extrs = ollieWrapper.extract(sentence);

        // print the extractions.
        for (OllieExtractionInstance inst : extrs) {
            OllieExtraction extr = inst.extr();
            System.out.println(extr.arg1().text()+"\t"+extr.rel().text()+"\t"+extr.arg2().text());
        }
    }
    public void downloadFile(URL link, String fileName) throws IOException
    {

		 
		
    //Code to download
		 InputStream in = new BufferedInputStream(link.openStream());
		 ByteArrayOutputStream out = new ByteArrayOutputStream();
		 byte[] buf = new byte[1024];
		 int n = 0;
		 while (-1!=(n=in.read(buf)))
		 {
		    out.write(buf, 0, n);
		 }
		 out.close();
		 in.close();
		 byte[] response = out.toByteArray();

		 FileOutputStream fos = new FileOutputStream(fileName);
		 fos.write(response);
		 fos.close();
    //End download code
		 
		 System.out.println("Finished");
    }
}
