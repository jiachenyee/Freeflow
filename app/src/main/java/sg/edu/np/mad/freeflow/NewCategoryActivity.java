package sg.edu.np.mad.freeflow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class NewCategoryActivity extends AppCompatActivity {

    ImageButton closeButton;
    Button doneButton;
    EditText categoryNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_category);

        closeButton = findViewById(R.id.close_button);
        doneButton = findViewById(R.id.done_button);
        categoryNameEditText = findViewById(R.id.category_name_edit_text);

        Bundle extras = this.getIntent().getExtras();

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                String categoryName = categoryNameEditText.getText().toString();
                Category category = new Category(categoryName);

                String workspaceID = extras.getString("workspaceID");

                Map<String, Object> object = category.toMap();

                db.collection("workspaces").document(workspaceID)
                        .update("categories", FieldValue.arrayUnion(object))
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),
                                "Unable to create category, try again.",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(),
                                "Successfully created category",
                                Toast.LENGTH_LONG)
                                .show();
                        finish();
                    }
                });
            }
        });

        Button doneButton = findViewById(R.id.done_button);
        doneButton.setBackgroundResource(Workspace.colors[extras.getInt("workspaceAccentColor",0)]);

        LinearLayout headerView = findViewById(R.id.header_view);
        headerView.setBackgroundResource(Workspace.colors[extras.getInt("workspaceAccentColor",0)]);

        CardView categoryNameCard = findViewById(R.id.category_name_card);
        categoryNameCard.setBackgroundResource(Workspace.colors[extras.getInt("workspaceAccentColor",0)]);
    }
}