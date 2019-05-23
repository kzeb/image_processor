package com.example.java_9.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.imageio.ImageIO;
import javax.servlet.ServletInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;

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
        if(percent<0){
            throw new ScaleException();
        }
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
        if(start<0||stop<0||width<0||height<0){
            throw new ScaleException();
        }
        BufferedImage originalImage = imagesMap.getImage(id);
        if(originalImage == null){
            throw new ImageNotFoundException();
        } else {
            return convertToByteArray(originalImage.getSubimage(start, stop, width, height));
        }
    }

    public byte[] getBlurredImageFromMap(String id, int radiuss) throws Exception {
        if(radiuss<0){
            throw new ScaleException();
        }
        BufferedImage image = imagesMap.getImage(id);

        if(image == null){
            throw new ImageNotFoundException();
        } else {
            int radius = radiuss;
            int size = radius * 2 + 1;
            float weight = 1.0f / (size * size);
            float[] data = new float[size * size];

            for (int i = 0; i < data.length; i++) {
                data[i] = weight;
            }
            Kernel kernel = new Kernel(size, size, data);
            int kernelWidth = size;
            int kernelHeight = size;

            int xOffset = (kernelWidth - 1) / 2;
            int yOffset = (kernelHeight - 1) / 2;

            BufferedImage newSource = new BufferedImage(
                    image.getWidth() + kernelWidth - 1,
                    image.getHeight() + kernelHeight - 1,
                    image.getType());
            Graphics2D g2 = newSource.createGraphics();
            g2.drawImage(image, xOffset, yOffset, null);
            g2.dispose();
            ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
            BufferedImage i = op.filter(newSource, null);
            return convertToByteArray(i);
        }
    }

    public String getImageHistogramFromMap(String id) throws IOException {
        BufferedImage image = imagesMap.getImage(id);
        if(image == null){
            throw new ImageNotFoundException();
        } else {
            ObjectMapper obj = new ObjectMapper();
            return obj.writeValueAsString(histogramEqualizationLUT(image));
        }
    }

    public static ArrayList<int[]> imageHistogram(BufferedImage input) {
        int[] rhistogram = new int[256];
        int[] ghistogram = new int[256];
        int[] bhistogram = new int[256];

        for(int i=0; i<rhistogram.length; i++) rhistogram[i] = 0;
        for(int i=0; i<ghistogram.length; i++) ghistogram[i] = 0;
        for(int i=0; i<bhistogram.length; i++) bhistogram[i] = 0;

        for(int i=0; i<input.getWidth(); i++) {
            for(int j=0; j<input.getHeight(); j++) {
                int red = new Color(input.getRGB (i, j)).getRed();
                int green = new Color(input.getRGB (i, j)).getGreen();
                int blue = new Color(input.getRGB (i, j)).getBlue();

                rhistogram[red]++; ghistogram[green]++; bhistogram[blue]++;
            }
        }

        ArrayList<int[]> hist = new ArrayList<int[]>();
        hist.add(rhistogram);
        hist.add(ghistogram);
        hist.add(bhistogram);

        return hist;
    }

    private static Histogram histogramEqualizationLUT(BufferedImage input) {
        ArrayList<int[]> imageHist = imageHistogram(input);
        ArrayList<int[]> imageLUT = new ArrayList<int[]>();

        int[] rhistogram = new int[256];
        int[] ghistogram = new int[256];
        int[] bhistogram = new int[256];

        for(int i=0; i<rhistogram.length; i++) rhistogram[i] = 0;
        for(int i=0; i<ghistogram.length; i++) ghistogram[i] = 0;
        for(int i=0; i<bhistogram.length; i++) bhistogram[i] = 0;

        long sumr = 0;
        long sumg = 0;
        long sumb = 0;

        float scale_factor = (float) (255.0 / (input.getWidth() * input.getHeight()));

        for(int i=0; i<rhistogram.length; i++) {
            sumr += imageHist.get(0)[i];
            int valr = (int) (sumr * scale_factor);
            if(valr > 255) {
                rhistogram[i] = 255;
            }
            else rhistogram[i] = valr;

            sumg += imageHist.get(1)[i];
            int valg = (int) (sumg * scale_factor);
            if(valg > 255) {
                ghistogram[i] = 255;
            }
            else ghistogram[i] = valg;

            sumb += imageHist.get(2)[i];
            int valb = (int) (sumb * scale_factor);
            if(valb > 255) {
                bhistogram[i] = 255;
            }
            else bhistogram[i] = valb;
        }


        Histogram hist = new Histogram();


        normalize(rhistogram, hist.getRhist());
        normalize(ghistogram, hist.getGhist());
        normalize(bhistogram, hist.getBhist());


        imageLUT.add(rhistogram);
        imageLUT.add(ghistogram);
        imageLUT.add(bhistogram);

        return hist;
    }

    public static void normalize(int[] rhistogram,  Map<String, String> rhist){
        int js = 0;
        int ks = 0;
        int maxs = 0;
        for (int is = 0; is<rhistogram.length; is++){
            if(rhistogram[is] == js){
                ks++;
            }else{
                if(ks>maxs){
                    maxs = ks;
                }
                ks=0;
                is--;
                js++;
            }
        }


        int j = 0;
        double k = 0;
//        int max = 0;
        for (int i = 0; i<rhistogram.length; i++){
            if(rhistogram[i] == j){
                k++;
            }else{
                if(k>0) {
                    rhist.put(Integer.toString(j), Double.toString(k/maxs));
                }
//                if(k>max){
//                    max = k;
//                }
                k=0;
                i--;
                j++;
            }
        }
    }
}


