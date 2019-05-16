package com.example.java_9.web;

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
        return uuid;
    }

    public byte[] getImageFromMap(int number) throws IOException {
        List<String> ids = new ArrayList<>(imagesMap.getIdArray());
        BufferedImage temp = imagesMap.getImage(ids.get(number));
        return convertToByteArray(temp);
    }

    public byte[] convertToByteArray(BufferedImage image) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", bos);
        return bos.toByteArray();
    }

//    public BufferedImage convert(byte[] byteImage){
//        InputStream in = new ByteArrayInputStream(byteImage);
//        BufferedImage bImageFromConvert = ImageIO.read(in);
//        return bImageFromConvert;
//    }
}

