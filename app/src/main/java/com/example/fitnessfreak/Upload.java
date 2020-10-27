package com.example.fitnessfreak;

public class Upload {

    private String vName;
    private String vImageUrl;

    public Upload() {
        //Empty constructor
    }

    public Upload(String name, String imageUrl) {
        if (name.trim().equals("")) {
            name = "No Name";
        }

        vName = name;
        vImageUrl = imageUrl;
    }

    public String getName() {
        return vName;
    }

    public void setName(String name) {
        vName = name;
    }

    public String getImageUrl() {
        return vImageUrl;
    }

    public void setImageUrl(String imageUrl)
    {
        vImageUrl = imageUrl;
    }

}
