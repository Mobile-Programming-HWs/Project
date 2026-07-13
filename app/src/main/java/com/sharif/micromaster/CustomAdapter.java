package com.sharif.micromaster;


import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    private final List<Course> courseList;
    private final Database db;

    public CustomAdapter(List<Course> courseList, Context context) {
        this.courseList = courseList == null ? Collections.emptyList() : courseList;
        db = context == null ? null : Database.getInstance(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Course course = courseList.get(position);
        if (course == null) {
            bindMissingCourse(holder);
            return;
        }

        holder.name.setText(displayText(course.getName(), "Untitled course"));
        holder.description.setText(displayText(course.getDescription(), "No description"));
        holder.units.setText(course.getUnits() + " units");
        holder.lecturer.setText(findTeacherName(course));
        bindImage(holder, course.getImage());
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    private void bindMissingCourse(MyViewHolder holder) {
        holder.name.setText("Untitled course");
        holder.description.setText("No description");
        holder.lecturer.setText("Unknown lecturer");
        holder.units.setText("0 units");
        holder.imageView.setImageResource(R.drawable.ic_launcher_background);
    }

    private String findTeacherName(Course course) {
        if (db == null) {
            return "Unknown lecturer";
        }
        User user = db.UserDao().getUserById(course.getTeacherID());
        if (user == null) {
            return "Unknown lecturer";
        }
        return displayText(user.getName(), "Unknown lecturer");
    }

    private void bindImage(MyViewHolder holder, String image) {
        if (image == null || image.trim().isEmpty()) {
            holder.imageView.setImageResource(R.drawable.ic_launcher_background);
            return;
        }
        try {
            Bitmap bitmap = BitmapHelper.stringToBitmap(image);
            if (bitmap == null) {
                holder.imageView.setImageResource(R.drawable.ic_launcher_background);
            } else {
                holder.imageView.setImageBitmap(bitmap);
            }
        } catch (IllegalArgumentException | NullPointerException e) {
            holder.imageView.setImageResource(R.drawable.ic_launcher_background);
        }
    }

    private String displayText(String value, String fallback) {
        if (value == null || value.trim().isEmpty()) {
            return fallback;
        }
        return value.trim();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name, description, lecturer, units;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            lecturer = itemView.findViewById(R.id.lecturer);
            units = itemView.findViewById(R.id.units);
        }
    }
}
