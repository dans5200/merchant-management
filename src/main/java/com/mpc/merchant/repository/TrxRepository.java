package com.mpc.merchant.repository;

import com.mpc.merchant.model.TrxLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrxRepository extends JpaRepository<TrxLog, Integer> {

}
