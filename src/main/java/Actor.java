public class Actor {

    private final String name ;
    private final String dob;

//    private final String type;

    public Actor(String name, String dob){
        System.out.println("creating actor object");

        // required
        this.name = name;
        System.out.println(name);
        // not required
        this.dob = dob;
        System.out.println(dob);
//        this.type = type;
    }



    public String getActorName() {
        return name;
    }

    public String getActorDob(){return dob;}

//    public String getType() {
//        return type;
//    }

    public String toString() {

        return "Name:" + getActorName() + ", " +
                "Dob:" + getActorDob() + ".";
    }
}
