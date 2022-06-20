package sg.edu.np.mad.freeflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Category {
    public String name;
    public List<String> subtasks;

    public Category(String name) {
        this.name = name;
        subtasks = new ArrayList<>();
    }

    public Category(String name, List<String> subtasks) {
        this.name = name;
        this.subtasks = subtasks;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("subtasks", subtasks);

        return result;
    }
}
