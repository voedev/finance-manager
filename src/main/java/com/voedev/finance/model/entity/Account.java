//package com.voedev.finance.model.entity;
//
//import com.voedev.finance.model.enums.AccountStatus;
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
//@Table(name = "account")
//public class Account implements BaseEntity<Long> {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false)
//    private String name;
//
//    @Column(name = "balance", precision = 15, scale = 2, columnDefinition = "numeric(15,2) default '0.00'")
//    private BigDecimal balance;
//
//    @ManyToOne(optional = false)
//    @JoinColumn(name = "user_id")
//    private User user;
//
//    @ManyToOne
//    @JoinColumn(name = "icon_pack_id")
//    private IconPack iconPack;
//
//    @Enumerated(EnumType.STRING)
//    @Column(name = "status")
//    private AccountStatus status = AccountStatus.ACTIVE;
//
//    @CreationTimestamp
//    @Temporal(TemporalType.TIMESTAMP)
//    @Column(name = "created_at", updatable = false)
//    private LocalDateTime createdAt;
//
//    @UpdateTimestamp
//    @Temporal(TemporalType.TIMESTAMP)
//    @Column(name = "updated_at")
//    private LocalDateTime updatedAt;
//}