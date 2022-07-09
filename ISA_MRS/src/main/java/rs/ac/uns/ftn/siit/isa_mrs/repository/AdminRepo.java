package rs.ac.uns.ftn.siit.isa_mrs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.siit.isa_mrs.model.Admin;

public interface AdminRepo extends JpaRepository<Admin, Long> {
    Admin getByEmail(String adminEmail);
}
