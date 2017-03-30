package com.zhstar.nbamanager.team.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.zhstar.nbamanager.team.entity.ContractLog;

public interface ContractLogRepository extends JpaRepository<ContractLog, Long> {
	
	@Query("select c from ContractLog c where c.userId = ?1 and playerName like %?2% order by c.id desc")
	List<ContractLog> findByUserId(Long userid,String searchName,Pageable pageable);
}
