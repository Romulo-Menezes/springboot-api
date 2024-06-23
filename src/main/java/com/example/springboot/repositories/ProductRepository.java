package com.example.springboot.repositories;

import com.example.springboot.models.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, UUID> {

    @Query(value = """
            WITH OrderedProducts AS (
                    SELECT id_product, row_number() OVER (ORDER BY created_at DESC) AS index
                    FROM tb_products
                )
                SELECT index
                FROM OrderedProducts
                WHERE id_product = :id_product;
            """, nativeQuery = true)
    int getIndexById(@Param("id_product") UUID id_product);
}
