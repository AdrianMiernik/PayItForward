package pl.miernik.payitforward.donation;

import org.springframework.stereotype.Component;

@Component
public class DonationMapper {

    public Donation dtoToDonation(DonationDto donationDto) {
        return Donation.builder()
                .id(donationDto.getId())
                .zipCode(donationDto.getZipCode())
                .quantity(donationDto.getQuantity())
                .street(donationDto.getStreet())
                .pickUpTime(donationDto.getPickUpTime())
                .pickUpDate(donationDto.getPickUpDate())
                .phoneNumber(donationDto.getPhoneNumber())
                .city(donationDto.getCity())
                .institution(donationDto.getInstitution())
                .categories(donationDto.getCategories())
                .pickUpComment(donationDto.getPickUpComment())
                .actualPickUpDate(donationDto.getActualPickUpDate())
                .build();
    }

    public DonationDto donationToDto(Donation donation) {
        return DonationDto.builder()
                .id(donation.getId())
                .zipCode(donation.getZipCode())
                .quantity(donation.getQuantity())
                .street(donation.getStreet())
                .pickUpTime(donation.getPickUpTime())
                .pickUpDate(donation.getPickUpDate())
                .phoneNumber(donation.getPhoneNumber())
                .city(donation.getCity())
                .institution(donation.getInstitution())
                .categories(donation.getCategories())
                .pickUpComment(donation.getPickUpComment())
                .actualPickUpDate(donation.getActualPickUpDate())
                .build();
    }

}
