package com.comsysto.findparty.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * User: tim.hoheisel
 * Date: 27.11.13
 * Time: 14:35
 * To change this template use File | Settings | File Templates.
 */
public interface PictureController {

    byte[] showPicture(Double id);

}
