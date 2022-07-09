package rs.ac.uns.ftn.siit.isa_mrs.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;
import java.util.LinkedHashSet;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Admin extends User {
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "operator", cascade = CascadeType.ALL)
    private Collection<Response> responses = new LinkedHashSet<>();
}