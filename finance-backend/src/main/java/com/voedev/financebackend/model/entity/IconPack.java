package com.voedev.financebackend.model.entity;//package com.voedev.finance.model.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.util.Set;
//
//@Entity
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//@Data
//@Table(name = "icon_pack")
//public class IconPack implements BaseEntity<Long> {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false, unique = true)
//    private String icon_name;
//
//    @Column(nullable = false, columnDefinition = "TEXT")
//    private String path;
//
//    @ManyToMany
//    @JoinTable(
//            name = "icon_pack_icon_pack_category",
//            joinColumns = @JoinColumn(name = "icon_pack_id"),
//            inverseJoinColumns = @JoinColumn(name = "icon_pack_category_id")
//    )
//    private Set<IconPackCategory> categories;
//}