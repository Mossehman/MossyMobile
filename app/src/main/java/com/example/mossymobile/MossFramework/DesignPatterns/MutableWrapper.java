package com.example.mossymobile.MossFramework.DesignPatterns;

import androidx.annotation.NonNull;

import java.io.Serializable;

///This class is meant for passing primitive values by reference (eg: {@code float}s, {@code int}s, etc)
public class MutableWrapper<T> implements Serializable {
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
