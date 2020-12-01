package com.example.iccet2020.ui.TakeNote;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TakeNoteViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TakeNoteViewModel() {
        mText = new MutableLiveData<>();

    }

    public LiveData<String> getText() {
        return mText;
    }
}