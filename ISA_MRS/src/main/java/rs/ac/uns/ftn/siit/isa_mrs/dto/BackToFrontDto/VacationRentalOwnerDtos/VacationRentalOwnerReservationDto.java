package rs.ac.uns.ftn.siit.isa_mrs.dto.BackToFrontDto.VacationRentalOwnerDtos;

import lombok.Data;
import rs.ac.uns.ftn.siit.isa_mrs.dto.BackToFrontDto.ReservationDtos.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;

@Data
public class VacationRentalOwnerReservationDto implements Serializable {
    private Long id;
    private Integer price;
    private Integer people;
    private Boolean cancelled;
    private Boolean equipmentRequired;
    private LocalDateTime timeStamp;
    private LocalDateTime initDate;
    private LocalDateTime termDate;
    private Collection<ReservationReviewDto> reviews;
    private Collection<ReservationReportDto> reports;
    private Collection<ReservationAdditionalServiceDto> additionalServices;
    private ReservationRentalObjectDto rentalObject;
    private ReservationClientDto client;
    private ReservationIncomeDto income;
}
