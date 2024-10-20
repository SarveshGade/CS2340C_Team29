public class TeamMember implements IMember {
    private String name;
    private String email;
    public TeamMember(String name, String email) {
        this.name = name;
        this.email = email;
    }

    @Override
    public void introduce() {
        System.out.println("Hi, my name is: " + name);
    }
}