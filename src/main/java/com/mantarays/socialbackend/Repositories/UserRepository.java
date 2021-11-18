package com.mantarays.socialbackend.Repositories;

import com.mantarays.socialbackend.Models.User;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
    User findByUsername(String username);

    User findByEmail(String email);

    @Query("SELECT u " +
            "FROM USERS u " +
            "WHERE password_reset_token = ?1" )
    User findByPasswordResetToken(String passwordResetToken);

    List<User> findAllByPotentialFriendsContaining(User user);

    List<User> findAllByPotentialFriendsContains(User user);
}
