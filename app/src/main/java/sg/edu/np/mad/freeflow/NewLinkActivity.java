package sg.edu.np.mad.freeflow;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewLinkActivity extends AppCompatActivity {

    EditText linkEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_link);

        linkEditText = findViewById(R.id.link_edit_text);

        Bundle extras = getIntent().getExtras();

        setupAccentColor(extras);
        setUpDismiss();
        setUpEditText();
    }

    private void setupAccentColor(Bundle extras) {
        int color = extras.getInt("workspaceAccentColor");

        LinearLayout headerView = findViewById(R.id.header_view);

        headerView.setBackgroundColor(color);
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
                // TODO: Enable or disable create button based on result
                System.out.println(validateURL(linkEditText.getText().toString()));
            }
        });
    }

    private boolean validateURL(String url) {
        Pattern pattern = Pattern.compile("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(url);

        return matcher.matches();
    }
}