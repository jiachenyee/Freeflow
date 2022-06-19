package sg.edu.np.mad.freeflow;

import java.util.ArrayList;
import java.util.Map;

public class Category {
    public String name;
    public String[] subtasks;

    public Map<String, Object> tasks;

    public Category(Map<String, Object> object) {
        name = (String) object.get("catName");
        String subtaskString = (String) object.get("subtasks");

        subtasks = subtaskString.split("\t");
    }
}
