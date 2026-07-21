package org.erenaltun.countriesbe.repository;

import org.erenaltun.countriesbe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    
    Optional<User>findByUserName(String UserName);

    Boolean existsByUserName(String userName);

    Boolean existsByEmail(String email);


}
