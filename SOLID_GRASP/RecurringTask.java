import com.interfaces.activity;

public class RecurringTask extends Task {
    private int frequency;

    public RecurringTask(String title, String description, String dueDate, String status, String priority, int frequency) {
        super(title, description, dueDate, status, priority);
        this.frequency = frequency;
    }

    public RecurringTask(String title, String description, String dueDate, String status, String priority) {
        this(title, description, dueDate, status, priority, 0);
    }

    @Override
    public void doTask() {
        System.out.println("This is a recurring task that repeats " + frequency + " times");
    }

    public int getFrequency() {
        return frequency;
    }
    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
}