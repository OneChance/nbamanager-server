package com.zhstar.nbamanager.team.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.zhstar.nbamanager.team.entity.ContractLog;

public interface ContractLogRepository extends JpaRepository<ContractLog, Long> {
	List<ContractLog> findByUserId(Long userid,Pageable pageable);
}
