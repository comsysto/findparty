package com.comsysto.findparty.web;

import com.comsysto.findparty.Category;
import com.comsysto.findparty.Party;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: tim.hoheisel
 * Date: 06.09.12
 * Time: 08:20
 * To change this template use File | Settings | File Templates.
 */

@Controller
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    public CategoryService categoryService;


    @RequestMapping(value = "/getall", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    Set<Category> getAllCategories() throws Exception {
        Set<Category> categories = categoryService.getAll();
        return categories;
    }
}
