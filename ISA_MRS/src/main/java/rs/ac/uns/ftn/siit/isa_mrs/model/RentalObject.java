package rs.ac.uns.ftn.siit.isa_mrs.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.uns.ftn.siit.isa_mrs.model.enumeration.ConductType;
import rs.ac.uns.ftn.siit.isa_mrs.model.enumeration.RentalObjectType;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedHashSet;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class RentalObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;
    private RentalObjectType rentalObjectType;
    private String description;
    private Double cancellationFee;
    private LocalDateTime initDate;
    private LocalDateTime termDate;
    @OneToMany(mappedBy = "rentalObject", cascade = CascadeType.ALL)
    private Collection<Photo> photos = new LinkedHashSet<>();
    private Integer capacity;
    private Double price;
    private Boolean isActive=true;
    @OneToMany(mappedBy = "rentalObject", cascade = CascadeType.ALL)
    private Collection<AdditionalService> additionalServices = new LinkedHashSet<>();
    @OneToMany(mappedBy = "rentalObject", cascade =  CascadeType.ALL)
    private Collection<ConductRule> conductRules = new LinkedHashSet<>();
    @OneToMany(mappedBy = "rentalObject", cascade = CascadeType.ALL)
    private Collection<SpecialOffer> specialOffers = new LinkedHashSet<>();
    @OneToMany(mappedBy = "rentalObject", cascade = CascadeType.ALL)
    private Collection<Reservation> reservations = new LinkedHashSet<>();
    @ManyToOne
    private RentalObjectOwner rentalObjectOwner;
    @ManyToOne
    private Address address;
    @ManyToMany
    @JoinTable(name = "subscribed_client",
            joinColumns = @JoinColumn(name = "rental_id"),
            inverseJoinColumns = @JoinColumn(name = "subscriber_id"))
    private Collection<Client> subscribers = new LinkedHashSet<>();

    public boolean hasRule(String rule, ConductType conductType) {
        for (ConductRule conductRule: conductRules) {
            if (conductRule.getRule().equals(rule) && conductRule.getType().equals(conductType)) {
                return true;
            }
        }
        return  false;
    }

    public boolean hasService(String service) {
        for (AdditionalService additionalService: additionalServices) {
            if (additionalService.getName().equals(service)) {
                return true;
            }
        }
        return  false;
    }

    public boolean hasService(String service, double price) {
        for (AdditionalService additionalService: additionalServices) {
            if (additionalService.getName().equals(service) && additionalService.getPrice().equals(price)) {
                return true;
            }
        }
        return  false;
    }

    public void setAdditionalServicePrice(String service, double price) {
        for (AdditionalService additionalService: additionalServices) {
            if (additionalService.getName().equals(service)) {
                additionalService.setPrice(price);
            }
        }
    }
}
