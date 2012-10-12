package com.comsysto.findparty.web;

import com.comsysto.findparty.Category;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: tim.hoheisel
 * Date: 06.09.12
 * Time: 08:22
 * To change this template use File | Settings | File Templates.
 */
public interface CategoryService {

    public Set<Category> getAllCategories();
}
