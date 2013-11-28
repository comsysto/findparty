package com.comsysto.findparty.web;

import com.comsysto.findparty.Picture;

/**
 * Created with IntelliJ IDEA.
 * User: stefandjurasic
 * Date: 28.04.13
 * Time: 11:33
 * To change this template use File | Settings | File Templates.
 */
public interface PictureService {


    Picture getPicture(String id);

    String createPartyImage(String username, byte[] content);

    void removePicture(String pictureId);
}
