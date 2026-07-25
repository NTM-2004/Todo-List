package com.example.todolist.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.example.todolist.model.Task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;
    private OnTaskActionListener listener;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
    private int lastAnimatedPos = -1;

    public interface OnTaskActionListener {
        void onStatusChanged(Task task, boolean isDone);
        void onTaskClick(Task task);
    }

    public TaskAdapter(List<Task> taskList, OnTaskActionListener listener) {
        this.taskList = taskList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        Context ctx = holder.itemView.getContext();

        // Animate item entrance
        if (position > lastAnimatedPos) {
            holder.itemView.startAnimation(
                    AnimationUtils.loadAnimation(ctx, android.R.anim.slide_in_left));
            lastAnimatedPos = position;
        }

        holder.tvTitle.setText(task.getTitle());
        holder.tvContent.setText(task.getContent());
        holder.tvCategory.setText(task.getCategory());

        // Priority stripe color
        int priorityColor;
        String priorityLabel;
        switch (task.getPriority()) {
            case Task.PRIORITY_HIGH:
                priorityColor = ContextCompat.getColor(ctx, R.color.priority_high);
                priorityLabel = "High";
                break;
            case Task.PRIORITY_MEDIUM:
                priorityColor = ContextCompat.getColor(ctx, R.color.priority_medium);
                priorityLabel = "Medium";
                break;
            default:
                priorityColor = ContextCompat.getColor(ctx, R.color.priority_low);
                priorityLabel = "Low";
        }
        holder.viewPriorityStripe.setBackgroundColor(priorityColor);
        holder.tvPriority.setText(priorityLabel);
        holder.tvPriority.setTextColor(priorityColor);

        // Deadline
        if (task.getDeadline() != null && task.getDeadline() > 0) {
            holder.tvDeadline.setText(dateFormat.format(new Date(task.getDeadline())));
            if (task.isOverdue()) {
                holder.tvDeadline.setTextColor(ContextCompat.getColor(ctx, R.color.priority_high));
                holder.tvDeadline.setText("⚠ " + dateFormat.format(new Date(task.getDeadline())));
            } else {
                holder.tvDeadline.setTextColor(ContextCompat.getColor(ctx, R.color.text_secondary));
            }
        } else {
            holder.tvDeadline.setText("Không có deadline");
            holder.tvDeadline.setTextColor(ContextCompat.getColor(ctx, R.color.text_secondary));
        }

        // Status (strikethrough if done)
        holder.cbStatus.setOnCheckedChangeListener(null);
        holder.cbStatus.setChecked(task.getStatus() == Task.STATUS_COMPLETE);

        if (task.getStatus() == Task.STATUS_COMPLETE) {
            holder.tvTitle.setPaintFlags(holder.tvTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvTitle.setAlpha(0.5f);
        } else {
            holder.tvTitle.setPaintFlags(holder.tvTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.tvTitle.setAlpha(1.0f);
        }

        holder.cbStatus.setOnCheckedChangeListener((btn, isChecked) -> {
            if (listener != null) listener.onStatusChanged(task, isChecked);
        });

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onTaskClick(task);
        });
    }

    @Override
    public int getItemCount() {
        return taskList == null ? 0 : taskList.size();
    }

    public void updateData(List<Task> newList) {
        this.taskList = newList;
        lastAnimatedPos = -1;
        notifyDataSetChanged();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvContent, tvCategory, tvDeadline, tvPriority;
        CheckBox cbStatus;
        View viewPriorityStripe;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvDeadline = itemView.findViewById(R.id.tvDeadline);
            tvPriority = itemView.findViewById(R.id.tvPriority);
            cbStatus = itemView.findViewById(R.id.cbStatus);
            viewPriorityStripe = itemView.findViewById(R.id.viewPriorityStripe);
        }
    }
}
