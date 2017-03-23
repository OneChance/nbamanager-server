package com.zhstar.nbamanager.account.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.zhstar.nbamanager.account.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
	@Query("select a from Account a where a.name=:name")
	Account getByIdentity(@Param("name")String name);
	Account getByName(String name);
}
