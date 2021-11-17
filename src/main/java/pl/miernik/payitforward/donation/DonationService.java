package pl.miernik.payitforward.donation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.miernik.payitforward.exception.NotExistingRecordException;
import pl.miernik.payitforward.user.User;
import pl.miernik.payitforward.user.UserService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DonationService implements IDonationService {
    private final DonationRepository donationRepository;
    private final DonationMapper donationMapper;
    private final UserService userService;

    @Override
    public void save(DonationDto donationDto) throws NotExistingRecordException {
        User principal = userService.getPrincipal();
        Donation donation = donationMapper.dtoToDonation(donationDto);
        donation.setUser(principal);
        donationRepository.save(donation);
    }

    @Override
    public int countTotalBoxes() {
        return donationRepository.countTotalBoxes().orElse(0);
    }

    @Override
    public Long countTotalDonations() {
        return donationRepository.count();
    }

    // Method used for changing donation status
    private Donation getDonation(Long id) throws NotExistingRecordException {
        return donationRepository.findById(id)
                .orElseThrow(new NotExistingRecordException("Donation with id " + id + " does not exist."));
    }

    @Override
    public void changeDonationStatus(Long id) throws NotExistingRecordException {
        LocalDate currentDate = LocalDate.now();
        Donation donation = getDonation(id);
        donation.setActualPickUpDate(currentDate);
        donation.setIsPickedUp(true);
        donationRepository.save(donation);
    }

    @Override
    public List<DonationDto> findAll() {
        return donationRepository.findAll()
                .stream()
                .map(donationMapper::donationToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DonationDto> findAllByPrincipalActive() throws NotExistingRecordException {
        Long principalId = userService.getPrincipal().getId();
        return donationRepository.findAll().stream()
                .filter(donation -> donation.getUser().getId().equals(principalId))
                .filter(donation -> donation.getIsPickedUp().equals(false)).map(donationMapper::donationToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DonationDto> findAllByPrincipalPast() throws NotExistingRecordException {
        Long principalId = userService.getPrincipal().getId();
        return donationRepository.findAll().stream().filter(donation -> donation.getUser().getId().equals(principalId))
                .filter(donation -> donation.getIsPickedUp().equals(true)).map(donationMapper::donationToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Integer> findTopThree() {
        List<Donation> list = donationRepository.findAll();
        Map<String, Integer> map = new HashMap<>();
        for (Donation each : list) {
            if (map.containsKey(each.getUser().getFirstName())) {
                map.put(each.getUser().getFirstName(), map.get(each.getUser().getFirstName()) + each.getQuantity());
            } else {
                map.put(each.getUser().getFirstName(), each.getQuantity());
            }
        }
        return map.entrySet()
                .stream()
                .sorted((e1, e2) -> Integer.compare(e1.getValue(), e2.getValue()))
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(3)
                //.collect(Collectors.toMap(e -> e.getKey(),e -> e.getValue()));
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    @Override
    public void delete(Long id) {
        donationRepository.deleteById(id);
    }
}