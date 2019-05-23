package com.example.java_9.web;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

@RestController
public class MyRestController {
    public ImageProcessorController imageProcessorController = new ImageProcessorController();

    @RequestMapping(value = "/files", method = RequestMethod.GET)
    public String getFiles() throws Exception {
        return imageProcessorController.getUploadedFiles();
    }

    @RequestMapping(value = "/image", method = RequestMethod.POST)
    public String addImage(HttpServletRequest requestEntity) throws Exception {
        return imageProcessorController.addImageToMap(requestEntity.getInputStream());
    }

    @RequestMapping(value = "/image/{id}", method = RequestMethod.DELETE)
    public String deleteImage(@PathVariable(value = "id") String id) throws ImageNotFoundException {
        return imageProcessorController.deleteImageFromMap(id);
    }

    @RequestMapping(value = "/image/get/{id}", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getImage(@PathVariable(value = "id") String id) throws Exception {
        return imageProcessorController.getImageFromMap(id);
    }

    @RequestMapping(value = "/image/{id}/size", method = RequestMethod.GET)
    public String getImageSize(@PathVariable(value = "id") String id) {
        return imageProcessorController.getImageSizeJson(id);
    }

    @RequestMapping(value = "/image/{id}/grayscale", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getGrayscaleImage(@PathVariable(value = "id") String id) throws Exception {
        return imageProcessorController.getGrayscaleImageFromMap(id);
    }

    @RequestMapping(value = "/image/{id}/scale/{percent}", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getGrayscaleImage(@PathVariable(value = "id") String id, @PathVariable(value = "percent") float percent) throws Exception {
        return imageProcessorController.getScaledImageFromMap(id, percent);
    }

    @RequestMapping(value = "/image/{id}/crop/{start}/{stop}/{width}/{height}", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getCroppedImage(@PathVariable(value = "id") String id, @PathVariable(value = "start") int start, @PathVariable(value = "stop") int stop, @PathVariable(value = "width") int width, @PathVariable(value = "height") int height) throws Exception {
        return imageProcessorController.getCroppedImageFromMap(id, start, stop, width, height);
    }

    @RequestMapping(value = "/image/{id}/histogram", method = RequestMethod.GET)
    public String getImageHstogram(@PathVariable(value = "id") String id) throws Exception {
        return imageProcessorController.getImageHistogramFromMap(id);
    }

    @RequestMapping(value = "/image/{id}/blur/{radius}", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getBlurredImage(@PathVariable(value = "id") String id, @PathVariable(value = "radius") int radius) throws Exception {
        return imageProcessorController.getBlurredImageFromMap(id, radius);
    }
}
