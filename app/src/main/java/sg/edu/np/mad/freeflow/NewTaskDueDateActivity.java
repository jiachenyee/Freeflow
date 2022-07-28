package sg.edu.np.mad.freeflow;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import androidx.activity.result.ActivityResult;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@RequiresApi(api = Build.VERSION_CODES.O)
public class NewTaskDueDateActivity
        extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    // region Static Properties
    public static final int REQUEST_CODE = 0;
    public static final String CHOSEN_DUE_DATE = "dueDate";
    public static final String CHOSEN_DUE_TIME = "dueTime";
    public static final String ACCENT_COLOR = "workspaceAccentColor";

    private static final int DIALOG_STYLE = AlertDialog.THEME_HOLO_LIGHT;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMMM yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH mm");
    // endregion

    // region Static Method To Start Activity
    public static void startActivityForResult(final AppCompatActivity context, final int accentColor) {
        final Intent intent = new Intent(context, NewTaskDueDateActivity.class);
        intent.putExtra(ACCENT_COLOR, accentColor);

        context.startActivityForResult(intent, REQUEST_CODE);
    }
    // endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task_due_date);

        this.initDatePicker();
        this.initTimePicker();
        this.initConfirmBtn();
        this.setAccentColor();
    }

    // region Ui Initialization
    private void initDatePicker() {
        final Button btnPicker = findViewById(R.id.due_date_picker);

        btnPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View ignored) {
                final LocalDate date = LocalDate.now();
                final int year = date.getYear();
                final int month = date.getMonthValue();
                final int day = date.getDayOfMonth();

                final DatePickerDialog dialog = new DatePickerDialog(NewTaskDueDateActivity.this, DIALOG_STYLE, NewTaskDueDateActivity.this, year, month, day);
                dialog.show();
            }
        });
    }

    private void initTimePicker() {
        final Button btnPicker = findViewById(R.id.due_time_picker);

        btnPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View ignored) {
                final LocalTime now = LocalTime.now();
                final int hour = now.getHour();
                final int minute = now.getMinute();

                final TimePickerDialog dialog = new TimePickerDialog(NewTaskDueDateActivity.this, DIALOG_STYLE, NewTaskDueDateActivity.this, hour, minute, true);
                dialog.show();
            }
        });
    }

    /**
     * Get accent color from intent and set it to the header, button create
     */
    private void setAccentColor() {
        final int resource = this.getIntent().getIntExtra(ACCENT_COLOR, 0);

        final LinearLayout header = findViewById(R.id.header_view);
        final Button btnConfirm = findViewById(R.id.create_button);

        header.setBackgroundResource(resource);
        btnConfirm.setBackgroundResource(resource);
    }


    /**
     * Initialize the {@code R.id.create_button} view. <br />
     * Create an intent containing both selected <b>DATE</b> and selected <b>TIME</b> and set it as a <b>{@link ActivityResult}</b>
     */
    private void initConfirmBtn() {
        final Button btnConfirm = findViewById(R.id.create_button);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View ignored) {
                final Button btnDatePicker = NewTaskDueDateActivity.this.findViewById(R.id.due_date_picker);
                final Button btnTimePicker = NewTaskDueDateActivity.this.findViewById(R.id.due_time_picker);

                final String date = btnDatePicker.getText().toString();
                final String time = btnTimePicker.getText().toString();

                // Prepare for activity result intent
                final Intent intent = new Intent();
                intent.putExtra(CHOSEN_DUE_DATE, date);
                intent.putExtra(CHOSEN_DUE_TIME, time);

                NewTaskDueDateActivity.this.setResult(REQUEST_CODE, intent); // Set activity result
                NewTaskDueDateActivity.this.finish(); // End this current activity
            }
        });
    }
    // endregion

    /**
     * <i>OnDateSetListener for {@link DatePickerDialog}.</i> <br />
     * Set the text of the {@code R.id.due_date_picker} to the selected date.
     *
     * @param view  Dialog view
     * @param year  Chosen Year
     * @param month Chosen Month in number
     * @param day   Chosen Day of Month
     */
    @Override
    public void onDateSet(final DatePicker view, final int year, final int month, final int day) {
        final LocalDate date = LocalDate.of(year, month + 1, day);
        final Button btnPicker = this.findViewById(R.id.due_date_picker);

        btnPicker.setText(date.format(DATE_FORMATTER));
    }

    /**
     * <i>OnTimeSetListener for {@link TimePickerDialog}.</i> <br />
     * Set the text of the {@code R.id.due_time_picker} to the selected time.
     *
     * @param view   Dialog view
     * @param hour   Chosen Hour
     * @param minute Chosen Minute
     */
    @Override
    public void onTimeSet(final TimePicker view, final int hour, final int minute) {
        final LocalTime time = LocalTime.of(hour, minute);
        final Button btnPicker = this.findViewById(R.id.due_time_picker);

        btnPicker.setText(time.format(TIME_FORMATTER));
    }
}