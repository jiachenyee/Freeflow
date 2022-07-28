package sg.edu.np.mad.freeflow;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class WorkspaceSettingsViewHolder extends RecyclerView.ViewHolder {

    FirebaseFirestore db;
    private FirebaseAuth mAuth;

    Button inviteButton;
    Button manageButton;
    EditText workspaceNameEditText;
    ImageView workspaceImage;
    CardView workspaceNameCard;

    TextView usernameTextView;
    TextView emailTextView;

    CardView adminCardView;
    ArrayList<String> admins;

    public WorkspaceSettingsViewHolder(View view, WorkspaceSettingsActivity activity, Bundle extras) {
        super(view);

        inviteButton = view.findViewById(R.id.invite_button);
        manageButton = view.findViewById(R.id.manage_button);
        workspaceNameEditText = view.findViewById(R.id.category_name_edit_text);
        workspaceImage = view.findViewById(R.id.workspace_image);
        workspaceNameCard = view.findViewById(R.id.category_name_card);

        usernameTextView = view.findViewById(R.id.assignee_text_view);
        emailTextView = view.findViewById(R.id.email_text_view);
        adminCardView = view.findViewById(R.id.admin_card_view);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();





        if (inviteButton != null) {
            inviteButton.setTextColor(activity.getResources().getColor(Workspace.colors[extras.getInt("workspaceAccentColor",0)]));
            inviteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent workspaceInviteActivity = new Intent(activity, WorkspaceInviteActivity.class);

                    workspaceInviteActivity.putExtra("workspaceIcon", (Bitmap) extras.getParcelable("workspaceIcon"));
                    workspaceInviteActivity.putExtra("workspaceAccentColor", extras.getInt("workspaceAccentColor"));
                    workspaceInviteActivity.putExtra("workspaceName", extras.getString("workspaceName"));
                    workspaceInviteActivity.putExtra("workspaceInviteCode", extras.getString("workspaceInviteCode"));

                    activity.startActivity(workspaceInviteActivity);
                }
            });
        }
        if (manageButton != null) {
            manageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent manageUserActivity = new Intent(activity, ManageUserActivity.class);

                    manageUserActivity.putExtra("workspaceIcon", (Bitmap) extras.getParcelable("workspaceIcon"));
                    manageUserActivity.putExtra("workspaceAccentColor", extras.getInt("workspaceAccentColor"));
                    manageUserActivity.putExtra("workspaceName", extras.getString("workspaceName"));
                    manageUserActivity.putExtra("workspaceInviteCode", extras.getString("workspaceInviteCode"));

                    activity.startActivity(manageUserActivity);
                }
            });


    }
}
}
