package vn.iot.star.entity;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categories")
public class CategoryEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;

    @Column(name = "categoryname", columnDefinition = "NVARCHAR(255) NULL")
    private String name;

    @Column(name = "images", columnDefinition = "NVARCHAR(255) NULL")
    private String images;

    @Column(name = "status")
    private int status;
}