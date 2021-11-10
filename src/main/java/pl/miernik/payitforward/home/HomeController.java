package pl.miernik.payitforward.home;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.miernik.payitforward.user.UserDto;
import pl.miernik.payitforward.user.UserService;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final UserService userService;


    @GetMapping("/login")
    public String login() {
        return "loginFile";
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/registration")
    public String showRegistrationForm(ModelMap model) {
        model.addAttribute("user", new UserDto());
        return "registrationFile";
    }

    @PostMapping("/registration")
    public String registerUserAccount(@ModelAttribute("user") UserDto registrationDto) {
        userService.saveUser(registrationDto);
        return "redirect:/registration?success";
    }
}