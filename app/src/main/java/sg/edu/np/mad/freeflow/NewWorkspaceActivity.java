package sg.edu.np.mad.freeflow;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.material.card.MaterialCardView;

import java.io.IOException;

public class NewWorkspaceActivity extends AppCompatActivity {

    private MaterialCardView workspaceAccent1;
    private MaterialCardView workspaceAccent2;
    private MaterialCardView workspaceAccent3;
    private MaterialCardView workspaceAccent4;
    private MaterialCardView workspaceAccent5;
    private MaterialCardView workspaceAccent6;

    private ImageView workplaceImage;

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

    private void updateAccentColorSelector() {
        workspaceAccent1.setStrokeWidth(selectedColorIndex == 1 ? 21 : 0);
        workspaceAccent2.setStrokeWidth(selectedColorIndex == 2 ? 21 : 0);
        workspaceAccent3.setStrokeWidth(selectedColorIndex == 3 ? 21 : 0);
        workspaceAccent4.setStrokeWidth(selectedColorIndex == 4 ? 21 : 0);
        workspaceAccent5.setStrokeWidth(selectedColorIndex == 5 ? 21 : 0);
        workspaceAccent6.setStrokeWidth(selectedColorIndex == 6 ? 21 : 0);
    }
}