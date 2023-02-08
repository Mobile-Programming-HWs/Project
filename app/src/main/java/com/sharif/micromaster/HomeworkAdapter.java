package com.sharif.micromaster;

import static android.content.Context.DOWNLOAD_SERVICE;



import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HomeworkAdapter extends RecyclerView.Adapter<HomeworkAdapter.MyViewHolder> {


    List<Homework> homeworkList;
    Context context;
    Database db;

    public HomeworkAdapter(List<Homework> homeworkList, Context context) {
        this.homeworkList = homeworkList;
        this.context = context;
        db = Database.getInstance(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.homework_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Homework homework = homeworkList.get(position);
        holder.description.setText(homework.getDescription());
        holder.creator.setText(db.UserDao().getUserById(homework.getCreator()).getName());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadManager.Request r = new DownloadManager.Request(Uri.parse(homework.getPdfLink()));
                r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "homeworks");
                r.allowScanningByMediaScanner();
                r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                DownloadManager dm = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(r);
            }
        });
    }

    @Override
    public int getItemCount() {
        return homeworkList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView description, creator;
        Button button;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.hw_description);
            creator = itemView.findViewById(R.id.hw_name);
            button = itemView.findViewById(R.id.hw_download);
        }
    }
}
