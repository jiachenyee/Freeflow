package sg.edu.np.mad.freeflow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    private static final String DEFAULT_DUE_DATE = "DD MMMM YYYY HH mm";

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    TextView usernameTextView;
    ImageView profileImageView;
    TextView subtitleTextView;
    //Create SearchView Variable
    SearchView taskSearchView;

    FirebaseFirestore db;

    ArrayList<String> workspaceIDs;
    ArrayList<Workspace> workspaces = new ArrayList<>();

    ArrayList<TaskWorkspaceWrapper> tasks = new ArrayList<>();

    HomeFragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        usernameTextView = findViewById(R.id.username_text_view);
        profileImageView = findViewById(R.id.their_message_profilepic);
        subtitleTextView = findViewById(R.id.subtitle_text_view);

        db = FirebaseFirestore.getInstance();

        // When the user clicks the profile image, show the account activity
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent accountActivity = new Intent(MainActivity.this, AccountActivity.class);
                startActivity(accountActivity);
            }
        });

        // This is for testing, set up empty state
        setUpEmptyState();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() == null) {
            // The user is not signed in, show sign in interface
            Intent signInActivity = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(signInActivity);
        } else {
            if (user == null) {
                // User is signed in, set `user` to the current user
                user = mAuth.getCurrentUser();

                // Set up the interface for the user
                setUpUser();
            }
        }
    }

    // Set up username and profile picture
    private void setUpUser() {
        usernameTextView.setText("Hello " + user.getDisplayName() + "!");

        // Add profile picture, make async HTTP request to download pfp
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(user.getPhotoUrl().toString());
                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                    runOnUiThread(()->{
                        profileImageView.setImageBitmap(bmp);
                    });
                    System.out.println("success");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        loadUserInformation();
    }

    private void loadUserInformation() {
        db.collection("users").document(user.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.getData() != null) {
                            workspaceIDs = (ArrayList<String>) documentSnapshot.getData().get("workspaces");
                            setUpWorkspaces();
                        } else {
                            System.out.println("Error");
                            System.out.println(user.getUid());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        setUpErrorState();
                    }
                });
    }

    // Create empty state if the user does not currently have a workspace.
    private void setUpEmptyState() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_fragment, new HomeEmptyStateFragment(this));
        ft.commit();
    }

    private void setUpHomeFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        homeFragment = HomeFragment.newInstance(this).setWorkspaces(workspaces, tasks);

        ft.replace(R.id.content_fragment, homeFragment);
        ft.commit();
    }

    private void setUpWorkspaces() {
        if (workspaceIDs == null || workspaceIDs.isEmpty()) { setUpEmptyState(); return; }

        setUpHomeFragment();

        for (int i = 0; i < workspaceIDs.size(); i++) {
            String workspaceID = workspaceIDs.get(i);

            DocumentReference workspaceDocumentReference = db.collection("workspaces").document(workspaceID);
            workspaceDocumentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Map<String, Object> data = document.getData();

                            Workspace workspace = new Workspace(data, document.getId()).setImageLoadHandler(new Workspace.OnImageLoadHandler() {
                                @Override
                                public void onImageLoad() {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            homeFragment.reloadData();
                                        }
                                    });
                                }
                            });
                            workspaces.add(workspace);
                            homeFragment.reloadData();

                            if (workspaceIDs.size() == workspaces.size()) {
                                loadAllTasks();
                            }
                        }
                    } else {

                    }
                }
            });
        }
    }

    private void setUpErrorState() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_fragment, ErrorFragment.newInstance().setOnRetryActionHandler(new ErrorFragment.OnRetryActionHandler() {
            @Override
            public void onRetry() {
                loadUserInformation();
            }
        }));
        ft.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 200 && data.getStringExtra("workspaceID") != null) {
            // Returned from new workspace activity
            if (workspaceIDs == null) {
                workspaceIDs = new ArrayList<>();
            }
            workspaceIDs.add(data.getStringExtra("workspaceID"));

            workspaces = new ArrayList<>();

            setUpWorkspaces();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadAllTasks() {
        tasks = new ArrayList<>();

        for (Workspace workspace: workspaces) {
            db.collection("workspaces").document(workspace.id).collection("tasks").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            String taskName = (String) document.getData().get("title");
                            String taskDueDate_string = (String) document.getData().get("dueDate");
                            String taskDescription = (String) document.getData().get("description");

                            tasks.add(new TaskWorkspaceWrapper(new sg.edu.np.mad.freeflow.Task(taskName, taskDueDate_string, taskDescription, document.getId()), workspace.id, workspace.accentColor - 1));
                        }

                        tasks = filterByTaskDueDate(tasks);
                        refreshTasksRecyclerView();
                    } else {
                        Toast.makeText(MainActivity.this, "Failed to load some tasks", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private ArrayList<TaskWorkspaceWrapper> filterByTaskDueDate(ArrayList<TaskWorkspaceWrapper> taskList) {
        return taskList.stream()
                .filter(new Predicate<TaskWorkspaceWrapper>() {
                    @Override
                    public boolean test(TaskWorkspaceWrapper taskWorkspaceWrapper) {
                        return taskWorkspaceWrapper.task.dueDate != null && !DEFAULT_DUE_DATE.equals(taskWorkspaceWrapper.task.dueDate);
                    }
                })
                .filter(new Predicate<TaskWorkspaceWrapper>() {
                    @Override
                    public boolean test(TaskWorkspaceWrapper workspace) {
                        final LocalDate today = LocalDate.now();

                        final LocalDate date = LocalDate.parse(workspace.task.dueDate, DateTimeFormatter.ofPattern("d MMMM yyyy HH mm"));

                        return today.isEqual(date) || today.isAfter(date);
                    }
                }).collect(Collectors.toCollection(ArrayList::new));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private ArrayList<TaskWorkspaceWrapper> sortAlphabetically(ArrayList<TaskWorkspaceWrapper> taskList) {
        return taskList.stream()
                .sorted(new Comparator<TaskWorkspaceWrapper>() {
                    @Override
                    public int compare(TaskWorkspaceWrapper o1, TaskWorkspaceWrapper o2) {
                        return o1.task.title.compareTo(o2.task.title);
                    }
                }).collect(Collectors.toCollection(ArrayList::new));
    }

    void refreshTasksRecyclerView() {
        int taskCount = tasks.size();
        if (taskCount == 1) {
            subtitleTextView.setText(Integer.toString(taskCount) + " task remaining");
        } else {
            subtitleTextView.setText(Integer.toString(taskCount) + " tasks remaining");
        }

        setUpHomeFragment();
    }

}