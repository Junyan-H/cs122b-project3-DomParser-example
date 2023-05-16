import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class DomParserCast {

    String xmlFile;

    HashMap<String ,Cast> castMap = new HashMap<String, Cast>();

    Document dom;

    int dups = 0;


    public static class InvalidInput extends Exception{
        public InvalidInput(String errorMessage){
            super(errorMessage);
        }
    }

    //return HashMap obj
    public HashMap<String,Cast> getCastMap(){return castMap;}
    public void runParser(){

        //parse xmpl file and get dom object
        parseXmlFile();

        //get each movie element and create a movie object
        parseDocument();

        //iterate through the list and print data
//        printData();
    }

    private void parseXmlFile(){
        //document factory
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

        try{
            //document builder
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            //parse using builder to get Dom representation of the xml file
            dom = documentBuilder.parse("casts124.xml");

        }catch (ParserConfigurationException | SAXException | IOException error) {
            error.printStackTrace();
        }

    }

    private  void parseDocument(){
        // get the document root Element
        Element documentElement = dom.getDocumentElement();

        //get a nodelist of movies elements parse each into film objects
        NodeList nodelist = documentElement.getElementsByTagName("filmc");
        for(int i = 0; i< nodelist.getLength(); i++){
            //grab movie element
            Element element = (Element) nodelist.item(i);
            //grab movie object

            Cast cast = parserCast(element);
            if(this.castMap.containsKey(cast.getID())){
                this.dups +=1;
                //can redirect dups to a file if wanted to below
            }else {
                this.castMap.put(cast.getID(),cast);
            }
            //add it to movie list

        }
    }


    private Cast parserCast(Element element){
        String id = getTextValue(element,"f");

        String title = getTextValue(element,"t");

        Set<String> actors = new HashSet<>( getListValue(element,"a"));



        return new Cast(id,title,actors);
    }

    private String getTextValue(Element element, String tagName) {
        String textVal = null;
        NodeList nodeList = element.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            try{
                // here we expect only one <Name> would present in the <Employee>
                textVal = nodeList.item(0).getFirstChild().getNodeValue();
            }catch (NullPointerException e){

            }

        }
        return textVal;
    }


    private ArrayList<String> getListValue(Element element, String tagName){
        ArrayList<String> val = new ArrayList<>();
        String textVal = null;

        NodeList nodeList = element.getElementsByTagName(tagName);

        for(int i = 0; i < nodeList.getLength(); i++){
            try{
                textVal = nodeList.item(i).getFirstChild().getNodeValue();
                if(textVal != null && !toString().isEmpty()){
//                    System.out.println("Parsing through list vals for "+ tagName+" :"+textVal);
                    val.add(textVal);
                }

            }catch (NullPointerException e){

            }


        }
        return val;
    }
    private void printData() {

        System.out.println("Total parsed " + this.castMap.size() + " movies");

        for (Map.Entry<String,Cast> cast : castMap.entrySet()) {
            String key = cast.getKey();
            Cast value = cast.getValue();
            System.out.println("KEY = " + key + " Value = " + value.toString());

        }
        System.out.println("DUPS : "+ this.dups);
    }


    public static void main(String[] args) {
        // create an instance
         DomParserCast domParserCast= new DomParserCast();
         domParserCast.runParser();
    }
}
