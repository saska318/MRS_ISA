package rs.ac.uns.ftn.siit.isa_mrs.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.siit.isa_mrs.model.RentalObjectOwner;
import rs.ac.uns.ftn.siit.isa_mrs.model.enumeration.UserType;


import java.util.Collection;
import java.util.Optional;

public interface RentalObjectOwnerRepo extends JpaRepository<RentalObjectOwner, Long> {
    Optional<RentalObjectOwner> findByEmail(String email);
    Page<RentalObjectOwner> findAllByUserType(UserType type, Pageable pageable);
    Collection<RentalObjectOwner> findAllByIsActive(Boolean isActive);
}
