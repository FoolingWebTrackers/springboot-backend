package com.jester.backendserver.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jester.backendserver.model.Marketplace;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MarketplaceRepository extends JpaRepository<Marketplace, Long> {

    @Transactional
    @Modifying
    @Query(value = "CALL sell_persona(:uname, :pname)", nativeQuery = true)
    void sellPersona(@Param("uname") String username, @Param("pname") String personaName);

    @Transactional
    @Modifying
    @Query(value = "CALL buy_persona(:uname, :pname)", nativeQuery = true)
    void buyPersona(@Param("uname") String username, @Param("pname") String personaName);

    @Transactional
    @Modifying
    @Query(value = "CALL remove_from_marketplace(:uname, :pname)", nativeQuery = true)
    void removeFromMarketplace(@Param("uname") String username, @Param("pname") String personaName);

    @Query(value = "SELECT * FROM get_marketplace(:uname)", nativeQuery = true)
    List<Marketplace> getMarketplace(@Param("uname") String username);

    @Query(value = "SELECT * FROM get_seller_personas(:uname)", nativeQuery = true)
    List<Marketplace> getSellerPersonas(@Param("uname") String username);
}

