package vn.iot.star.service.impl;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.iot.star.entity.CategoryEntity;
import vn.iot.star.repository.CategoryRepository;
import vn.iot.star.service.ICategoryService;

@Service
public class CategoryServiceImpl implements ICategoryService {

    private final CategoryRepository categoryRepository;

    CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryEntity save(CategoryEntity entity) { return categoryRepository.save(entity); }

    @Override
    public List<CategoryEntity> findAll() { return categoryRepository.findAll(); }

    @Override
    public Page<CategoryEntity> findAll(Pageable pageable) { return categoryRepository.findAll(pageable); }

    @Override
    public Optional<CategoryEntity> findById(Integer id) { return categoryRepository.findById(id); }

    @Override
    public void deleteById(Integer id) { categoryRepository.deleteById(id); }

    @Override
    public List<CategoryEntity> findByNameContaining(String name) { return categoryRepository.findByNameContaining(name); }

    @Override
    public Page<CategoryEntity> findByNameContaining(String name, Pageable pageable) { return categoryRepository.findByNameContaining(name, pageable); }
}