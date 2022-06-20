package sg.edu.np.mad.freeflow;

import java.util.HashMap;
import java.util.Map;

public class Task {
    String title;
    String description;

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("description", description);

        return result;
    }
}
