package pl.miernik.payitforward.donation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import pl.miernik.payitforward.category.Category;
import pl.miernik.payitforward.exception.NotExistingRecordException;
import pl.miernik.payitforward.institution.Institution;
import pl.miernik.payitforward.user.User;
import pl.miernik.payitforward.user.UserDto;
import pl.miernik.payitforward.user.UserService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

class DonationServiceTest {
    private IDonationService donationServiceMock;
    private DonationMapper donationMapper;
    private DonationRepository donationRepositoryMock;

    private Institution institution;
    private Set<Category> categories;
    private User user;

    @BeforeEach
    void setUp() throws NotExistingRecordException {
        donationMapper = new DonationMapper();
        donationRepositoryMock = mock(DonationRepository.class);
        UserService userServiceMock = mock(UserService.class);
        donationServiceMock = new DonationService(donationRepositoryMock, donationMapper, userServiceMock);

        institution = Institution.builder().id(15L).name("Red Cross").build();
        Category clothes = Category.builder().id(22L).name("clothes").build();
        Category toys = Category.builder().id(33L).name("toys").build();
        categories = Set.of(toys, clothes);

        UserDto userResource = UserDto.builder()
                .id(55L)
                .firstName("John")
                .lastName("Smith")
                .password("Password1!")
                .email("genetic@test")
                .build();
        user = User.builder()
                .id(55L)
                .firstName("John")
                .lastName("Smith")
                .password("Password1!")
                .email("genetic@test")
                .build();

        when(userServiceMock.getUsedDtoByEmail("generic@test")).thenReturn(userResource);
        when(userServiceMock.getPrincipal()).thenReturn(user);
    }

    @Test
    @WithMockUser(username = "user@test")
    void test_saveDonationtoDatabase() throws NotExistingRecordException {
        Donation donationToSave = Donation.builder()
                .categories(categories)
                .institution(institution)
                .city("Cracow")
                .phoneNumber("+48 500 600 700")
                .pickUpDate(LocalDate.now().plusMonths(1))
                .pickUpTime(LocalTime.now())
                .street("18 High Street")
                .quantity(5)
                .zipCode("30-993")
                .build();
        DonationDto donationResource = donationMapper.donationToDto(donationToSave);
        Donation savedDonation = donationToSave.toBuilder().id(111L).build();
        when(donationRepositoryMock.save(donationToSave)).thenReturn(savedDonation);

        donationServiceMock.save(donationResource);

        verify(donationRepositoryMock).save(donationToSave);
    }

    @Test
    @WithMockUser(username = "user@test")
    void test_CountTotalBoxes() {
        //given
        Integer expectedBoxesNumber = 22;
        when(donationRepositoryMock.countTotalBoxes()).thenReturn(Optional.of(expectedBoxesNumber));
        //when
        final int actualBagsNumber = donationServiceMock.countTotalBoxes();
        //then
        verify(donationRepositoryMock).countTotalBoxes();
        assertThat(actualBagsNumber, is(expectedBoxesNumber));
    }

    @Test
    @WithMockUser(username = "user@test")
    void test_countTotalBoxesReturnZeroIfNull() {
        when(donationRepositoryMock.countTotalBoxes()).thenReturn(Optional.empty());
        final int expectedBagNum = 0;

        final int actualBagsNum = donationServiceMock.countTotalBoxes();

        assertThat(actualBagsNum, is(expectedBagNum));
    }

    @Test
    @WithMockUser(username = "user@test")
    void test_countTotalDonations() {
        Long expectedDonationNumber = 10L;
        LocalDate now = LocalDate.now();
        when(donationRepositoryMock.count()).thenReturn(expectedDonationNumber);

        Long actualDonationNumber = donationServiceMock.countTotalDonations();

        verify(donationRepositoryMock).count();
        assertThat(actualDonationNumber, is(expectedDonationNumber));
    }

    @Test
    @WithMockUser(username = "user@test")
    void test_countTotalDonationsReturnZero() {
        LocalDate now = LocalDate.now();
        when(donationRepositoryMock.count()).thenReturn(0L);
        Long expectedDonationNum = 0L;

        Long actualDonationsNum = donationServiceMock.countTotalDonations();

        verify(donationRepositoryMock).count();
        assertThat(actualDonationsNum, is(expectedDonationNum));
    }


    @Test
    @WithMockUser(username = "user@test")
    void test_findListOfDonations() {
        Donation donation1 = Donation.builder()
                .id(11L)
                .isPickedUp(true)
                .created(LocalDate.now().minusDays(10))
                .actualPickUpDate(LocalDate.now().minusDays(3))
                .institution(institution)
                .build();
        final Donation donation2 = Donation.builder()
                .id(33L)
                .isPickedUp(false)
                .created(LocalDate.now().minusDays(2))
                .institution(institution)
                .build();
        final List<Donation> donations = List.of(donation1, donation2);
        final List<DonationDto> expected = donations
                .stream()
                .map(donationMapper::donationToDto)
                .collect(Collectors.toList());
        when(donationRepositoryMock.findAll())
                .thenReturn(donations);

        final List<DonationDto> actual = donationServiceMock.findAll();

        assertThat(actual, is(expected));
    }


}
