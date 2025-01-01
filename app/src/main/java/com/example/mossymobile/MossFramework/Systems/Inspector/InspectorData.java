package com.example.mossymobile.MossFramework.Systems.Inspector;

public class InspectorData {
    public boolean IsReadOnly = false;
    public Object obj = null;

    public InspectorData(Object obj, boolean IsReadOnly)
    {
        this.obj = obj;
        this.IsReadOnly = IsReadOnly;
    }
}
