package com.mpc.merchant.repository;

import com.mpc.merchant.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchanRepository extends JpaRepository<Merchant, Integer> {
}
