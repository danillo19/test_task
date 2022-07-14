package com.nsu.danilllo.services.impls;

import com.nsu.danilllo.repositories.BannerRepository;
import com.nsu.danilllo.controllers.requests.CategoryRequest;
import com.nsu.danilllo.exceptions.CategoryIsLinkedException;
import com.nsu.danilllo.exceptions.NoEntityException;
import com.nsu.danilllo.model.Category;
import com.nsu.danilllo.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CategoryServiceImpl {
    private final CategoryRepository categoryRepository;
    private final BannerRepository bannerRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, BannerRepository bannerRepository) {
        this.categoryRepository = categoryRepository;
        this.bannerRepository = bannerRepository;
    }

    public Long createCategory(CategoryRequest createRequest) throws EntityExistsException {
        if (categoryRepository.findCategoryByRequestedIdOrName(
                createRequest.getRequestedID(), createRequest.getName()).isPresent()) {
            throw new EntityExistsException("Category already exists\n");
        }

        Category category = new Category();
        category.setName(createRequest.getName());
        category.setRequestedID(createRequest.getRequestedID());
        categoryRepository.save(category);

        return category.getId();
    }

    public void deleteCategory(Long id) throws NoEntityException, CategoryIsLinkedException {
        Category categoryFromDB = categoryRepository.findById(id).orElseThrow(() -> new NoEntityException("No such category"));
        if (!bannerRepository.findByCategoriesContainingAndDeletedFalse(categoryFromDB).isEmpty()) {
            throw new CategoryIsLinkedException("Can't delete category");
        }
        categoryFromDB.setDeleted(true);
        categoryRepository.save(categoryFromDB);
    }


    public Long updateCategory(CategoryRequest updateRequest, Long id) throws NoEntityException, EntityExistsException {
        Category updating = categoryRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new NoEntityException("No such category"));

        if (isCategoryWithSuchNameOrRequestIdExists(updating, updateRequest.getName(), updateRequest.getRequestedID())) {
            throw new EntityExistsException("Category already exists");
        }

        updating.setName(updateRequest.getName());
        updating.setRequestedID(updateRequest.getRequestedID());
        categoryRepository.save(updating);

        return updating.getId();
    }

    public Category getById(Long id) throws NoSuchElementException {
        return categoryRepository.findById(id).orElseThrow(() -> new NoSuchElementException("No category with such id"));
    }

    public List<Category> findByNamePart(String namePart) {
        return categoryRepository.findCategoriesByName(namePart);
    }

    private boolean isCategoryWithSuchNameOrRequestIdExists(Category category, String name, String requestId) {
        return category != categoryRepository.findCategoryByRequestedIdOrName(requestId, name).orElse(category);
    }
}
