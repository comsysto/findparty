package com.comsysto.findparty.web;

import com.comsysto.findparty.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: tim.hoheisel
 * Date: 06.09.12
 * Time: 09:43
 * To change this template use File | Settings | File Templates.
 */
@Component
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    public MongoOperations mongoOperations;


    @Override
    public Set<Category> getAllCategories() {
        Set<Category> categories = new HashSet<Category>();
        categories.addAll(mongoOperations.findAll(Category.class));
        return categories;
    }


}
