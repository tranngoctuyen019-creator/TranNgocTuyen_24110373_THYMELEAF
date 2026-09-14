package vn.iot.star.controllers;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;
import vn.iot.star.entity.CategoryEntity;
import vn.iot.star.model.CategoryModel;
import vn.iot.star.service.ICategoryService;
import vn.iot.star.utils.FileStorageService;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController {

    private static final String UPLOAD_SUB_DIR = "category";

    private final ICategoryService categoryService;
    private final FileStorageService fileStorageService;

    CategoryController(ICategoryService categoryService, FileStorageService fileStorageService) {
        this.categoryService = categoryService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("")
    public String index() {
        return "redirect:/admin/categories/searchpaginated";
    }

    @GetMapping("/add")
    public String add(ModelMap model) {
        CategoryModel cateModel = new CategoryModel();
        cateModel.setEdit(false);
        model.addAttribute("category", cateModel);
        return "admin/categories/add";
    }

    @PostMapping("/saveOrUpdate")
    public String saveOrUpdate(@Valid @ModelAttribute("category") CategoryModel cateModel,
            BindingResult result, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return cateModel.isEdit() ? "admin/categories/edit" : "admin/categories/add";
        }

        CategoryEntity entity;
        if (cateModel.isEdit() && cateModel.getCategoryId() != null) {
            entity = categoryService.findById(cateModel.getCategoryId()).orElse(new CategoryEntity());
        } else {
            entity = new CategoryEntity();
            entity.setStatus(1);
        }

        entity.setName(cateModel.getName());

        String newImage = fileStorageService.store(cateModel.getIcon(), UPLOAD_SUB_DIR);
        if (newImage != null) entity.setImages(newImage);

        categoryService.save(entity);

        String message = cateModel.isEdit() ? "Cập nhật danh mục thành công!" : "Thêm danh mục thành công!";
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/admin/categories/searchpaginated";
    }

    @GetMapping("/edit/{categoryId}")
    public String edit(ModelMap model, @PathVariable("categoryId") Integer categoryId,
            RedirectAttributes redirectAttributes) {

        Optional<CategoryEntity> opt = categoryService.findById(categoryId);
        if (opt.isPresent()) {
            CategoryEntity entity = opt.get();
            CategoryModel cateModel = new CategoryModel();
            cateModel.setCategoryId(entity.getCategoryId());
            cateModel.setName(entity.getName());
            cateModel.setImages(entity.getImages());
            cateModel.setEdit(true);
            model.addAttribute("category", cateModel);
            return "admin/categories/edit";
        }
        redirectAttributes.addFlashAttribute("message", "Không tìm thấy danh mục cần sửa!");
        return "redirect:/admin/categories/searchpaginated";
    }

    @GetMapping("/delete/{categoryId}")
    public String delete(@PathVariable("categoryId") Integer categoryId, RedirectAttributes redirectAttributes) {
        categoryService.deleteById(categoryId);
        redirectAttributes.addFlashAttribute("message", "Xóa danh mục thành công!");
        return "redirect:/admin/categories/searchpaginated";
    }

    @GetMapping("/searchpaginated")
    public String search(ModelMap model,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "page", required = false) Optional<Integer> page,
            @RequestParam(name = "size", required = false) Optional<Integer> size) {

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("categoryId").descending());

        Page<CategoryEntity> resultPage;
        if (StringUtils.hasText(name)) {
            resultPage = categoryService.findByNameContaining(name, pageable);
        } else {
            resultPage = categoryService.findAll(pageable);
        }
        model.addAttribute("name", name);

        int totalPages = resultPage.getTotalPages();
        if (totalPages > 0) {
            int start = Math.max(1, currentPage - 2);
            int end = Math.min(currentPage + 2, totalPages);
            if (totalPages > 5) {
                if (end == totalPages) start = end - 4;
                else if (start == 1) end = start + 4;
            }
            model.addAttribute("pageNumbers", IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList()));
        }

        model.addAttribute("categoryPage", resultPage);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("currentPage", currentPage);
        return "admin/categories/list";
    }
}