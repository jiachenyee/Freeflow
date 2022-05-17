package sg.edu.np.mad.freeflow;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeEmptyStateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeEmptyStateFragment extends Fragment {

    Button newWorkspaceButton;

    private MainActivity activity;

    public HomeEmptyStateFragment(MainActivity activity) {
        // Required empty public constructor
        this.activity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home_empty_state, container, false);

        newWorkspaceButton = v.findViewById(R.id.new_workspace_button);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        newWorkspaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newWorkspaceActivity = new Intent(activity, NewWorkspaceActivity.class);
                startActivity(newWorkspaceActivity);
            }
        });
    }
}