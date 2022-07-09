package rs.ac.uns.ftn.siit.isa_mrs.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.siit.isa_mrs.dto.BackToFrontDto.ClientDtos.ClientPenaltyDto;
import rs.ac.uns.ftn.siit.isa_mrs.dto.BackToFrontDto.ClientDtos.ClientProfileDto;
import rs.ac.uns.ftn.siit.isa_mrs.dto.BackToFrontDto.ClientDtos.ClientReservationDtos.ClientReservationDto;
import rs.ac.uns.ftn.siit.isa_mrs.dto.BackToFrontDto.ReservationDtos.ReservationRentalObjectDto;
import rs.ac.uns.ftn.siit.isa_mrs.dto.BackToFrontDto.ReservationDtos.ReservationReportDto;
import rs.ac.uns.ftn.siit.isa_mrs.dto.PhotoDto;
import rs.ac.uns.ftn.siit.isa_mrs.model.Client;
import rs.ac.uns.ftn.siit.isa_mrs.model.Photo;
import rs.ac.uns.ftn.siit.isa_mrs.model.RentalObject;
import rs.ac.uns.ftn.siit.isa_mrs.model.Reservation;
import rs.ac.uns.ftn.siit.isa_mrs.model.enumeration.RequestStatus;
import rs.ac.uns.ftn.siit.isa_mrs.repository.ClientRepo;
import rs.ac.uns.ftn.siit.isa_mrs.repository.PhotoRepo;
import rs.ac.uns.ftn.siit.isa_mrs.repository.ReservationRepo;
import rs.ac.uns.ftn.siit.isa_mrs.security.JwtDecoder;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ClientServiceImpl implements ClientService {

    private final JwtDecoder jwtDecoder;
    private final ModelMapper modelMapper;
    private final ClientRepo clientRepo;
    private final ReservationRepo reservationRepo;
    private final PhotoRepo photoRepo;

    @Override
    public ResponseEntity<ClientProfileDto> getClient(String token) {
        JwtDecoder.DecodedToken decodedToken;
        try {
            decodedToken = jwtDecoder.decodeToken(token);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Optional<Client> optionalClient = clientRepo.findByEmail(decodedToken.getEmail());
        if(optionalClient.isPresent()) {
            Client client = optionalClient.get();
            ClientProfileDto clientProfileDto = modelMapper.map(client, ClientProfileDto.class);
            clientProfileDto.setReservations(setUpReservationDtos(client.getId()));
            clientProfileDto.setPenalties(setUpPenalties(client));
            return new ResponseEntity<>(clientProfileDto, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @Override
    public long countClients() {
        return clientRepo.findAllByIsActive(true).size();
    }

    private Collection<ClientReservationDto> setUpReservationDtos(Long clientId) {
        Collection<Reservation> reservations = reservationRepo.findAllByClientId(clientId);
        Collection<ClientReservationDto> reservationDtos = new ArrayList<>();
        for(var reservation : reservations) {
            if(reservation.getCancelled()) continue;
            ClientReservationDto reservationDto = modelMapper.map(reservation, ClientReservationDto.class);
            reservationDto.setReports(setUpReservationReports(reservation));
            reservationDto.setRentalObject(setUpReservationRentalObjectDto(reservation.getRentalObject()));
            reservationDtos.add(reservationDto);
        }
        return reservationDtos;
    }

    private Collection<ReservationReportDto> setUpReservationReports(Reservation reservation) {
        Collection<ReservationReportDto> reports = new ArrayList<>();
        for(var report : reservation.getReports()) {
            ReservationReportDto r = modelMapper.map(report, ReservationReportDto.class);
            r.setUser(report.getAuthor().getUserType());
            reports.add(r);
        }
        return reports;
    }

    private ReservationRentalObjectDto setUpReservationRentalObjectDto(RentalObject rental) {
        ReservationRentalObjectDto rentalDto = modelMapper.map(rental, ReservationRentalObjectDto.class);
        if(rental.getPhotos().size() != 0) {
            Optional<Photo> photo = rental.getPhotos().stream().findFirst();
            if(photo.isPresent()) {
                PhotoDto photoDto = modelMapper.map(photo, PhotoDto.class);
                rentalDto.setDisplayPhoto(photoDto);
            }
        }
        return rentalDto;
    }

    public Collection<ClientPenaltyDto> setUpPenalties(Client c) {
        Collection<ClientPenaltyDto> penalties = new ArrayList<>();
        for(var reservation : c.getReservations()) {
            for(var report : reservation.getReports()) {
                if(report.getStatus() == RequestStatus.Accepted && report.getAuthor() != c) {
                    if(report.getTimeStamp().isAfter(ChronoLocalDateTime.from(getFirstOfTheMonth())) &&
                        report.getTimeStamp().isBefore(ChronoLocalDateTime.from(LocalDateTime.now())))
                        penalties.add(modelMapper.map(report, ClientPenaltyDto.class));
        }}}
        return penalties;
    }

    private LocalDateTime getFirstOfTheMonth() {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime firstInMonth = today.withDayOfMonth(1);
        firstInMonth = firstInMonth.withHour(0).withMinute(0).withSecond(0);
        return firstInMonth;
    }
}
