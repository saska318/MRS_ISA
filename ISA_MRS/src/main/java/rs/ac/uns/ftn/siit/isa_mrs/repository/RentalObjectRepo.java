package rs.ac.uns.ftn.siit.isa_mrs.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.siit.isa_mrs.model.RentalObject;
import rs.ac.uns.ftn.siit.isa_mrs.model.RentalObjectOwner;
import rs.ac.uns.ftn.siit.isa_mrs.model.enumeration.RentalObjectType;

import java.util.Collection;


public interface RentalObjectRepo extends JpaRepository<RentalObject, Long> {
    Page<RentalObject> findAllByRentalObjectType(RentalObjectType type, Pageable pageable);
    Collection<RentalObject> findAllByRentalObjectOwner(RentalObjectOwner owner);
}
