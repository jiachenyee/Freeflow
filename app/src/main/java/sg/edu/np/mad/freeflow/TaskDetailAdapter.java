package sg.edu.np.mad.freeflow;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.circularreveal.CircularRevealWidget;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import io.umehara.ogmapper.OgMapper;
import io.umehara.ogmapper.domain.OgTags;
import io.umehara.ogmapper.jsoup.JsoupOgMapperFactory;

public class TaskDetailAdapter extends RecyclerView.Adapter<TaskDetailViewHolder> {

    TaskActivity taskActivity;
    ArrayList<WebsiteInformation> websiteInformationArrayList;

    public TaskDetailAdapter(ArrayList<String> websiteArrayList, TaskActivity taskActivity) {

        this.taskActivity = taskActivity;

        websiteInformationArrayList = new ArrayList<>();

        for (String website : websiteArrayList) {
            WebsiteInformation info = new WebsiteInformation(website, null, null);

            websiteInformationArrayList.add(info);

            OgMapper ogMapper = new JsoupOgMapperFactory().build();

            Thread main = Thread.currentThread();

            Runnable r = new Runnable() {
                public void run() {
                    try {
                        URL url = new URL(website);
                        OgTags result = ogMapper.process(url);

                        if (result != null) {
                            if (result.getTitle() != null) {
                                info.name = result.getTitle();
                            }
                            if (result.getDescription() != null) {
                                info.description = result.getDescription();
                            }
                        }

                        taskActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                notifyDataSetChanged();
                            }
                        });

                    } catch (MalformedURLException e) {
                    }
                }
            };

            new Thread(r).start();
        }
    }

    @NonNull
    @Override
    public TaskDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == 0) {
            View item = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.link_card,
                    parent,
                    false);

            TaskDetailViewHolder viewHolder = new TaskDetailViewHolder(item);

            int color = taskActivity.getIntent().getExtras().getInt("accentColor");

            viewHolder.setAccentColor(color);

            return viewHolder;
        } else if (viewType == 1) {
            View item = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.link_header_card,
                    parent,
                    false);

            TaskDetailViewHolder viewHolder = new TaskDetailViewHolder(item);

            return viewHolder;
        } else {
            View item = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.subtask_card,
                    parent,
                    false);

            TaskDetailViewHolder viewHolder = new TaskDetailViewHolder(item);

            return viewHolder;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == websiteInformationArrayList.size() + 1) {
            return 1;
        } else if (position < websiteInformationArrayList.size() + 2) {
            return 0;
        } else {
            return 2;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull TaskDetailViewHolder holder, int position) {
        switch (holder.getViewType()) {
            case HEADER:
                if (position == 0) {
                    holder.titleTextView.setText("Links and Attachments");
                } else {
                    holder.titleTextView.setText("Subtasks");
                }
                break;
            case WEBSITE:
                WebsiteInformation information = websiteInformationArrayList.get(position - 1);

                if (information.url != null) {
                    holder.websiteURLTextView.setText(information.url);

                    holder.root.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(information.url));
                            taskActivity.startActivity(i);
                        }
                    });
                    holder.websiteURLTextView.setVisibility(View.VISIBLE);
                } else {
                    holder.websiteURLTextView.setVisibility(View.GONE);
                }

                if (information.name != null) {
                    holder.websiteTitleTextView.setText(information.name);
                    holder.websiteTitleTextView.setVisibility(View.VISIBLE);
                } else {
                    holder.websiteTitleTextView.setVisibility(View.GONE);
                }

                if (information.description != null) {
                    holder.websiteDescriptionTextView.setText(information.description);
                    holder.websiteDescriptionTextView.setVisibility(View.VISIBLE);
                } else {
                    holder.websiteDescriptionTextView.setVisibility(View.GONE);
                }
                break;
            case SUBTASK:
                int realPosition = position - 2 - websiteInformationArrayList.size();

                Map<String, Object> task = taskActivity.subtasks.get(realPosition);

                holder.taskTitleTextView.setText((String) task.get("title"));

                boolean isCompleted = (boolean) task.get("isCompleted");

                Drawable image = taskActivity.getResources().getDrawable(isCompleted ? R.drawable.ic_baseline_check_box_24 : R.drawable.ic_baseline_check_box_outline_blank_24);
                holder.checkboxImageView.setImageDrawable(image);

                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        taskActivity.toggleMarkAsComplete(realPosition);
                    }
                });

                break;
        }
    }

    @Override
    public int getItemCount() {
        return websiteInformationArrayList.size() + taskActivity.subtasks.size() + 2;
    }
}
