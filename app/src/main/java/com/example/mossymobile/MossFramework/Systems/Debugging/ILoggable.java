package com.example.mossymobile.MossFramework.Systems.Debugging;

///Helper Interface that allows us to define a Log statement for Debugging
public interface ILoggable {

    /**
     * Allows us to define a specific statement to be output when the class is passed as a parameter in {@code Debug.Log()}.
     *
     * @return The Log Statement defined by the user for the specific class ({@code String})
     */
    String GetLogStatement();
}
