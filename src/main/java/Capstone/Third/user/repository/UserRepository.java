package Capstone.Third.user.repository;

import Capstone.Third.user.entity.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    //    @PersistenceContext
//    @Autowired        최신 JPA 에서는 @PersistenceContext 대신 @Autowired 해도 지원해 준다.
    private final EntityManager em;

    public void save(User user) {
        em.persist(user);
    }

    public Optional<User> findOne(Long id) {
        User user = em.find(User.class, id);

        return Optional.ofNullable(user);
    }

    public List<User> findAll() {
        return em.createQuery("select u from User u", User.class)
                .getResultList();
    }

    public Optional<User> findByLoginId(String loginId) {
        List<User> result = em.createQuery("select u from User u where u.loginId = :loginId", User.class)
                .setParameter("loginId", loginId)
                .getResultList();
        return result.stream().findAny();
    }


}
