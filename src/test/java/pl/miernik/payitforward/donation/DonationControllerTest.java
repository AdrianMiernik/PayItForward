package pl.miernik.payitforward.donation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import pl.miernik.payitforward.category.Category;
import pl.miernik.payitforward.category.CategoryRepository;
import pl.miernik.payitforward.category.CategoryService;
import pl.miernik.payitforward.institution.Institution;
import pl.miernik.payitforward.institution.InstitutionMapper;
import pl.miernik.payitforward.institution.InstitutionRepository;
import pl.miernik.payitforward.institution.InstitutionService;
import pl.miernik.payitforward.user.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ComponentScan("pl.miernik.payitforward.*")
@WebMvcTest(DonationController.class)
//@SpringBootTest(classes = DonationController.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

class DonationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private IUserService userServiceMock;

    @MockBean
    private InstitutionRepository institutionRepository;

    @MockBean
    private InstitutionService institutionService;

    @MockBean
    InstitutionMapper institutionMapper;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private DonationService donationServiceMock;

    @MockBean
    private DonationRepository donationRepository;

    @MockBean
    private DonationMapper donationMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserMapper userMapper;

    private DonationDto donationDto;
    List<DonationDto> allDonations;
    List<DonationDto> allActiveDonations;
    List<DonationDto> allPastDonations;
    Map<String, Integer> map;


    @BeforeEach
    void setUp() throws Exception {

        allActiveDonations = new ArrayList<DonationDto>();
        allPastDonations = new ArrayList<DonationDto>();

        final Institution redCross = Institution.builder().id(50L).name("Red Cross").description("We help").build();
        final Category clothes = Category.builder().id(40L).name("clothes").build();
        final Category toys = Category.builder().id(41L).name("toys").build();
        final Set<Category> categories = Set.of(toys, clothes);

        donationDto = DonationDto.builder().categories(categories).institution(redCross).city("Cracow")
                .phoneNumber("+48 500 600 700").pickUpDate(LocalDate.now().plusMonths(1)).pickUpTime(LocalTime.now())
                .street("12 High Street").quantity(5).zipCode("33-333").build();

        final UserDto userDto = UserDto.builder().id(222L).firstName("John").lastName("Smith").password("Password1!")
                .email("generic@test").build();
        when(userServiceMock.getUsedDtoByEmail(userDto.getEmail())).thenReturn(userDto);
    }

    @Test
    void test_displayDonationForm() throws Exception {
        mockMvc.perform(get("/donation")).andExpect(status().isOk())
                .andExpect(model().attribute("donation", new DonationDto())).andExpect(view().name("/donations/form"));
    }

    @Test
    void test_saveDonationDtoAsDonationToDatabase() throws Exception {
        mockMvc.perform(post("/donation").with(csrf()).flashAttr("donation", donationDto)).andExpect(status().isOk());
        verify(donationServiceMock).save(donationDto);
    }


    @Test
    void test_DonationDtoShouldNotBeSavedToDatabase() throws Exception {
        final Institution redCross = Institution.builder().id(45L).name("Red Cross").build();
        final DonationDto invalidDonation = DonationDto.builder().institution(redCross)
                .phoneNumber("+48 222 222").pickUpDate(LocalDate.now().plusMonths(1)).pickUpTime(LocalTime.now())
                .quantity(2).zipCode("44-b4b4b4").build();

        mockMvc.perform(post("/donation").with(csrf()).flashAttr("donation", invalidDonation))
                .andExpect(status().isOk()).andExpect(model().errorCount(5)).andExpect(model()
                .attributeHasFieldErrors("donation", "categories", "city", "street", "zipCode", "phoneNumber"))
                .andExpect(view().name("/donations/form"));
    }

    @Test
    void test_displayMain() throws Exception {
        when(donationServiceMock.findAll()).thenReturn(allDonations);
        when(donationServiceMock.findAllByPrincipalPast()).thenReturn(allPastDonations);

        mockMvc.perform(get("/donation/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("/donations/donationlist"))
                .andExpect(model().attribute("donations", allActiveDonations))
                .andExpect(model().attribute("donationsPast", allPastDonations));
    }


    @Test
    void testFindPaginated() throws Exception {
        Page<Institution> page = null;
        List<Institution> list = null;
        int pageNo = 10;
        int pageSize = 7;
        String sortField = "sortField";
        String sortDir = "sortDir";
        int totalBoxes = 5;
        long totalDonations = 3;
        allDonations = new ArrayList<DonationDto>();
        map = new HashMap<String, Integer>();
        map.put("Franek", 3);
        map.put("Janek", 5);
        map.put("Maciej", 2);

        when(donationServiceMock.countTotalDonations()).thenReturn(totalDonations);
        when(donationServiceMock.countTotalBoxes()).thenReturn(totalBoxes);
        when(donationServiceMock.findTopThree()).thenReturn(map);
        when(institutionService.findPaginated(pageNo, pageSize, sortField, sortDir)).thenReturn(page);
        list = page.getContent();

        mockMvc.perform(get("/page/10"))
                .andExpect(status().isOk())
                .andExpect(view().name("/donations/main"))
                .andExpect(model().attribute("totalDonations", totalDonations))
                .andExpect(model().attribute("totalBoxes", totalBoxes))
                .andExpect(model().attribute("topDonators", map))
                .andExpect(model().attribute("institutions", list))
                .andExpect(model().attribute("currentPage", pageNo))
                .andExpect(model().attribute("totalPages", page.getTotalPages()))
                .andExpect(model().attribute("totalItems", page.getTotalElements()));

    }

    @Test
    void test_displayDonations() throws Exception {
        when(donationServiceMock.findAllByPrincipalActive()).thenReturn(allActiveDonations);
        when(donationServiceMock.findAllByPrincipalPast()).thenReturn(allPastDonations);

        mockMvc.perform(get("/donation/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("/donations/donationlist"))
                .andExpect(model().attribute("donations", allActiveDonations))
                .andExpect(model().attribute("donationsPast", allPastDonations));
    }

    @Test
    void test_deleteDonationById() throws Exception {
        Long id = 35L;

        mockMvc.perform(post("/donation/deleteDonation").with(csrf())
                .param("id", id.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/donation/list"));
        verify(donationServiceMock).delete(id);

    }

    @Test
    void test_changeStatusOfDonationTo_IsPickedUpEqualsTrue() throws Exception {
        Long id = 25L;

        mockMvc.perform(post("/donation/updateDonation").with(csrf())
                .param("id", id.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/donation/list"));
        verify(donationServiceMock).changeDonationStatus(id);

    }

}
