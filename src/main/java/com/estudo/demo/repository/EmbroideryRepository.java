package com.estudo.demo.repository;

import com.estudo.demo.model.Enbroidery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmbroideryRepository extends JpaRepository<Enbroidery, Long> {
}
