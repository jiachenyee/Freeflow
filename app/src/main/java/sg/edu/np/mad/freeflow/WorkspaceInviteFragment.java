package sg.edu.np.mad.freeflow;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class WorkspaceInviteFragment extends Fragment {

    Button joinButton;
    EditText workspaceInviteCodeEditText;

    OnSubmitHandler onSubmitHandler;

    public WorkspaceInviteFragment() {
        // Required empty public constructor
    }

    public static WorkspaceInviteFragment newInstance(OnSubmitHandler onSubmitHandler) {
        WorkspaceInviteFragment fragment = new WorkspaceInviteFragment();

        fragment.onSubmitHandler = onSubmitHandler;

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
        View v = inflater.inflate(R.layout.fragment_workspace_invite, container, false);

        joinButton = v.findViewById(R.id.join_workspace_button);
        workspaceInviteCodeEditText = v.findViewById(R.id.workspace_invite_code_edit_text);

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onSubmitHandler != null) {
                    onSubmitHandler.onSubmit(workspaceInviteCodeEditText.getText().toString());
                }
            }
        });

        return v;
    }

    public interface OnSubmitHandler {
        void onSubmit(String code);
    }
}