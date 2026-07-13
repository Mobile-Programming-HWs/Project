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
        Context context = holder.itemView.getContext();
        holder.units.setText(context.getString(R.string.units_format, course.getUnits()));
        holder.lecturer.setText(context.getString(R.string.teacher_format, findTeacherName(course)));
        holder.homeworkCount.setText(context.getString(
                R.string.course_homework_count_format,
                findHomeworkCount(course)
        ));
        bindImage(holder, course.getImage());
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    private void bindMissingCourse(MyViewHolder holder) {
        Context context = holder.itemView.getContext();
        holder.name.setText("Untitled course");
        holder.description.setText("No description");
        holder.lecturer.setText(context.getString(R.string.teacher_format, "Unknown lecturer"));
        holder.units.setText(context.getString(R.string.units_format, 0));
        holder.homeworkCount.setText(context.getString(R.string.course_homework_count_format, 0));
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

    private int findHomeworkCount(Course course) {
        if (db == null || course == null) {
            return 0;
        }
        List<Homework> homeworks = db.HomeworkDao().getHomeworksByCourseId(course.getId());
        return homeworks == null ? 0 : homeworks.size();
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
        TextView name, description, lecturer, units, homeworkCount;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            lecturer = itemView.findViewById(R.id.lecturer);
            units = itemView.findViewById(R.id.units);
            homeworkCount = itemView.findViewById(R.id.course_homework_count);
        }
    }
}
