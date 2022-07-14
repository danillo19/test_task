package com.nsu.danilllo.services;

import com.nsu.danilllo.controllers.requests.CategoryRequest;
import com.nsu.danilllo.model.Category;

import java.util.List;

public interface CategoryService {

    /***
     * @param createRequest - body http запроса в виде json'a
     * @return id созданной категории
     */
    public Long createCategory(CategoryRequest createRequest);

    /**
     *
     * @param id - идентификатор удаляемой(soft-delete) категории
     */
    public void deleteCategory(Long id);

    /***
     *
     * @param updateRequest - новые значения для категории
     * @param id - идентификатор категории
     * @return - id обновлённой категории
     */
    public Long updateCategory(CategoryRequest updateRequest, Long id);

    public Category getById(Long id);

    /***
     * Поиск категорий по шаблону имени
     * @param namePart - шаблон поиска имени категории
     * @return - список подходящих категорий
     */
    public List<Category> findByNamePart(String namePart);
}
