package com.example.java_9.web;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class MyRestController {
    public ImageProcessorController imageProcessorController = new ImageProcessorController();

    @RequestMapping(value = "/image/add", method = RequestMethod.POST)
    public String addImage(HttpServletRequest requestEntity) throws Exception {
        return imageProcessorController.addImageToMap(requestEntity.getInputStream());
    }

    @RequestMapping(value = "/image/get/{id}", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getImage(@PathVariable(value = "id") int id) throws Exception {
        return imageProcessorController.getImageFromMap(id); }

//    @RequestMapping("/hello")
//    public String sayHello(@RequestParam(value = "name") String name){
//        return "Hello " + name + "!";
//    }
}
