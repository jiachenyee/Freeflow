package sg.edu.np.mad.freeflow;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private List<String> workspaceIDs;
    private MainActivity activity;

    private RecyclerView workspaceRecyclerView;

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
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        WorkspaceCardAdapter mAdapter = new WorkspaceCardAdapter(new ArrayList<>(), activity);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(
                activity,
                LinearLayoutManager.HORIZONTAL,
                false);

        workspaceRecyclerView.setLayoutManager(mLayoutManager);
        workspaceRecyclerView.setItemAnimator(new DefaultItemAnimator());
        workspaceRecyclerView.setAdapter(mAdapter);
    }

    public void setWorkspaceIDs(ArrayList<String> workspaceIDs) {
        this.workspaceIDs = workspaceIDs;
    }
}