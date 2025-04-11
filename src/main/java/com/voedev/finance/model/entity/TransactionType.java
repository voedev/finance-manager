//package com.voedev.finance.model.entity;
//
//import com.voedev.finance.model.enums.TransactionTypeEnum;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Entity
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//@Data
//@Table(name = "transaction_type")
//public class TransactionType implements BaseEntity<Long> {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false)
//    @Enumerated(EnumType.STRING)
//    private TransactionTypeEnum type;
//}
