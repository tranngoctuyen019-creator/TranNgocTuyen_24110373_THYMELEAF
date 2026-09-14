package vn.iot.star.model;

import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryModel {
    private Integer categoryId;

    @NotBlank(message = "Tên danh mục không được để trống!")
    @Size(max = 200, message = "Tên danh mục tối đa 200 ký tự!")
    private String name;

    private String images;
    private MultipartFile icon;
    private boolean edit;
}