package sg.edu.np.mad.freeflow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class AssigneeAdapter extends RecyclerView.Adapter<AssigneeViewHolder>{
    ArrayList<User> assigneeList;
    Context context;
    public AssigneeAdapter(ArrayList<User> assigneeList, Context context){
        this.assigneeList = assigneeList;
        this.context = context;
    }


    @NonNull
    @Override
    public AssigneeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.assignee_view_holder, null, false);
        return new AssigneeViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull AssigneeViewHolder holder, int position) {
        System.out.println(assigneeList.size() + "HEREEE");
        System.out.println(position);
        User user = assigneeList.get(position);
        System.out.println(user.name);
        String name = user.name;
        //String imageURL = user.profilePictureURL;

        holder.userNameTextView.setText(name);

    }

    @Override
    public int getItemCount() {
        return assigneeList.size();
    }


}
