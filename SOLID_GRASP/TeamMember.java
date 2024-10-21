import org.activity;

public class TeamMember implements IMember {
    private String name;
    private String email;
    public TeamMember(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void introduce() {
        System.out.println("Hi, my name is: " + name);
    }

    @Override
    public void contribute() {
        System.out.println("I am contributing towards the team :)");
    }
}