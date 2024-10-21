import java.util.List;
import com.interfaces.activity;

public class TeamLeader extends TeamMember implements ILeader {
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
    public void contribute() {
        System.out.println("I'm the leader!");
    }

    @Override
    public void oversee() {
        System.out.println("Overseeing the team...");
        System.out.println("Our project is: " + project.getName());
        System.out.println("Our team members are:");
        List<TeamMember> members = project.getTeamMembers();
        for (TeamMember member: members) {
            System.out.println(member.getName());
        }
    }
}