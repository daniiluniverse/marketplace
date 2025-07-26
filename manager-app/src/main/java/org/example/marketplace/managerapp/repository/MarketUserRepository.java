package org.example.marketplace.managerapp.repository;

import org.example.marketplace.managerapp.entity.MarketUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MarketUserRepository extends JpaRepository<MarketUser, Integer> {

    Optional<MarketUser> findMarketUserByUsername(String username);
}
