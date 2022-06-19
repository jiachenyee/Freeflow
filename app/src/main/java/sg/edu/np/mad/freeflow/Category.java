package sg.edu.np.mad.freeflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Category {
    public String name;
    public List<String> subtasks;

    public Map<String, Object> tasks;

    public Category(String name) {
        this.name = name;
        subtasks = new ArrayList<>();
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);

        String encodedTasks = "";
        for (int i = 0; i < subtasks.size(); i++) {
            if (i > 0) {
                encodedTasks += ", ";
            }

            encodedTasks += subtasks.get(i);
        }

        result.put("subtasks", encodedTasks);

        return result;
    }
}
