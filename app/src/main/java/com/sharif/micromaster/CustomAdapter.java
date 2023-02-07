package com.sharif.micromaster;


import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    //create a list to pass our Model class
    List<Course> courseList;
    Database db;
    Context context;
    public CustomAdapter(List<Course> courseList, Context context) {
        this.courseList = courseList;
        this.context = context;
        db = Database.getInstance(context);
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//inflate our custom view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_item,parent,false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//bind all custom views by its position
//to get the positions we call our Model class
        final Course course = courseList.get(position);
        holder.name.setText(course.getName());
        holder.description.setText(course.getDescription());
        holder.imageView.setImageBitmap(BitmapHelper.stringToBitmap(course.getImage()));
        holder.units.setText(String.valueOf(course.getUnits()));
        User user = db.UserDao().getUserById(course.getTeacherID());
        holder.lecturer.setText(user.getName());
    }
    @Override
    public int getItemCount() {
        return courseList.size();
    }

    //all the custom view will be hold here or initialize here inside MyViewHolder
    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView name, description, lecturer, units;
        RelativeLayout relativeLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            lecturer = itemView.findViewById(R.id.lecturer);
            units = itemView.findViewById(R.id.units);
            relativeLayout = itemView.findViewById(R.id.item);
        }
    }
}