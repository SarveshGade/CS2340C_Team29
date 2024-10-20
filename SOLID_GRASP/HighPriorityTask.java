public class HighPriorityTask extends Task {

    public HighPriorityTask(String name, String description, String dueDate, String status, String priority) {
        super(name, description, dueDate, status, priority);
    }

    @Override
    public void doTask() {
        System.out.println("This is a high priority task that must be run immediately");
    }
}
