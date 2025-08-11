package br.com.alg.algexpress.repository.user;

import br.com.alg.algexpress.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    List<User> findByRole(User.UserRole role);
    
    List<User> findByStatus(User.UserStatus status);
    
    @Query("SELECT u FROM User u WHERE u.username = :username OR u.email = :email")
    Optional<User> findByUsernameOrEmail(@Param("username") String username, @Param("email") String email);
    
    @Query("SELECT u FROM User u WHERE u.status = 'ACTIVE' AND (u.accountLockedUntil IS NULL OR u.accountLockedUntil < :now)")
    List<User> findActiveAndUnlockedUsers(@Param("now") LocalDateTime now);
    
    @Query("SELECT u FROM User u WHERE u.role = :role AND u.status = 'ACTIVE'")
    List<User> findActiveUsersByRole(@Param("role") User.UserRole role);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.status = :status")
    Long countByStatus(@Param("status") User.UserStatus status);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role")
    Long countByRole(@Param("role") User.UserRole role);
    
    @Query("SELECT u FROM User u WHERE u.accountLockedUntil IS NOT NULL AND u.accountLockedUntil > :now")
    List<User> findLockedUsers(@Param("now") LocalDateTime now);
    
    @Query("SELECT u FROM User u WHERE u.lastLogin IS NULL OR u.lastLogin < :date")
    List<User> findUsersNotLoggedInSince(@Param("date") LocalDateTime date);
    
    @Query("SELECT u FROM User u WHERE u.loginAttempts >= :maxAttempts")
    List<User> findUsersWithExcessiveLoginAttempts(@Param("maxAttempts") Integer maxAttempts);
    
    @Query("SELECT u.role, COUNT(u) FROM User u WHERE u.status = 'ACTIVE' GROUP BY u.role")
    List<Object[]> findActiveUserRoleStatistics();
    
    @Query("SELECT u FROM User u WHERE u.name LIKE %:name% AND u.status = 'ACTIVE'")
    List<User> findActiveUsersByNameContaining(@Param("name") String name);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    @Query("SELECT u FROM User u WHERE DATE(u.createdAt) = CURRENT_DATE")
    List<User> findUsersCreatedToday();
    
    @Query("SELECT u FROM User u WHERE u.lastLogin BETWEEN :startDate AND :endDate ORDER BY u.lastLogin DESC")
    List<User> findUsersByLastLoginBetween(@Param("startDate") LocalDateTime startDate, 
                                          @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT u FROM User u WHERE u.status = 'PENDING_ACTIVATION' ORDER BY u.createdAt ASC")
    List<User> findPendingActivationUsers();
}