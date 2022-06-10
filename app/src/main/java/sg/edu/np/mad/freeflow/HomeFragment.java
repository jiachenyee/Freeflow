package sg.edu.np.mad.freeflow;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private List<Workspace> workspaces;
    private MainActivity activity;
    private WorkspaceCardAdapter mAdapter;

    private RecyclerView workspaceRecyclerView;

    private ImageButton addWorkspaceButton;

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
        addWorkspaceButton = v.findViewById(R.id.new_workspace_button);

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
                            startActivity(joinWorkspaceActivity);
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
    }

    public void reloadData() {
        mAdapter.notifyDataSetChanged();
    }

    public HomeFragment setWorkspaces(ArrayList<Workspace> workspaces) {
        this.workspaces = workspaces;

        return this;
    }
}