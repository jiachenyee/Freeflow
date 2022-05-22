package sg.edu.np.mad.freeflow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

public class NewWorkspaceActivity extends AppCompatActivity {

    private MaterialCardView workspaceAccent1;
    private MaterialCardView workspaceAccent2;
    private MaterialCardView workspaceAccent3;
    private MaterialCardView workspaceAccent4;
    private MaterialCardView workspaceAccent5;
    private MaterialCardView workspaceAccent6;

    private ImageView workplaceImage;

    private LinearLayout headerView;

    private int selectedColorIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_workspace);

        ImageButton closeButton = findViewById(R.id.close_button);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        workplaceImage = findViewById(R.id.workspace_image);

        workplaceImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imagePickerIntent = new Intent();
                imagePickerIntent.setType("image/*");
                imagePickerIntent.setAction(Intent.ACTION_GET_CONTENT);

                // pass the constant to compare it
                // with the returned requestCode
                startActivityForResult(Intent.createChooser(imagePickerIntent, "Select a workspace icon"), 200);
            }
        });

        headerView = findViewById(R.id.header_view);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        setUpAccentColorSelector();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri selectedImageUri = data.getData();

        System.out.println(selectedImageUri);
        Bitmap selectedImageBitmap;
        try {
            selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
            int newImageWidth = Math.min(selectedImageBitmap.getWidth(), selectedImageBitmap.getHeight());

            Bitmap croppedBitmap = Bitmap.createBitmap(selectedImageBitmap,
                    (selectedImageBitmap.getWidth() - newImageWidth) / 2,
                    (selectedImageBitmap.getHeight() - newImageWidth) / 2, newImageWidth, newImageWidth);

            workplaceImage.setImageBitmap(croppedBitmap);

            createDynamicLink();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        EditText workspaceNameEditText = findViewById(R.id.workspace_name_edit_text);

        workspaceNameEditText.postDelayed(new Runnable() {
            @Override
            public void run() {
                workspaceNameEditText.setFocusableInTouchMode(true);
                workspaceNameEditText.requestFocus();
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(workspaceNameEditText, 0);
            }
        }, 0);
    }

    private void setUpAccentColorSelector() {
        workspaceAccent1 = findViewById(R.id.workspace_accent_1);
        workspaceAccent1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedColorIndex = 1;
                updateAccentColorSelector();
            }
        });

        workspaceAccent2 = findViewById(R.id.workspace_accent_2);
        workspaceAccent2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedColorIndex = 2;
                updateAccentColorSelector();
            }
        });

        workspaceAccent3 = findViewById(R.id.workspace_accent_3);
        workspaceAccent3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedColorIndex = 3;
                updateAccentColorSelector();
            }
        });

        workspaceAccent4 = findViewById(R.id.workspace_accent_4);
        workspaceAccent4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedColorIndex = 4;
                updateAccentColorSelector();
            }
        });

        workspaceAccent5 = findViewById(R.id.workspace_accent_5);
        workspaceAccent5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedColorIndex = 5;
                updateAccentColorSelector();
            }
        });

        workspaceAccent6 = findViewById(R.id.workspace_accent_6);
        workspaceAccent6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedColorIndex = 6;
                updateAccentColorSelector();
            }
        });

        updateAccentColorSelector();
    }

    private int[] color = {
            R.color.workspace_red,
            R.color.workspace_orange,
            R.color.workspace_yellow,
            R.color.workspace_green,
            R.color.workspace_blue,
            R.color.workspace_purple
    };

    private void updateAccentColorSelector() {
        workspaceAccent1.setStrokeWidth(selectedColorIndex == 1 ? 21 : 0);
        workspaceAccent2.setStrokeWidth(selectedColorIndex == 2 ? 21 : 0);
        workspaceAccent3.setStrokeWidth(selectedColorIndex == 3 ? 21 : 0);
        workspaceAccent4.setStrokeWidth(selectedColorIndex == 4 ? 21 : 0);
        workspaceAccent5.setStrokeWidth(selectedColorIndex == 5 ? 21 : 0);
        workspaceAccent6.setStrokeWidth(selectedColorIndex == 6 ? 21 : 0);

        headerView.setBackgroundResource(color[selectedColorIndex - 1]);
    }

    private void uploadBitmap(Bitmap bitmap) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        StorageReference imageRef = storageRef.child("workspaceicons/" + UUID.randomUUID().toString() + ".jpg");

        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                displayErrorToast();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return imageRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();

                            Log.i("FB Storage", downloadUri.toString());
                        } else {
                            displayErrorToast();
                        }
                    }
                });
            }
        });
    }

    private void createDynamicLink() {
        Task<ShortDynamicLink> dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://www.example.com/"))
                .setDomainUriPrefix("https://npff.page.link")
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                .buildShortDynamicLink(ShortDynamicLink.Suffix.SHORT);

        dynamicLink.addOnSuccessListener(new OnSuccessListener<ShortDynamicLink>() {
            @Override
            public void onSuccess(ShortDynamicLink shortDynamicLink) {
                System.out.println(shortDynamicLink.getShortLink());
            }
        });

        dynamicLink.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                displayErrorToast();
            }
        });
    }

    private void displayErrorToast() {
        Toast.makeText(getApplicationContext(),
                "Failed to create workspace. Try again.",
                Toast.LENGTH_LONG)
                .show();
    }

}