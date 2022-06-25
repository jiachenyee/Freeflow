package sg.edu.np.mad.freeflow;

public class TaskWorkspaceWrapper {
    Task task;
    String workspaceID;
    int color;

    public TaskWorkspaceWrapper(Task task, String workspaceID, int color) {
        this.task = task;
        this.workspaceID = workspaceID;
        this.color = color;
    }
}
