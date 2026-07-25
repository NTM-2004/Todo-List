package com.example.todolist.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.todolist.db.DB;

public class TaskViewModel extends ViewModel {
    private final MutableLiveData<String> searchQuery = new MutableLiveData<>("");
    private final MutableLiveData<String> sortOrder = new MutableLiveData<>(DB.SORT_BY_CREATED);

    public void setSearchQuery(String query) {
        searchQuery.setValue(query);
    }

    public LiveData<String> getSearchQuery() {
        return searchQuery;
    }

    public void setSortOrder(String order) {
        sortOrder.setValue(order);
    }

    public LiveData<String> getSortOrder() {
        return sortOrder;
    }
}