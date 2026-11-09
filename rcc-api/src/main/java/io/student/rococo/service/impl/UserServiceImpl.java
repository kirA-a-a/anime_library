package io.student.rococo.service.impl;

import io.student.rococo.data.entity.UserEntity;
import io.student.rococo.data.repository.UserRepository;
import io.student.rococo.exception.ResourceNotFoundException;
import io.student.rococo.model.UserJson;
import io.student.rococo.service.api.UserService;
import io.student.rococo.util.StringAsBytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  @Autowired
  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public UserJson getCurrentUser(String username) {
    return UserJson.fromEntity(
        userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format("Пользователь не найден: %s", username)
            ))
    );
  }

  @Override
  @Transactional
  public UserJson update(UserJson user) {
    UserEntity entity = userRepository.findByUsername(user.username())
        .orElseThrow(() -> new ResourceNotFoundException(
            String.format("Пользователь не найден: %s", user.username())
        ));
    entity.setFirstname(user.firstname());
    entity.setLastname(user.lastname());
    entity.setAvatar(new StringAsBytes(user.avatar()).bytes());
    return UserJson.fromEntity(userRepository.save(entity));
  }

  @Override
  @Transactional
  public UserJson createNewUserIfNotPresent(String username) {
    return userRepository.findByUsername(username)
        .map(UserJson::fromEntity)
        .orElseGet(() -> {
          UserEntity entity = new UserEntity();
          entity.setUsername(username);
          return UserJson.fromEntity(userRepository.save(entity));
        });
  }
}
