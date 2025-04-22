package com.voedev.financebackend.model.entity;//package com.voedev.finance.model.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.hibernate.annotations.CreationTimestamp;
//import org.hibernate.annotations.UpdateTimestamp;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//
//@Entity
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//@Data
//@Table(name = "transaction")
//public class Transaction implements BaseEntity<Long> {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "account_id", nullable = false)
//    private Account account;
//
//    @ManyToOne
//    @JoinColumn(name = "user_category_id", nullable = false)
//    private UserCategory category;
//
//    @Column(name = "amount", precision = 15, scale = 2, columnDefinition = "numeric(15,2)", nullable = false)
//    private BigDecimal balance;
//
//    @Column(columnDefinition = "TEXT")
//    private String description;
//
//    @Column(name = "created_at", updatable = false)
//    @CreationTimestamp
//    private LocalDateTime createdAt;
//
//    @Column(name = "updated_at")
//    @UpdateTimestamp
//    private LocalDateTime updatedAt;
//}