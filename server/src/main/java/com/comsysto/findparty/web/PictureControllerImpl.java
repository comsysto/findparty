package com.comsysto.findparty.web;

import com.comsysto.findparty.Picture;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: stefandjurasic
 * Date: 30.03.13
 * Time: 11:36
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/pictures")
public class PictureControllerImpl implements PictureController {

    public static final Logger LOGGER = Logger.getLogger(PictureController.class);

    @Autowired
    private PictureService pictureService;

    @Override
    @RequestMapping(value = "/{pictureId}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public
    @ResponseBody
    byte[] showPicture(@PathVariable("pictureId") String pictureId, @RequestParam(value="sz", required = false) String size) {
        return getBytes(pictureId, size);
    }

    @Override
    @RequestMapping(value = "/{username}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String createPartyImage(@RequestBody byte[] content, @PathVariable String username) {
        return pictureService.createPartyImage(username, content);
    }

    private byte[] getBytes(String pictureId, String size) {
       Picture picture = pictureService.getPicture(String.valueOf(pictureId));
       if (picture != null) {
           if(size != null){
               return getBytesOfResizedImage(size, picture);
           }else{
               return picture.getContent();
            }
        }
        return null;
    }

    private byte[] getBytesOfResizedImage(String size, Picture picture) {
        try {
            ByteArrayInputStream input = new ByteArrayInputStream(picture.getContent());
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            BufferedImage originalImage = ImageIO.read(input);

            Image resizedImage = resizeBitmap(originalImage, Integer.valueOf(size));
            BufferedImage newBufferedImage = getAsBufferedImage(resizedImage, originalImage.getType());

            ImageIO.write(newBufferedImage, "PNG", output);

            return output.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("The image with the id " + picture.getId() + " + could not be loaded and resized.", e);
        }
    }

    private BufferedImage getAsBufferedImage(Image newImg, Integer type) {
        BufferedImage newBufferedImage = new BufferedImage(newImg.getWidth(null), newImg.getHeight(null), type);
        newBufferedImage.getGraphics().drawImage(newImg, 0, 0, null);
        return newBufferedImage;
    }

    private Image resizeBitmap(BufferedImage image, Integer size) {
        int width = image.getWidth();
        int height = image.getHeight();

        int newWidth = 0;
        int newHeight = 0;
        if (height < width) {
            newWidth = size;
            newHeight = (height * size /width);
        }
        else {
            newHeight = size;
            newWidth = (width * size /height);

        }
        return image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
    }


}
