package com.example.mossymobile.MossFramework.DesignPatterns;

import androidx.annotation.NonNull;

///This class is meant for passing primitive values by reference (eg: {@code float}s, {@code int}s, etc)
public class MutableWrapper<T> {
    public T value;
    public MutableWrapper(T value)
    {
        this.value = value;
    }

    public void SetValue(T value) {
        this.value = value;
    }

    @NonNull
    @Override
    public String toString() {
        return value.toString();
    }
}
