package sg.edu.np.mad.freeflow;

import android.content.Intent;
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
        View item = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.link_card,
                parent,
                false);

        TaskDetailViewHolder viewHolder = new TaskDetailViewHolder(item);

        int color = taskActivity.getIntent().getExtras().getInt("accentColor");

        viewHolder.setAccentColor(color);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskDetailViewHolder holder, int position) {
        WebsiteInformation information = websiteInformationArrayList.get(position);

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
    }

    @Override
    public int getItemCount() {
        return websiteInformationArrayList.size();
    }
}
