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

import java.util.Collections;
import java.util.List;

public class HomeworkAdapter extends RecyclerView.Adapter<HomeworkAdapter.MyViewHolder> {


    private final List<Homework> homeworkList;
    private final Context context;
    private final Database db;

    public HomeworkAdapter(List<Homework> homeworkList, Context context) {
        this.homeworkList = homeworkList == null ? Collections.emptyList() : homeworkList;
        this.context = context;
        db = context == null ? null : Database.getInstance(context);
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
        if (homework == null) {
            bindMissingHomework(holder);
            return;
        }
        holder.description.setText(displayText(homework.getDescription(), "No description"));
        holder.creator.setText(findCreatorName(homework));
        boolean canDownload = isValidDownloadLink(homework.getPdfLink());
        holder.button.setEnabled(canDownload);
        holder.button.setText(canDownload ? "Download" : "No file");
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDownload(homework.getPdfLink());
            }
        });
    }

    private void startDownload(String pdfLink) {
        if (context == null) {
            return;
        }
        if (!isValidDownloadLink(pdfLink)) {
            Toast.makeText(context, "Homework link is not valid", Toast.LENGTH_SHORT).show();
            return;
        }
        String trimmedLink = pdfLink.trim();
        Uri uri = Uri.parse(trimmedLink);
        try {
            DownloadManager.Request request = new DownloadManager.Request(uri);
            String fileName = URLUtil.guessFileName(trimmedLink, null, null);
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

    private void bindMissingHomework(MyViewHolder holder) {
        holder.description.setText("No description");
        holder.creator.setText("Unknown creator");
        holder.button.setText("No file");
        holder.button.setEnabled(false);
        holder.button.setOnClickListener(null);
    }

    private String findCreatorName(Homework homework) {
        if (db == null) {
            return "Unknown creator";
        }
        User creator = db.UserDao().getUserById(homework.getCreator());
        if (creator == null) {
            return "Unknown creator";
        }
        return displayText(creator.getName(), "Unknown creator");
    }

    private boolean isValidDownloadLink(String pdfLink) {
        if (pdfLink == null) {
            return false;
        }
        String trimmedLink = pdfLink.trim();
        if (trimmedLink.isEmpty() || !URLUtil.isNetworkUrl(trimmedLink)) {
            return false;
        }
        Uri uri = Uri.parse(trimmedLink);
        String scheme = uri.getScheme();
        return uri.getHost() != null && ("http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme));
    }

    private String displayText(String value, String fallback) {
        if (value == null || value.trim().isEmpty()) {
            return fallback;
        }
        return value.trim();
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
