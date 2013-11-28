package com.comsysto.findparty.web;

import com.comsysto.findparty.Picture;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    @RequestMapping(value = "/{pictureId}", method = RequestMethod.GET, produces = "image/jpg")
    @ResponseStatus(HttpStatus.OK)
    public
    @ResponseBody
    byte[] showPicture(@PathVariable("pictureId") String pictureId) {
        return getBytes(pictureId);
    }

    private byte[] getBytes(String pictureId) {
        Picture picture = pictureService.getPicture(String.valueOf(pictureId));
        if (picture != null) {
            return picture.getContent();
        }
        return null;
    }


}
