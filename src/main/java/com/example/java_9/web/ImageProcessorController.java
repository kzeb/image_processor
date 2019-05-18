package com.example.java_9.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import javax.imageio.ImageIO;
import javax.servlet.ServletInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

public class ImageProcessorController {

    private MyMap imagesMap = new MyMap();

    public ImageProcessorController() {
    }

    public String addImageToMap(ServletInputStream stream) throws IOException {
        BufferedImage imageNew = ImageIO.read(stream);
        String uuid = UUID.randomUUID().toString();
        imagesMap.setImage(uuid, imageNew);
        return "{ \"id\": \"" + uuid + "\", \"height\": \"" + imageNew.getHeight() + "\", \"width\": \"" + imageNew.getWidth() + "\" }";
    }

    public String deleteImageFromMap(String id) throws ImageNotFoundException {
        if(imagesMap.getImage(id) == null){
            throw new ImageNotFoundException();
        } else {
            imagesMap.removeImage(id);
            return "Image deleted successfully!";
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
            return "{ \"id\": \"" + id + "\", \"height\": \"" + temp.getHeight() + "\", \"width\": \"" + temp.getWidth() + "\" }";
        }
    }

    public String getUploadedFiles() throws JsonProcessingException {
        return imagesMap.toJson();
    }

    public byte[] getGrayscaleImageFromMap(String id) throws IOException {
        BufferedImage image = imagesMap.getImage(id);
        if(image == null){
            throw new ImageNotFoundException();
        } else {
            int width = image.getWidth();
            int height = image.getHeight();
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    Color c = new Color(image.getRGB(j, i));
                    int red = (int) (c.getRed() * 0.299);
                    int green = (int) (c.getGreen() * 0.587);
                    int blue = (int) (c.getBlue() * 0.114);
                    Color newColor = new Color(red + green + blue,
                            red + green + blue, red + green + blue);
                    image.setRGB(j, i, newColor.getRGB());
                }
            }
            return convertToByteArray(image);
        }
    }

    public byte[] getScaledImageFromMap(String id, float percent) throws IOException {
        BufferedImage originalImage = imagesMap.getImage(id);
        if(originalImage == null){
            throw new ImageNotFoundException();
        } else {
            percent /= 100;
            int newWidth = Math.round(originalImage.getWidth() * percent);
            int newHeight = Math.round(originalImage.getHeight() * percent);
            int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
            BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, type);
            Graphics2D g = resizedImage.createGraphics();
            g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
            g.dispose();
            return convertToByteArray(resizedImage);
        }
    }

    public byte[] getCroppedImageFromMap(String id, int start, int stop, int width, int height) throws IOException {
        BufferedImage originalImage = imagesMap.getImage(id);
        if(originalImage == null){
            throw new ImageNotFoundException();
        } else {
            return convertToByteArray(originalImage.getSubimage(start, stop, width, height));
        }
    }
}

