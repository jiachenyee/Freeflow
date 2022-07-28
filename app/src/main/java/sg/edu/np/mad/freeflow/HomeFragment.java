package sg.edu.np.mad.freeflow;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;
//import android.widget.SearchView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private List<Workspace> workspaces;
    private List<TaskWorkspaceWrapper> taskWorkspaceWrapperList;

    private MainActivity activity;

    private WorkspaceCardAdapter mAdapter;

    private TaskAdapter taskAdapter;

    private RecyclerView workspaceRecyclerView;
    private RecyclerView tasksRecyclerView;

    private ImageButton addWorkspaceButton;
    private ImageButton sortTasksButton;

    private SearchView taskSearchView;


    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(MainActivity activity) {
        HomeFragment fragment = new HomeFragment();

        fragment.activity = activity;

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        workspaceRecyclerView = v.findViewById(R.id.workspace_recycler_view);
        addWorkspaceButton = v.findViewById(R.id.new_assignee_button);
        tasksRecyclerView = v.findViewById(R.id.tasks_recycler_view);
        taskSearchView = v.findViewById(R.id.task_searchView);
        sortTasksButton = v.findViewById(R.id.sort_tasks_button);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        mAdapter = new WorkspaceCardAdapter(workspaces, activity);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(
                activity,
                LinearLayoutManager.HORIZONTAL,
                false);

        workspaceRecyclerView.setLayoutManager(mLayoutManager);
        workspaceRecyclerView.setItemAnimator(new DefaultItemAnimator());
        workspaceRecyclerView.setAdapter(mAdapter);

        taskSearchView.clearFocus();
        taskSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }

            private void filterList(String newText) {
                List<TaskWorkspaceWrapper> filteredList = new ArrayList<>();
                for (TaskWorkspaceWrapper taskWorkspaceWrapper : taskWorkspaceWrapperList){
                    if (taskWorkspaceWrapper.task.title.toLowerCase().contains(newText.toLowerCase())){
                        filteredList.add(taskWorkspaceWrapper);
                    }
                }
                if(filteredList.isEmpty()){
                    Toast.makeText(activity, "No task found", Toast.LENGTH_SHORT).show();
                }
                else{
                    taskAdapter.setFilteredList(filteredList);
                }
            }
        });

        addWorkspaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(activity, addWorkspaceButton);
                popupMenu.getMenuInflater().inflate(R.menu.workspace_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        if (menuItem.getItemId() == R.id.join_workspace_menu) {
                            Intent joinWorkspaceActivity = new Intent(activity, JoinWorkspaceActivity.class);
                            startActivityForResult(joinWorkspaceActivity, 1);
                        } else {
                            Intent newWorkspaceActivity = new Intent(activity, NewWorkspaceActivity.class);
                            startActivityForResult(newWorkspaceActivity, 1);
                        }

                        return true;
                    }
                });

                popupMenu.show();
            }
        });

        sortTasksButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                taskWorkspaceWrapperList = taskWorkspaceWrapperList.stream()
                        .sorted(new Comparator<TaskWorkspaceWrapper>() {
                            @Override
                            public int compare(TaskWorkspaceWrapper o1, TaskWorkspaceWrapper o2) {
                                return o1.task.title.compareTo(o2.task.title);
                            }
                        }).collect(Collectors.toCollection(ArrayList::new));
                taskAdapter.setTaskWorkspaceWrapperList(taskWorkspaceWrapperList);
                reloadData();
            }
        });

        setUpRecyclerView();
    }

    public void reloadData() {
        mAdapter.notifyDataSetChanged();
        taskAdapter.notifyDataSetChanged();
    }

    public HomeFragment setWorkspaces(ArrayList<Workspace> workspaces, ArrayList<TaskWorkspaceWrapper> taskWorkspaceWrapperList) {
        this.workspaces = workspaces;
        this.taskWorkspaceWrapperList = taskWorkspaceWrapperList;

        return this;
    }


    void setUpRecyclerView() {
        taskAdapter = new TaskAdapter(taskWorkspaceWrapperList, activity);

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                activity,
                LinearLayoutManager.VERTICAL,
                false);

        tasksRecyclerView.setLayoutManager(layoutManager);
        tasksRecyclerView.setItemAnimator(new DefaultItemAnimator());
        tasksRecyclerView.setAdapter(taskAdapter);
    }
}