import java.util.List;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import org.activity;

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
        this(name, description, startDate, endDate, new ArrayList<>(), new ArrayList<Task>());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public List<TeamMember> getTeamMembers() {
        return teamMembers;
    }

    public Task createTask(String title, String description, String dueDate, String status, String priority) {
        Task newTask = new Task(title, description, dueDate, status, priority);
        this.tasks.add(newTask); //Automatically add task to current project
        return newTask;
    }

    public TeamMember createTeamMember(String name, String email) {
        TeamMember newMember = new TeamMember(name, email);
        this.teamMembers.add(newMember);  //Automatically add team member to current porject
        return newMember;
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
}