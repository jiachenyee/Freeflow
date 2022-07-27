package sg.edu.np.mad.freeflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Task {
    String title;
    String dueDate;
    String description;
    String taskID;
    ArrayList<Message> messages;

    public Task(String title, String description, String taskID, ArrayList<Message> messages) {
        this.title = title;
        this.description = description;
        this.taskID = taskID;
        this.messages = messages;
    }

    public Task(String title, String dueDate, String description, String taskID) {
        this.title = title;
        this.dueDate = dueDate;
        this.description = description;
        this.taskID = taskID;
    }

    public Task(String title, String dueDate, String description) {
        this.title = title;
        this.dueDate = dueDate;
        this.description = description;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("dueDate", dueDate);
        result.put("description", description);

        return result;
    }
}
