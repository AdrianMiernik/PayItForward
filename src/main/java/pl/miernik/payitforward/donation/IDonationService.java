package pl.miernik.payitforward.donation;
import pl.miernik.payitforward.exception.NotExistingRecordException;

import java.util.*;

public interface IDonationService {
    //CRUD
    void save(DonationDto donationDto) throws NotExistingRecordException;
    List<DonationDto> findAll();

    //EXTRA METHODS
    int countTotalBoxes();
    Long countTotalDonations();
    void changeDonationStatus(Long id) throws NotExistingRecordException;
    List<DonationDto> findAllByPrincipalActive() throws NotExistingRecordException;
    List<DonationDto> findAllByPrincipalPast() throws NotExistingRecordException;
    Map<String, Integer> findTopThree();
    void delete(Long id);

}
