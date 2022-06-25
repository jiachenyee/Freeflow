package sg.edu.np.mad.freeflow;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class WorkspaceBuilder {
    String workspaceName;
    int workspaceColor = 1;
    Bitmap workspaceIcon;

    OnSuccessListener successListener;
    OnErrorListener errorListener;

    Uri workspaceInviteURI;
    Uri workspaceIconURI;

    Boolean isImageSetUp = false;

    public static WorkspaceBuilder newInstance()
    {
        return new WorkspaceBuilder();
    }

    public WorkspaceBuilder setWorkspaceName(String workspaceName) {
        this.workspaceName = workspaceName;
        return this;
    }

    public WorkspaceBuilder setAccentColor(int workspaceColor) {
        this.workspaceColor = workspaceColor;
        return this;
    }

    public WorkspaceBuilder setWorkspaceIcon(Bitmap icon) {
        this.workspaceIcon = icon;
        return this;
    }

    public WorkspaceBuilder setOnSuccessListener(OnSuccessListener successListener) {
        this.successListener = successListener;
        return this;
    }

    public WorkspaceBuilder setOnErrorListener(OnErrorListener errorListener) {
        this.errorListener = errorListener;
        return this;
    }

    public void build() {
        uploadBitmap(workspaceIcon);
        createDynamicLink();
    }

    public interface OnSuccessListener {
        public void onSuccess(String workspaceID);
    }

    public interface OnErrorListener {
        public void onError();
    }

    private void uploadBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            isImageSetUp = true;
            attemptWorkspaceCreation();
            return;
        }

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
                throwError();
            }
        });

        uploadTask.addOnSuccessListener(new com.google.android.gms.tasks.OnSuccessListener<UploadTask.TaskSnapshot>() {
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

                            workspaceIconURI = downloadUri;
                            isImageSetUp = true;
                            attemptWorkspaceCreation();
                        } else {
                            throwError();
                        }
                    }
                });
            }
        });
    }

    private void createDynamicLink() {
        UUID uuid = UUID.randomUUID();

        Task<ShortDynamicLink> dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://jiachen.app/" + uuid.toString().toLowerCase()))
                .setDomainUriPrefix("https://npff.page.link")
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                .buildShortDynamicLink(ShortDynamicLink.Suffix.SHORT);

        dynamicLink.addOnSuccessListener(new com.google.android.gms.tasks.OnSuccessListener<ShortDynamicLink>() {
            @Override
            public void onSuccess(ShortDynamicLink shortDynamicLink) {
                workspaceInviteURI = shortDynamicLink.getShortLink();
                attemptWorkspaceCreation();
            }
        });

        dynamicLink.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                throwError();
            }
        });
    }

    private void throwError() {
        if (errorListener != null) {
            errorListener.onError();
        }
    }

    private void attemptWorkspaceCreation() {
        if (workspaceInviteURI == null || !isImageSetUp) return;

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> workspace = new HashMap<>();
        workspace.put("accentColor", workspaceColor);
        workspace.put("inviteCode", workspaceInviteURI.toString().substring(23));
        workspace.put("name", workspaceName);

        if (workspaceIconURI != null) {
            workspace.put("workspaceIconURL", workspaceIconURI);
        }

        String[] arr = {userID};

        workspace.put("admin", Arrays.asList(arr));
        workspace.put("users", Arrays.asList(arr));

        db.collection("workspaces")
                .add(workspace)
                .addOnSuccessListener(new com.google.android.gms.tasks.OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        DocumentReference userReference = db.collection("users").document(userID);

                        userReference.update("workspaces", FieldValue.arrayUnion(documentReference.getId())).addOnSuccessListener(new com.google.android.gms.tasks.OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                if (successListener != null) {
                                    successListener.onSuccess(documentReference.getId());
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                throwError();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        throwError();
                    }
                });
    }
}
