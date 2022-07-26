package sg.edu.np.mad.freeflow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewLinkActivity extends AppCompatActivity {

    EditText linkEditText;
    Button createButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_link);

        linkEditText = findViewById(R.id.link_edit_text);
        createButton = findViewById(R.id.create_button);

        Bundle extras = getIntent().getExtras();

        setupAccentColor(extras);
        setUpDismiss();
        setUpEditText();
        setUpButton();
    }

    private void setupAccentColor(Bundle extras) {
        int color = extras.getInt("workspaceAccentColor");

        LinearLayout headerView = findViewById(R.id.header_view);

        headerView.setBackgroundColor(color);
        createButton.setBackgroundColor(color);
    }

    private void setUpDismiss() {
        findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setUpEditText() {
        linkEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Boolean isValid = validateURL(linkEditText.getText().toString());
                createButton.setEnabled(isValid);
                createButton.getBackground().setAlpha(isValid ? 255 : 128);
            }
        });
    }

    private void setUpButton() {
        createButton.setEnabled(false);
        createButton.getBackground().setAlpha(128);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = getIntent();
                i.putExtra("url", linkEditText.getText().toString());

                setResult(200, i);
                finish();
            }
        });
    }

    private boolean validateURL(String url) {
        Pattern pattern = Pattern.compile("https://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(url);

        return matcher.matches();
    }
}