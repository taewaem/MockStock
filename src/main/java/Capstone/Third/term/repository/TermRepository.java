package Capstone.Third.term.repository;

import Capstone.Third.term.entity.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TermRepository extends JpaRepository<Term, String> {
}
