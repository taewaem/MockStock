package Capstone.Third.user.service;

import Capstone.Third.user.entity.User;
import Capstone.Third.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * 회원가입
     *
     * @param user
     * @return
     */
    @Transactional
    public Long join(User user) {
        validateDuplicateUser(user);
        userRepository.save(user);
        return user.getId();
    }


    /**
     * 중복 회원 검사
     *
     * @param user
     */
    private void validateDuplicateUser(User user) {
        userRepository.findByLoginId(user.getLoginId())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 아이디입니다.");
                });
    }

    public List<User> findAll(Long id) {

        return userRepository.findAll();

    }

    /**
     * 회원 단건 조회
     */
    public Optional<User> findOne(Long id) {
        return userRepository.findOne(id);
    }

}
