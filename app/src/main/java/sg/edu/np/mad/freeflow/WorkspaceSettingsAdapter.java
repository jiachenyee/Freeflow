package sg.edu.np.mad.freeflow;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class WorkspaceSettingsAdapter extends RecyclerView.Adapter<WorkspaceSettingsViewHolder> {

    Bundle extras;
    WorkspaceSettingsActivity activity;

    ArrayList<String> users;
    ArrayList<String> admins;

    ArrayList<User> decodedUsers = new ArrayList<>();

    public WorkspaceSettingsAdapter(Bundle extras, WorkspaceSettingsActivity activity) {
        this.extras = extras;
        this.activity = activity;

        users = extras.getStringArrayList("workspaceUsers");
        admins = extras.getStringArrayList("workspaceUsers");



        FirebaseFirestore db = FirebaseFirestore.getInstance();

        for (String userID: users) {
            DocumentReference userRef = db.collection("users").document(userID);

            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("Firestore", "DocumentSnapshot data: " + document.getData());
                            User user = new User(document.getData(), userID);

                            decodedUsers.add(user);
                            notifyDataSetChanged();
                        } else {
                            Log.d("Firestore", "No such document");
                        }
                    } else {
                        Log.d("Firestore", "get failed with ", task.getException());
                    }
                }
            });

        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @NonNull
    @Override
    public WorkspaceSettingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item;
        if (viewType == 0) {
            item = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.workspace_settings_configuration,
                    parent,
                    false);
        } else {
            item = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.workspace_user_card,
                    parent,
                    false);
        }
        return new WorkspaceSettingsViewHolder(item, activity, extras);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkspaceSettingsViewHolder holder, int position) {
        if (position == 0) {
            int color = Workspace.colors[extras.getInt("workspaceAccentColor",0)];

            holder.workspaceNameCard.setCardBackgroundColor(ContextCompat.getColor(activity, color));
            if (extras.getParcelable("workspaceIcon") == null) {
                holder.workspaceImage.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.workspace_icon));
            } else {
                holder.workspaceImage.setImageBitmap((Bitmap) extras.getParcelable("workspaceIcon"));
            }

            holder.workspaceNameEditText.setText(extras.getString("workspaceName"));
        } else {
            User currentUser = decodedUsers.get(position - 1);

            holder.adminCardView.setVisibility(admins.contains(currentUser.userID) ? View.VISIBLE : View.INVISIBLE);

            holder.usernameTextView.setText(currentUser.name);
            holder.emailTextView.setText(currentUser.emailAddress);
        }

    }

    @Override
    public int getItemCount() {
        return 1 + decodedUsers.size();
    }
}
