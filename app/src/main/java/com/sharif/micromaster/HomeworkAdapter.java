package com.sharif.micromaster;

import static android.content.Context.DOWNLOAD_SERVICE;



import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.webkit.URLUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
        User creator = db.UserDao().getUserById(homework.getCreator());
        if (creator == null) {
            holder.creator.setText("Unknown creator");
        } else {
            holder.creator.setText(creator.getName());
        }
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDownload(homework.getPdfLink());
            }
        });
    }

    private void startDownload(String pdfLink) {
        Uri uri = Uri.parse(pdfLink);
        String scheme = uri.getScheme();
        if (uri.getHost() == null || !("http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme))) {
            Toast.makeText(context, "Homework link is not valid", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            DownloadManager.Request request = new DownloadManager.Request(uri);
            String fileName = URLUtil.guessFileName(pdfLink, null, null);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
            if (downloadManager == null) {
                Toast.makeText(context, "Download service is not available", Toast.LENGTH_SHORT).show();
                return;
            }
            downloadManager.enqueue(request);
        } catch (IllegalArgumentException e) {
            Toast.makeText(context, "Homework link is not valid", Toast.LENGTH_SHORT).show();
        }
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
