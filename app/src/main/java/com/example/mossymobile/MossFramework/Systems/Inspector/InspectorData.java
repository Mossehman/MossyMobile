package com.example.mossymobile.MossFramework.Systems.Inspector;

import java.io.Serializable;

public class InspectorData implements Serializable {
    public boolean IsReadOnly = false;
    public Object obj = null;

    public InspectorData(Object obj, boolean IsReadOnly)
    {
        this.obj = obj;
        this.IsReadOnly = IsReadOnly;
    }
}
