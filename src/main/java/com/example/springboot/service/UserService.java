package com.example.springboot.service;

import com.example.springboot.entity.User;
import com.example.springboot.repository.UserRepository;
import com.example.springboot.request.SmsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private static final int RANDOM_TOKEN_LENGTH = 6;

    private com.example.springboot.service.Service service;
    @Autowired
    public UserService(UserRepository userRepository,com.example.springboot.service.Service service ) {
        this.userRepository = userRepository;
        this.service = service;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User saveUser(User user) {
        // Generate a random reset token  new code
        String resetToken = generateRandomString(RANDOM_TOKEN_LENGTH);
        user.setResetToken(resetToken); // Store the random token in resetToken field new code

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    private String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }

    public void forgotPassword(String email){
        Optional<User> user=userRepository.findByEmail(email);
        String token = user.get().getResetToken();
        String msg="This is your Rest Token: "+token+" ";
        String phone = user.get().getPhone();
        SmsRequest req=new SmsRequest(phone,msg);
        service.sendSms(req);
    }
    public User findUserByResetToken(String resetToken) {
        return userRepository.findByResetToken(resetToken);
    }

    public void updatePasswordWithResetToken(String resetToken, String newPassword) {
        User user = findUserByResetToken(resetToken);

        if (user != null) {
            user.setPassword(passwordEncoder.encode(newPassword));
            resetToken = generateRandomString(RANDOM_TOKEN_LENGTH);
            user.setResetToken(resetToken);
            userRepository.save(user);

        }
    }

    public User updateUser(Long id, User updates) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User existingUser = user.get();
            if (updates.getFirstName() != null) {
                existingUser.setFirstName(updates.getFirstName());
            }
            if (updates.getLastName() != null) {
                existingUser.setLastName(updates.getLastName());
            }
            if (updates.getEmail() != null) {
                existingUser.setEmail(updates.getEmail());
            }
            if (updates.getPhone() != null) {
                existingUser.setPhone(updates.getPhone());
            }
            return userRepository.save(existingUser);
        }
        else {
            return null;
        }
    }
}

