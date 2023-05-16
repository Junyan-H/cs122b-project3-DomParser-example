import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.*;


public class ParserRunner {
    // create parsers
    // create an instance




    public HashMap <String, Actor>  actorMap = new HashMap<String, Actor>();
    public HashMap <String, Cast>  castMap = new HashMap<String, Cast>();
    public HashMap <String, Movie>  movieMap = new HashMap<String, Movie>();

    public Set<String> star_set = new HashSet<>();






//    arraylist of params for adding movies
    public ArrayList <HashMap<String,String>>  sqlMovieParams = new ArrayList<>();
//    arraylist of params for adding stars
    public ArrayList <HashMap<String,String>>  sqlStarParams = new ArrayList<>();

//    set of genres to add to sql genre
    public Set<String> genre_set = new HashSet<>();

    // STARSGENREMOVIEMAP links remaining cast and genres that needs to be added to stars in movies and genres in movies
//    KEY MOVIE OBJECT
//            KEYS (CastArray or GenreArray)
//                  Arraylist of genres or Casts that needs to be linked to the movie
    public HashMap<Movie, HashMap<String,ArrayList<String>>> starsGenreMovieMap= new HashMap<>();

    public ParserRunner(){
        //initializing the parsers and running them

        DomParserActor domParserActor = new DomParserActor();
        DomParserCast domParserCast = new DomParserCast();
        DomParserMovies domParserMovies = new DomParserMovies();

        domParserActor.runParser();
        domParserCast.runParser();
        domParserMovies.runParser();

        //grabbing each of their hashmaps
        this.actorMap = domParserActor.getActorMap();
        this.castMap = domParserCast.getCastMap();
        this.movieMap = domParserMovies.getMovieMap();
    }

    public void runParsers(){
        //arraylist of add movie param
        sqlMoviesParamInit();

        //arraylist of add star param
//        sqlStarParamsInit();
//
//        for (Map.Entry<Movie, HashMap<String,ArrayList<String>>> entry : starsGenreMovieMap.entrySet()) {
//            Movie movie = entry.getKey();
//            HashMap<String,ArrayList<String>> mapStarsGenreArray= entry.getValue();
//
//            String title = movie.getTitle();
//            String year = movie.getYear();
//            String director = movie.getDirector();
//
//            ArrayList<String> castArray = mapStarsGenreArray.get("CastArray");
//            ArrayList<String> genreArray = mapStarsGenreArray.get("GenreArray");
//
//            while (castArray.size() > 0 || genreArray.size()> 0 ){
//                String starName = null;
//                String genreName = null;
//                if(castArray.size()>0){
//                    starName = castArray.remove(0);
//                }
//                if(genreArray.size()>0){
//                    genreName = genreArray.remove(0);
//                }
//                //CAll the procedure here
//                System.out.println("QUERY FOR ADDING IN SIM AND GIM *********************************");
//                System.out.print("TITLE : ");
//                System.out.println(title);
//                System.out.print("Year : ");
//                System.out.println(year);
//                System.out.print("director : ");
//                System.out.println(director);
//                System.out.print("starname : ");
//                System.out.println(starName);
//                System.out.print("genrename : ");
//                System.out.println(genreName);
//
//            }
//        }

        //loop through genre set each elem is hte param


        // add stars to movies based on remaining cast hashmap movie <-> star actor "all actor should be added in db" same with movies

//    movie1 star1
//    movie1 star2

//    movie1 (star1, star2, star3)

//    movie1 (star2, star3 )
//


    }

    private void sqlStarParamsInit(){
//        Iterator<String> star_itr = this.star_set.iterator();


        for(String itr : this.star_set){
            HashMap <String,String> params = new HashMap<>();
            Actor actor = actorMap.get(itr);
            params.put("Actor", actor.getActorName());
            params.put("Dob",actor.getActorDob());
            System.out.println("ACTOR PARAM ---------------------");
            System.out.println("Actor: " + params.get("Actor"));
            System.out.println("Dob: " +params.get("Dob"));
            sqlStarParams.add(params);
        }


        System.out.println("TEST ********");
        System.out.println(sqlStarParams);
//        while(star_itr.hasNext()){
//
//            Actor actor = actorMap.get(star_itr.);
//            params.put("Actor", actor.getActorName());
//            params.put("Dob",actor.getActorDob());
//            System.out.println("ACTOR PARAM ---------------------");
//            System.out.println("Actor: " + params.get("Actor"));
//            System.out.println("Dob: " +params.get("Dob"));
//            sqlStarParams.add(params);
//            star_itr.next();
//        }

    }



    private void sqlMoviesParamInit(){
        // loop through cast movies id _>
        for (Map.Entry<String, Cast> itr: castMap.entrySet()) {
            String id = itr.getKey();
            Cast cast = itr.getValue();
            HashMap <String,String> params = new HashMap<>();

            // find if movie exists in movie hashmap -> t
            if(movieMap.containsKey(id) && (cast.getTitle() !=null || !cast.getTitle().isEmpty())){
                Movie m = movieMap.get(id);

                // remove all stars not present in actor list
                ArrayList<String> movieCast = new ArrayList<>();
                for(String castMember : cast.getActor()){
                    if(actorMap.containsKey(castMember)){
                        movieCast.add(castMember);
                    }
                }

                // checks for genres > 0

                if( movieCast.size()>0 && m.getGenre().size() >0){
                    // add movie procedure with first star ->
                    String actorName = movieCast.get(0);

                    if (actorName == null || actorName.isEmpty() || actorName.equals("null") ){
                        System.out.println("ErrrOR*********************************************************************************************");
                        System.out.println(id);
                        System.out.println(cast);
                        System.exit(1);
                    }

                    //GENRE
                    ArrayList<String> genreList = m.getGenre();
                    String genre;

                    //grabbing genres
                    genre = genreList.get(0);

                    params.put("Title", m.getTitle());
                    params.put("Year", m.getYear());
                    params.put("Director", m.getDirector());
                    params.put("Genre",genre);
                    params.put("Actor", actorName);

                    // grab dob if not null
                    params.put("Dob", actorMap.get(actorName).getActorDob());

                    System.out.println("TESTING PARAM HASHMAP*********************************");

                    System.out.println("title :" + params.get("Title"));
                    System.out.println("Year :" + params.get("Year"));
                    System.out.println("Director :" + params.get("Director"));
                    System.out.println("Actor :" +params.get("Actor"));
                    System.out.println("Dob :"+ params.get("Dob"));
                    System.out.println("Genre :"+ params.get("Genre"));

                    //remove first star
                    movieCast.remove(0);
                    //remove first genre
                    genreList.remove(0);

                    System.out.println("size param");
                    System.out.println(movieCast.size());
                    System.out.println(genreList.size());

                    //link movie to remaining cast member and genres
                    HashMap<String,ArrayList<String>> sgmParam = new HashMap<>();

                    sgmParam.put("CastArray",movieCast);
                    sgmParam.put("GenreArray",genreList);
                    starsGenreMovieMap.put(m,sgmParam);

                    // add remaining stars to ->set
                    star_set.addAll(movieCast);
                    genre_set.addAll(genreList);

                    sqlMovieParams.add(params);
                }
            }


        }



    }







    public static void main(String[] args) {
        ParserRunner parserRunner = new ParserRunner();

        parserRunner.runParsers();
    }


}
