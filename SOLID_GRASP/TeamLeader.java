public class TeamLeader extends TeamMember implements Lead {
    private Project project;

    public TeamLeader(String name, String email) {
        super(name, email);
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public void lead() {
        System.out.println("I'm the leader!");
    }

    @Override
    public void oversee() {
        System.out.println("Overseeing the team...");
    }


}