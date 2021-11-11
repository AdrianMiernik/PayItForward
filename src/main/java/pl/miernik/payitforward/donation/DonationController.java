package pl.miernik.payitforward.donation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.miernik.payitforward.category.Category;
import pl.miernik.payitforward.category.CategoryService;
import pl.miernik.payitforward.exception.NotExistingRecordException;
import pl.miernik.payitforward.institution.Institution;
import pl.miernik.payitforward.institution.InstitutionDto;
import pl.miernik.payitforward.institution.InstitutionService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/donation")
@RequiredArgsConstructor
public class DonationController {
    private final InstitutionService institutionService;
    private final CategoryService categoryService;
    private final DonationService donationService;

    @GetMapping
    public String displayForm(ModelMap model) {
        model.addAttribute("donation", new DonationDto());
        return "/donations/form";
    }

    @PostMapping
    public String addDonation(@ModelAttribute("donation")
                              @Valid DonationDto donationDto,
                              BindingResult violation)
            throws NotExistingRecordException {
        if (violation.hasErrors()) {
            return "/donations/form";
        }
        donationService.save(donationDto);
        return ("/donations/form-confirmation");
    }

    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortDir,
                                ModelMap model) {
        int pageSize = 6;

        Page<Institution> page = institutionService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<Institution> list = page.getContent();
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("totalDonations", donationService.countTotalDonations());
        model.addAttribute("totalBoxes", donationService.countTotalBoxes());
        model.addAttribute("topDonators", donationService.findTopThree());
        model.addAttribute("institutions", list);

        return "/donations/main";
    }

    @GetMapping("/main")
    public String displayMain(ModelMap model) {
        return findPaginated(1, "name", "asc", model);
    }

    @GetMapping("/list")
    public String displayDonations(ModelMap model) throws NotExistingRecordException {
        List<DonationDto> donations = donationService.findAllByPrincipalActive();
        model.addAttribute("donations", donations);

        List<DonationDto> donationsPast = donationService.findAllByPrincipalPast();
        model.addAttribute("donationsPast", donationsPast);
        return "/donations/donationlist";
    }

    @PostMapping("/deleteDonation")
    public String deleteDonation(@RequestParam Long id) throws NotExistingRecordException {
        donationService.delete(id);
        return "redirect:/donation/list";
    }
    /*  -- ALTERNATIVE SOLUTION for @PathVariable
     * @GetMapping("/deleteDonation/{id}") public String
     * deleteDonation(@ParhVariable Long id) throws NotExistingRecordException {
     * donationService.delete(id); return "redirect:/donation/list"; }
     */

    @PostMapping("/updateDonation")
    public String changeStatus(@RequestParam Long id) throws NotExistingRecordException {
        donationService.changeDonationStatus(id);
        return "redirect:/donation/list";
    }


    @ModelAttribute("institutions")
    private List<InstitutionDto> institutions() {
        return institutionService.findAll();
    }

    @ModelAttribute("categories")
    private List<Category> categories() {
        return categoryService.findAllCategories();
    }
}