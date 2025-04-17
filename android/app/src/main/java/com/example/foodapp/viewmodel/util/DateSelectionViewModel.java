package com.example.foodapp.viewmodel.util;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.time.LocalDate;

public class DateSelectionViewModel extends ViewModel {
    private MutableLiveData<Integer> selectedYear = new MutableLiveData<>();
    private MutableLiveData<Integer> selectedMonth = new MutableLiveData<>();

    public DateSelectionViewModel(Context context) {
        LocalDate currentDate = LocalDate.now();
        selectedYear.setValue(currentDate.getYear());
        selectedMonth.setValue(currentDate.getMonthValue());
    }

    public LiveData<Integer> getSelectedYear() {
        return selectedYear;
    }

    public void setSelectedYear(int year) {
        selectedYear.setValue(year);
    }

    public LiveData<Integer> getSelectedMonth() {
        return selectedMonth;
    }

    public void setSelectedMonth(int month) {
        selectedMonth.setValue(month);
    }
}
