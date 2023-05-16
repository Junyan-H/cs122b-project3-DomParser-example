import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DomParserActor {

    //url for xml file
    String xmlFile;

    //list of actors
    List<Actor> actors = new ArrayList<>();



    HashMap <String, Actor>  actorMap = new HashMap<String, Actor>();

    //number of dups
    int dups = 0;

    //Dom doc
    Document dom;

    public HashMap<String, Actor> getActorMap(){return this.actorMap;}
    public void runParser(){


        //parse xmpl file and get dom object
        parseXmlFile();

        //get each actor element and create a actor object
        parseDocument();

        //iterate through the list and print data
//        printData();
    }

    //
    private void parseXmlFile(){
        //document factory
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

        try{
            //document builder
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            //parse using builder to get Dom representation of the xml file
            dom = documentBuilder.parse("actors63.xml");

        }catch (ParserConfigurationException | SAXException | IOException error) {
            error.printStackTrace();
        }

    }


    private void parseDocument(){
        // get the document root Element
        Element documentElement = dom.getDocumentElement();

        //get a nodelist of actors elements parse each into actor objects
        NodeList nodelist = documentElement.getElementsByTagName("actor");
        for(int i = 0; i< nodelist.getLength(); i++){

            //grab actor element
            Element element = (Element) nodelist.item(i);

            //grab actor object
            Actor actor = parserActor(element);

            if (this.actorMap.containsKey(actor.getActorName())){
                this.dups +=1;
                //can redirect dups to a file if wanted to below

            }else{
                this.actorMap.put(actor.getActorName(),actor);
                this.actors.add(actor);
            }

            //add it to list

        }
    }

    private Actor parserActor(Element element){
        //param needed for actors
        String name = getTextValue(element,"stagename");
        String birthYear;
        try{
            birthYear = getTextValue(element, "dob");
        }catch (NullPointerException e){
             birthYear = null;
        }

//
//        System.out.println("INSIDE CREATING ACTOR");
//        System.out.println(name);
//        System.out.println(birthYear);

        //filtering for birthyear
        try {
            birthYear = birthYear.substring(0,4);
            Integer.parseInt(birthYear);
        }catch(NumberFormatException e) {
            birthYear = null;
        }catch(StringIndexOutOfBoundsException e){
            birthYear = null;
        }catch (NullPointerException e){
            birthYear = null;
        }

        //id is generated when we add to list
        return new Actor(name,birthYear);

    }

    /**
     * It takes an XML element and the tag name, look for the tag and get
     * the text content
     * i.e for <Employee><Name>John</Name></Employee> xml snippet if
     * the Element points to employee node and tagName is name it will return John
     */
    private String getTextValue(Element element, String tagName) {
        String textVal = null;
        NodeList nodeList = element.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            // here we expect only one <Name> would present in the <Employee>
            textVal = nodeList.item(0).getFirstChild().getNodeValue();
        }
        return textVal;
    }

    /**
     * Iterate through the list and print the
     * content to console
     */
    private void printData() {
//
        System.out.println("Total parsed " + this.actorMap.size() + " actors");

        for (Map.Entry<String,Actor> actor : actorMap.entrySet()) {
            String key = actor.getKey();
            Actor value = actor.getValue();
            System.out.println("KEY = " + key + " Value = " + value.toString());

        }
        System.out.println("DUPS : "+ this.dups);
    }

    private void addData(){
//        prebuild sql statement add it all together
    }



    public static void main(String[] args) {
        // create an instance
        DomParserActor domParserActor = new DomParserActor();

        // call run example
        domParserActor.runParser();
    }

}
