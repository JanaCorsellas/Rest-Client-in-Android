package dsa.upc.edu.listapp.github;

public class Repo {
    public final String name;
    public final String description;

    public final String avatar_url;

    public Repo(String name, String description, String avatar_url) {
        this.name = name;
        this.description = description;
        this.avatar_url = avatar_url;
    }

    @Override
    public String toString() {
        return name+" ("+description+")" + avatar_url;
    }

}
