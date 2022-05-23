package sg.edu.np.mad.freeflow;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ErrorFragment extends Fragment {

    private OnRetryActionHandler retryActionHandler;

    Button retryButton;

    public ErrorFragment() {

    }

    public static ErrorFragment newInstance() {
        return new ErrorFragment();
    }

    public ErrorFragment setOnRetryActionHandler(OnRetryActionHandler retryActionHandler) {
        this.retryActionHandler = retryActionHandler;

        return this;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_error, container, false);

        retryButton = v.findViewById(R.id.retry_button);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retry();
            }
        });
    }

    private void retry() {
        if (retryActionHandler != null) retryActionHandler.onRetry();
    }

    public interface OnRetryActionHandler {
        public void onRetry();
    }
}