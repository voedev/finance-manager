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
//@Table(name = "preset_category")
//public class PresetCategory implements BaseEntity<Long> {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "transaction_type_id")
//    private TransactionType transactionType;
//
//    @Column(name = "created_at", updatable = false)
//    @CreationTimestamp
//    private LocalDateTime createdAt;
//
//    @Column(name = "updated_at")
//    @UpdateTimestamp
//    private LocalDateTime updatedAt;
//}
