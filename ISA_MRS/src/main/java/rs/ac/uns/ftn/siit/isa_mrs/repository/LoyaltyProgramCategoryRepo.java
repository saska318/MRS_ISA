package rs.ac.uns.ftn.siit.isa_mrs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.siit.isa_mrs.model.LoyaltyCategory;

public interface LoyaltyProgramCategoryRepo extends JpaRepository<LoyaltyCategory, Long>  {
}
