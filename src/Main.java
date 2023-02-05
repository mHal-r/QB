import javax.xml.parsers.DocumentBuilderFactory;
import java.util.*; 
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import edu.stanford.nlp.*;
import edu.stanford.nlp.coref.CorefCoreAnnotations;

//import edu.stanford.nlp.coref.data.CorefChain;
//import edu.stanford.nlp.io.*;
//import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.pipeline.*;
//import edu.stanford.nlp.semgraph.SemanticGraph;
//import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
//import edu.stanford.nlp.semgraph.SemanticGraphEdge;
//import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
//import edu.stanford.nlp.trees.*;
//import edu.stanford.nlp.util.*;
import javax.xml.*; 
public class Main {
	public static TreeMap<String, String> encyclopedia = new TreeMap<String, String>(); 
	//stores all the words ever 
	//public static HashMap<String, String> testingdict = new HashMap<String, String>(); 
	public static ArrayList<String> testkeys = new ArrayList<String>(); 
	public static ArrayList<String> testdata = new ArrayList<String>();
	public static TreeMap<String, ArrayList<String>> categories = new TreeMap<String, ArrayList<String>> (); //use ner to group things later
	public static Properties props = new Properties();
	public static StanfordCoreNLP pipeline ; 
  //  public static StanfordCoreNLP pipeline;
	//update with a couple of lists attached to a couple of keywords 
	 public static void main(String[] args) {
 
      try {
    	 props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse");
    	 pipeline = new StanfordCoreNLP(props); 
         File inputFile = new File("Wikipedia-Physics-Articles.xml");
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.parse(inputFile);
         doc.getDocumentElement().normalize();
         System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
         NodeList nList = doc.getElementsByTagName("page");
         System.out.println("----------------------------");
 
         for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
//            	NodeList chlist = nNode.getChildNodes(); 
//            	for(int i = 0; i< chlist.getLength(); i++) {
//            		 Node lNode = nList.item(temp);
//                     if (lNode.getNodeType() == Node.ELEMENT_NODE) {
//                    	 Element eElement = (Element) lNode; 
//                    	  System.out.println("Info: "
//                                  + eElement.getTagName()) ;
//                     }
//            	}
               Element eElement = (Element) nNode;
//               System.out.println("\nNode Name =" + nNode.getNodeName()+ " [OPEN]");  
//               System.out.println("Node Content =" + nNode.getTextContent());  
               System.out.println("Title : "
                   + eElement.getElementsByTagName("title").item(0).getTextContent()) ;
              
               encyclopedia.put(eElement.getElementsByTagName("title").item(0).getTextContent(), eElement.getElementsByTagName("text").item(0).getTextContent());
            }
            
         }
        
         
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
   public static void cleanData() {
	   String doc = ""; 
	   for(int i = 0; i< encyclopedia.size(); i++) {
		   doc = encyclopedia.get(i); 
		   CoreDocument newdoc= new CoreDocument(doc); 
		   pipeline.annotate(newdoc); 
		   // nouns 
		   //group nouns w dependencies 
		   // maybe later do ner 
	   }
	
	   //do stanford nlp stuff 
	   //extract nouns and whatnot 
	   //add categories 
	   
   }
   private static String calculateList (String keyword, String tryword) {
	   // we are getting tryword from QuizBot 
	   // which has a Qb question database, searches the questions with our keyword in them, identifies a noun 
	   // and sends it over 
	  //lemmatize the keyword and the tryword i suppose  
	    
		   
	   return null; 
   }
   private static ArrayList<String> calculateList (String keyword) {
	   // getting random trywords from the articles 
	  //lemmatize the keyword and the tryword i suppose  
	   String srcarticle = encyclopedia.get(keyword); 
	   ArrayList<String> trys = new ArrayList<String>(); 
	   SortedMap<Double, String> rankedwords
       = new TreeMap<Double, String>();
	    for(int i = 0; i< trys.size(); i++) {
	    	rankedwords.put(analyzeText(keyword, trys.get(i)), trys.get(i)); 
	    }
		   
	   ArrayList<String> words = new ArrayList<String> (rankedwords.values()); 
	   return words; 
   } 
   private static double analyzeText(String keyword, String tryword)  
   {  
	   double score = 0; 
	   double N = encyclopedia.size(); 
	   double n1 = 0 , n2 = 0, n3 = 0; 
	   for(int i = 0; i< N; i++ ) {
		   if(encyclopedia.get(i).indexOf(keyword) != -1) {
			   n1++; 
			   if(encyclopedia.get(i).indexOf(tryword)!= -1) {
				   n3++; 
			   }
		   }
		   else if (encyclopedia.get(i).indexOf(keyword) != -1){
			   n2++; 
		   }
	   }
	   double blim = n1*n2/(N*N); 
	   score = n3/N; 
	   if(n2 != n1 && score>blim) {
		   return score; 
	   }
	   else {
		   return 0; 
	   }
	   

   } 
   private static void test() {
	   // look through the list
	   double pscore = 0; //(input size/output size) 
	   double ascore = 0; //(matches/ input) 
	   ArrayList<String> result = new ArrayList<String>(); 
	   for(int i = 0; i< testkeys.size(); i++) { 
		   result = calculateList(testkeys.get(i)); 
		   String[] arrdata = testdata.get(i).split("\n"); 
		   pscore = result.size()/arrdata.length; 
		   for(int k = 0; k < result.size(); k++) { 
			   if(testdata.get(i).indexOf(result.get(k))!= -1) {
				   ascore += 1; 
			   }
		   }
		   ascore = ascore/arrdata.length; 
		   System.out.println(testkeys.get(i) +":\n" +  ascore + "\n" + pscore); 
		   
	   }
	   
   }
   
}


