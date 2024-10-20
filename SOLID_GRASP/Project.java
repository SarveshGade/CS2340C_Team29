import java.util.List;
import java.util.ArrayList;
import java.util.NoSuchElementException;


public class Project {

    private String name;
    private String description;
    private String startDate;
    private String endDate;
    private List<TeamMember> teamMembers;
    private List<Task>tasks;

    public Project(String name, String description, String startDate, String endDate, List<TeamMember> teamMembers, List<Task> taskCollection) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.teamMembers = teamMembers;
        this.tasks = taskCollection;
    }

    public Project(String name, String description, String startDate, String endDate) {
        this(name, description, startDate, endDate, new ArrayList<TeamMember>(), new ArrayList<Task>());
    }

    public void addTask(Task toAdd) {
        tasks.add(toAdd);
    }

    public Task removeTask(Task toDelete) {
        if (tasks.remove(toDelete)) {
            return toDelete;
        } else {
            throw new NoSuchElementException("Task not found");
        }
    }

    public void addTeamMember(TeamMember toAdd) {
        teamMembers.add(toAdd);
    }

    public TeamMember removeTeamMember(TeamMember toDelete) {
        if (teamMembers.remove(toDelete)) {
            return toDelete;
        } else {
            throw new NoSuchElementException("Team member not found");
        }
    }

    public List<TeamMember> getTeamMembers() {
        return teamMembers;
    }
}