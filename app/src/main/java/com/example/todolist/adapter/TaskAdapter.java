package com.example.todolist.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.example.todolist.model.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> implements Filterable {

    private List<Task> taskList;
    private List<Task> taskListFull;
    private OnTaskStatusChangeListener listener;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    public interface OnTaskStatusChangeListener {
        void onStatusChanged(Task task, boolean isDone);
        void onTaskClick(Task task);
    }

    public TaskAdapter(List<Task> taskList, OnTaskStatusChangeListener listener) {
        this.taskList = taskList;
        this.taskListFull = new ArrayList<>(taskList);
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.tvTitle.setText(task.getTitle());
        holder.tvContent.setText(task.getContent());
        holder.tvCategory.setText(task.getCategory());
        
        if (task.getDeadline() != null) {
            holder.tvDeadline.setText(dateFormat.format(new Date(task.getDeadline())));
        } else {
            holder.tvDeadline.setText("No deadline");
        }

        holder.cbStatus.setOnCheckedChangeListener(null);
        holder.cbStatus.setChecked(task.getStatus() == 1);
        holder.cbStatus.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (listener != null) {
                listener.onStatusChanged(task, isChecked);
            }
        });

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTaskClick(task);
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void updateData(List<Task> newList) {
        this.taskList = newList;
        this.taskListFull = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return taskFilter;
    }

    private Filter taskFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Task> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(taskListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Task item : taskListFull) {
                    if (item.getTitle() != null && item.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            taskList.clear();
            taskList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvContent, tvCategory, tvDeadline;
        CheckBox cbStatus;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvDeadline = itemView.findViewById(R.id.tvDeadline);
            cbStatus = itemView.findViewById(R.id.cbStatus);
        }
    }
}
