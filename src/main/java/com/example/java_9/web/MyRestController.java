package com.example.java_9.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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
    public void deleteImage(@PathVariable(value = "id") String id) throws ImageNotFoundException {
        imageProcessorController.deleteImageFromMap(id);
    }

    @RequestMapping(value = "/image/get/{id}", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getImage(@PathVariable(value = "id") String id) throws Exception {
        return imageProcessorController.getImageFromMap(id);
    }

    @RequestMapping(value = "/image/{id}/size", method = RequestMethod.GET)
    public String getImageSize(@PathVariable(value = "id") String id) throws Exception {
        return imageProcessorController.getImageSizeJson(id);
    }


//    @RequestMapping("/hello")
//    public String sayHello(@RequestParam(value = "name") String name){
//        return "Hello " + name + "!";
//    }
}
