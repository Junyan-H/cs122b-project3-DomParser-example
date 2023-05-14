import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class DomParserMovies {
    //url for xml file
    String xmlFile;

    //list of movies

    List<Movie> movies = new ArrayList<>();

    //Dom doc
    Document dom;
    public static class InvalidInput extends Exception{
        public InvalidInput(String errorMessage){
            super(errorMessage);
        }
    }

    public void runParser(){


        //parse xmpl file and get dom object
        parseXmlFile();

        //get each movie element and create a movie object
        parseDocument();

        //iterate through the list and print data
        printData();
    }

    private void parseXmlFile(){
        //document factory
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

        try{
            //document builder
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            //parse using builder to get Dom representation of the xml file
            dom = documentBuilder.parse("mains243.xml");

        }catch (ParserConfigurationException | SAXException | IOException error) {
            error.printStackTrace();
        }

    }

    private  void parseDocument(){
        // get the document root Element
        Element documentElement = dom.getDocumentElement();

        //get a nodelist of movies elements parse each into film objects
        NodeList nodelist = documentElement.getElementsByTagName("film");
        for(int i = 0; i< nodelist.getLength(); i++){

            //grab movie element
            Element element = (Element) nodelist.item(i);

            //grab movie object
            try{
                Movie movie = parserMovie(element);
                if(movie.getGenre() == null|| movie.getGenre().isEmpty() ){
                    throw new InvalidInput("SKIPPING: "+movie.getTitle() +" because invalid genres");
                }
                if( movie.getDirector() == null || movie.getDirector().isEmpty() ){
                    throw new InvalidInput("SKIPPING: "+movie.getTitle() +" because invalid Director");
                }
                this.movies.add(movie);
            }catch(InvalidInput e){
                System.out.println(e);
            }



        }
    }

    private Movie parserMovie(Element element){
        System.out.println("PARSING MOVE ***********************************************");
        //grabbing id
        String id = getTextValue(element,"fid");
        //grabbing title
        String title = getTextValue(element, "t");
        //grabbing year
        String year = getTextValue(element,"year");

        //if null skip
        ArrayList<String> directors = getListValue(element,"dirn");
        String director;

        if(!directors.isEmpty()){
            director = directors.get(0);
        }else{
            director = null;
        }


        //if null skip
        ArrayList<String> genres = getListValue(element,"cat");


        System.out.println("INSIDE CREATING Movie");
        System.out.print("TITLE :");

        System.out.println(title);
        System.out.print("Year :");

        System.out.println(year);
        System.out.print("Director :");

        System.out.println(director);
        System.out.print("genre : ");
        for(String genre : genres){
            System.out.print(genre+ " ");
        }
        System.out.println(" ");
        return new Movie(id, title,year,director,genres);
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
                    System.out.println("Parsing through list vals for "+ tagName+" :"+textVal);
                    val.add(textVal);
                }

            }catch (NullPointerException e){

            }


        }
        return val;
    }

    private void printData() {

        System.out.println("Total parsed " + this.movies.size() + " movies");

        for (Movie movie : movies) {
            System.out.println("\t" + movie.toString());
        }
    }

    public static void main(String[] args) {
        // create an instance
        DomParserMovies domParserMovies = new DomParserMovies();

        // call run example
        domParserMovies.runParser();
    }



}
