package com.example.ml_vision;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class LabReport
{
    @SerializedName("Properties")
    @Expose
    private List<String> Properties;
    @SerializedName("Value")
    @Expose
    private List<String> Value ;

    public List<String> getProperties() {
        return Properties;
    }

    public void setProperties(List<String> properties) {
        Properties = properties;
    }

    public List<String> getValue() {
        return Value;
    }

    public void setValue(List<String> value) {
        Value = value;
    }
}
