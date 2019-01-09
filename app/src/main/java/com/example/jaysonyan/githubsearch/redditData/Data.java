package com.example.jaysonyan.githubsearch.redditData;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("after")
    @Expose
    public Object after;

    @Override
    public String toString() {
        return "Data{" +
                "after=" + after +
                ", dist=" + dist +
                ", facets=" + facets +
                ", modhash='" + modhash + '\'' +
                ", children=" + children +
                ", before=" + before +
                '}';
    }

    @SerializedName("dist")
    @Expose
    public Integer dist;
    @SerializedName("facets")
    @Expose
    public Facets facets;
    @SerializedName("modhash")
    @Expose
    public String modhash;
    @SerializedName("children")
    @Expose
    public List<Child_> children = null;
    @SerializedName("before")
    @Expose
    public Object before;

}