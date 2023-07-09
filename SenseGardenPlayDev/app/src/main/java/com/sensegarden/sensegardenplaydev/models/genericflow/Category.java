package com.sensegarden.sensegardenplaydev.models.genericflow;

import java.util.ArrayList;

public class Category {
    private String name;
    private String url;
    private ArrayList<String> images;

    public Category(String name, String url, ArrayList<String> images) {
        this.name = name;
        this.url = url;
        this.images = images;
    }

    public Category() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public void addImage(String image) {
        if (images == null) images = new ArrayList<>();

        images.add(image);
    }

    public void addImages(ArrayList<String> imgs) {
        if (images == null) images = new ArrayList<>();
        if (imgs != null) images.addAll(imgs);
    }

    public void removeImages(ArrayList<String> imgs) {
        for (int i = images.size() - 1; i >= 0; i--) {
            if (imgs.contains(images.get(i)))
                images.remove(i);

        }
    }
}
