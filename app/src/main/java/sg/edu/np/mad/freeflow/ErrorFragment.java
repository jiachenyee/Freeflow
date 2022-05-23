package sg.edu.np.mad.freeflow;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ErrorFragment extends Fragment {

    private OnRetryActionHandler retryActionHandler;

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_error, container, false);
    }

    private void retry() {
        if (retryActionHandler != null) retryActionHandler.onRetry();
    }

    public interface OnRetryActionHandler {
        public void onRetry();
    }
}