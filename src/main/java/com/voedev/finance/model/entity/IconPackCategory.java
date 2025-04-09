package com.voedev.finance.model.entity;

import com.voedev.finance.model.enums.IconPackCategoryEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "icon_pack_category")
public class IconPackCategory implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private IconPackCategoryEnum type;

    @ManyToMany(mappedBy = "categories")
    private Set<IconPack> iconPack;
}
