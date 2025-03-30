package com.charge.autorization.internalas.repositories;

import com.charge.autorization.internalas.model.CallBackResponseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CallBackResponseRepository extends JpaRepository<CallBackResponseEntity, Long> {
}
