package com.example.springboot.repository;

import com.example.springboot.entity.Products;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Products,Long> {

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update Products p set p.avail=:avail where p.id=:prod_id")
    public void setAvailability(@Param("avail") String avail, @Param("prod_id") Long prod_id);

    Optional<Products> findByProdName(String name);

    List<Products> findByIdIn(List<Long> productIds);

}
