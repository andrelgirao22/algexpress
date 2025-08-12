package br.com.alg.algexpress.infra.service;

import br.com.alg.algexpress.domain.user.User;
import br.com.alg.algexpress.infra.repository.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findUsersCreatedToday() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().plusDays(1).atStartOfDay();
        return userRepository.findUsersCreatedBetween(startOfDay, endOfDay);
    }

    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public List<User> findByRole(User.UserRole role) {
        return userRepository.findByRole(role);
    }

    @Transactional(readOnly = true)
    public List<User> findActiveUsersByRole(User.UserRole role) {
        return userRepository.findActiveUsersByRole(role);
    }

    @Transactional(readOnly = true)
    public List<User> findActiveAndUnlockedUsers() {
        return userRepository.findActiveAndUnlockedUsers(LocalDateTime.now());
    }

    public User save(User user) {
        if (user.getId() == null) {
            user.setCreatedAt(LocalDateTime.now());
        }
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public void lockUser(Long userId, LocalDateTime lockUntil) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setAccountLockedUntil(lockUntil);
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
        }
    }

    public void unlockUser(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setAccountLockedUntil(null);
            user.setLoginAttempts(0);
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
        }
    }

    public void incrementLoginAttempts(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setLoginAttempts(user.getLoginAttempts() + 1);
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
        }
    }

    public void updateLastLogin(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setLastLogin(LocalDateTime.now());
            user.setLoginAttempts(0);
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
        }
    }

    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public Long countByStatus(User.UserStatus status) {
        return userRepository.countByStatus(status);
    }

    @Transactional(readOnly = true)
    public Long countByRole(User.UserRole role) {
        return userRepository.countByRole(role);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}