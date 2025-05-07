package com.voedev.financebackend.repository;

import com.voedev.financebackend.model.entity.Currency;
import com.voedev.financebackend.model.enums.CurrencyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    Optional<Currency> findByValue(CurrencyType value);
}
