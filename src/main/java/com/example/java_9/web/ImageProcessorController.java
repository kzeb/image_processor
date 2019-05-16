package com.example.java_9.web;

import com.fasterxml.jackson.core.JsonProcessingException;

import javax.imageio.ImageIO;
import javax.servlet.ServletInputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ImageProcessorController {
    private MyMap imagesMap = new MyMap();
   // private String uuid;

    public ImageProcessorController() {
    }

    public String addImageToMap(ServletInputStream stream) throws IOException {
        BufferedImage imageNew = ImageIO.read(stream);
        String uuid = UUID.randomUUID().toString();
        imagesMap.setImage(uuid, imageNew);
        return "{ \"key\": \"" + uuid + "\", \"height\": \"" + imageNew.getHeight() + "\", \"width\": \"" + imageNew.getWidth() + "\" }";
    }

    public void deleteImageFromMap(String id) throws ImageNotFoundException {
        if(imagesMap.getImage(id) == null){
            throw new ImageNotFoundException();
        } else {
            imagesMap.removeImage(id);
        }
    }

    public byte[] getImageFromMap(String id) throws IOException {
        BufferedImage temp = imagesMap.getImage(id);
        if(temp == null){
            throw new ImageNotFoundException();
        } else {
            return convertToByteArray(temp);
        }
    }

    public byte[] convertToByteArray(BufferedImage image) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", bos);
        return bos.toByteArray();
    }

    public String getImageSizeJson(String id){
        BufferedImage temp = imagesMap.getImage(id);
        if(temp == null){
            throw new ImageNotFoundException();
        } else {
            return "{ \"key\": \"" + id + "\", \"height\": \"" + temp.getHeight() + "\", \"width\": \"" + temp.getWidth() + "\" }";
        }
    }

    public String getUploadedFiles() throws JsonProcessingException {
        return imagesMap.toJson();
    }
}

