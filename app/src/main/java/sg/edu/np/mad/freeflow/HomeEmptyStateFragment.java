package sg.edu.np.mad.freeflow;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeEmptyStateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeEmptyStateFragment extends Fragment {

    private MainActivity activity;
    private ImageButton addWorkspaceButton;

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

        addWorkspaceButton = v.findViewById(R.id.add_workspace_button);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        addWorkspaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(activity, addWorkspaceButton);
                popupMenu.getMenuInflater().inflate(R.menu.workspace_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        if (menuItem.getItemId() == R.id.join_workspace_menu) {
                            // TODO: Link with join workspace interface
                        } else {
                            Intent newWorkspaceActivity = new Intent(activity, NewWorkspaceActivity.class);
                            startActivity(newWorkspaceActivity);
                        }

                        return true;
                    }
                });

                popupMenu.show();
            }
        });
    }
}