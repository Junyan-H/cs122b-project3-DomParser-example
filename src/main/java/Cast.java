import java.util.ArrayList;

public class Cast {

    private final String id;

    private final String title;

    private final ArrayList<String> actor;

    public Cast(String id, String title, ArrayList<String> actor){
        // params
        this.id = id;
        this.title = title;
        this.actor = actor;
    }
    public String getID(){return id;}
    public String getTitle(){return title;}

    public ArrayList<String> getActor(){return actor;}


    public String toString(){
        String actors="";
        for(int i = 0; i < getActor().size(); i++ ){
            actors += getActor().get(i) +", ";
        }

        return  "ID: "+ getID() +", " +
                "Title: " + getTitle() + ", " +
                "actors: " + actors +".";
    }


}
