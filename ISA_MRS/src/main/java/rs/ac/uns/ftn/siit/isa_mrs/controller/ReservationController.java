package rs.ac.uns.ftn.siit.isa_mrs.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.auth.AUTH;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.siit.isa_mrs.dto.BackToFrontDto.BoatOwnerDtos.BoatOwnerReservationDto;
import rs.ac.uns.ftn.siit.isa_mrs.dto.BackToFrontDto.ClientDtos.ClientReservationLimitsDto;
import rs.ac.uns.ftn.siit.isa_mrs.dto.BackToFrontDto.InstructorDtos.InstructorReservationsDtos.InstructorReservationDto;
import rs.ac.uns.ftn.siit.isa_mrs.dto.BackToFrontDto.VacationRentalOwnerDtos.ReservationRentalLimitsDto;
import rs.ac.uns.ftn.siit.isa_mrs.dto.BackToFrontDto.VacationRentalOwnerDtos.VacationRentalOwnerReservationDto;
import rs.ac.uns.ftn.siit.isa_mrs.dto.BackToFrontDto.InstructorDtos.ReservationLimitsDto;
import rs.ac.uns.ftn.siit.isa_mrs.dto.FrontToBackDto.OwnerReservationDto;
import rs.ac.uns.ftn.siit.isa_mrs.dto.FrontToBackDto.ReportDtos.AddInstructorReportDto;
import rs.ac.uns.ftn.siit.isa_mrs.dto.FrontToBackDto.ReportDtos.AddReportDto;
import rs.ac.uns.ftn.siit.isa_mrs.dto.FrontToBackDto.ReservationDtos.ClientBookDto;
import rs.ac.uns.ftn.siit.isa_mrs.dto.FrontToBackDto.ReservationDtos.ReserveSpecialOfferDto;
import rs.ac.uns.ftn.siit.isa_mrs.dto.FrontToBackDto.ReviewDtos.AddInstructorReviewDto;
import rs.ac.uns.ftn.siit.isa_mrs.dto.FrontToBackDto.ReviewDtos.AddReviewDto;
import rs.ac.uns.ftn.siit.isa_mrs.dto.ReservationDto;
import rs.ac.uns.ftn.siit.isa_mrs.service.InstructorService;
import rs.ac.uns.ftn.siit.isa_mrs.service.RentalObjectOwnerService;
import rs.ac.uns.ftn.siit.isa_mrs.service.RentalObjectService;
import rs.ac.uns.ftn.siit.isa_mrs.service.ReservationService;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static rs.ac.uns.ftn.siit.isa_mrs.util.Paths.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/Reservations")
public class ReservationController {
    private final ReservationService reservationService;
    private final InstructorService instructorService;
    private final RentalObjectOwnerService rentalObjectOwnerService;
    private final RentalObjectService rentalObjectService;

    @PostMapping("/bookSpecialOffer")
    public ResponseEntity<Void> bookSpecialOffer(@RequestBody ReserveSpecialOfferDto rsod, HttpServletRequest request) {
        return reservationService.bookSpecialOffer(request.getHeader(AUTHORIZATION), rsod.getOfferId(), rsod.getTotal());
    }

    @GetMapping("/getReservations")
    public ResponseEntity<Collection<ReservationDto>> getFutureReservations(HttpServletRequest request) {
        return reservationService.getFutureReservations(request.getHeader(AUTHORIZATION));
    }

    @GetMapping("/getInstructorReservations")
    public ResponseEntity<Collection<InstructorReservationDto>> getAllInstructorReservations(HttpServletRequest request) {
        return instructorService.getAllInstructrorReservations(request.getHeader(AUTHORIZATION));
    }

    @PostMapping("/addInstructorReview")
    public ResponseEntity<Void> addInstructorReview(@RequestBody AddInstructorReviewDto aird, HttpServletRequest request) {
        return reservationService.addInstructorReview(aird, request.getHeader(AUTHORIZATION));
    }

    @PostMapping("/addInstructorReport")
    public ResponseEntity<Void> addInstructorReport(@RequestBody AddInstructorReportDto aird, HttpServletRequest request) {
        return reservationService.addInstructorReport(aird, request.getHeader(AUTHORIZATION));
    }

    @PostMapping("/bookForClient")
    public ResponseEntity<Void> bookForClient(@RequestBody rs.ac.uns.ftn.siit.isa_mrs.dto.FrontToBackDto.InstructorReservationDto dto) {
        return reservationService.bookForClient(dto);
    }

    @PostMapping("/book")
    public ResponseEntity<Void> book(@RequestBody ClientBookDto cbd, HttpServletRequest request) {
        return reservationService.book(cbd, request.getHeader(AUTHORIZATION));
    }

    @GetMapping("/instructorReservationLimits")
    public ResponseEntity<ReservationLimitsDto> getInstructorReservationLimits(@RequestParam long id, HttpServletRequest request) {
        return instructorService.getReservationLimits(id, request.getHeader(AUTHORIZATION));
    }

    @GetMapping("/getLimits")
    public ResponseEntity<ClientReservationLimitsDto> getReservationLimits(@RequestParam long rentalId, @RequestParam long ownerId) {
        return rentalObjectOwnerService.getReservationLimits(rentalId, ownerId);
    }

    @PutMapping(CANCEL_RESERVATION)
    public ResponseEntity<Void> cancelReservation(@RequestParam Long id, @RequestParam double feeAmount) {
        return reservationService.cancelReservation(id, feeAmount);
    }

    @PostMapping(ADD_REVIEW)
    public ResponseEntity<Void> addReview(@RequestBody AddReviewDto ard, HttpServletRequest request) {
        return reservationService.addReview(ard, request.getHeader(AUTHORIZATION));
    }

    @PostMapping(ADD_REPORT)
    public ResponseEntity<Void> addReport(@RequestBody AddReportDto ard, HttpServletRequest request) {
        return reservationService.addReport(ard, request.getHeader(AUTHORIZATION));
    }

    @GetMapping("/getBoatOwnerReservations")
    public ResponseEntity<Collection<BoatOwnerReservationDto>> getAllBoatOwnerReservations(HttpServletRequest request) {
        return rentalObjectOwnerService.getAllBoatOwnerReservations(request.getHeader(AUTHORIZATION));
    }

    @GetMapping("/getVacationRentalOwnerReservations")
    public ResponseEntity<Collection<VacationRentalOwnerReservationDto>> getAllVacationRentalOwnerReservations(HttpServletRequest request) {
        return rentalObjectOwnerService.getAllVacationRentalOwnerReservations(request.getHeader(AUTHORIZATION));
    }

    @GetMapping("/ownerReservationLimits")
    public ResponseEntity<ReservationRentalLimitsDto> getReservationRentalLimits(@RequestParam long id, HttpServletRequest request) {
        return rentalObjectService.getReservationRentalLimits(id, request.getHeader(AUTHORIZATION));
    }

    @PostMapping("/ownerBookForClient")
    public ResponseEntity<Void> ownerBookForClient(@RequestBody OwnerReservationDto dto) {
        return reservationService.ownerBookForClient(dto);
    }
}
