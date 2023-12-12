package com.shop.ua.repositories;

import com.shop.ua.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByEmailVerificationToken(String emailVerificationToken);
}
