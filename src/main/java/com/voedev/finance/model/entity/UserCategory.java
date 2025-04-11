//package com.voedev.finance.model.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.hibernate.annotations.CreationTimestamp;
//import org.hibernate.annotations.UpdateTimestamp;
//
//import java.time.LocalDateTime;
//
//@Entity
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//@Data
//@Table(name = "user_category")
//public class UserCategory implements BaseEntity<Long> {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;
//
//    @ManyToOne
//    @JoinColumn(name = "icon_pack_id")
//    private IconPack iconPack;
//
//    @Column(nullable = false)
//    private String name;
//
//    @ManyToOne
//    @JoinColumn(name = "transaction_type_id")
//    private TransactionType transactionType;
//
//    @ManyToOne
//    @JoinColumn(name = "preset_category_id")
//    private PresetCategory presetCategory;
//
//    @Column(name = "created_at", updatable = false)
//    @CreationTimestamp
//    private LocalDateTime createdAt;
//
//    @Column(name = "updated_at")
//    @UpdateTimestamp
//    private LocalDateTime updatedAt;
//}
