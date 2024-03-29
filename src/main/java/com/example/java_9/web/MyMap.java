package com.example.java_9.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MyMap {
    private Map<String, BufferedImage> images;

    public MyMap(){
        images = new HashMap<>();
    }

    public Map<String, BufferedImage> getImages() {
        return images;
    }

    public void setImages(Map<String, BufferedImage> images) {
        this.images = images;
    }

    public BufferedImage getImage(String key){
        return images.get(key);
    }

    public void setImage(String key, BufferedImage image){
        images.put(key, image);
    }

    public Set<String> getIdArray(){
        return images.keySet();
    }

    public void removeImage(String key) { images.remove(key); }

    public String toJson() throws JsonProcessingException {
        ObjectMapper obj = new ObjectMapper();
        return obj.writeValueAsString(images.keySet());
    }
}
