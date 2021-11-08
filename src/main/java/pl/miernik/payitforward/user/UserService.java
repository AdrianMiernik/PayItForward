package pl.miernik.payitforward.user;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.miernik.payitforward.exception.NotExistingRecordException;
import pl.miernik.payitforward.security.Role;
import pl.miernik.payitforward.security.RoleRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void saveUser(UserDto userDto) {
        User user = userMapper.dtoToUser(userDto);
        user.addRole(roleRepository.findFirstByNameIgnoringCase("ROLE_USER"));
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(user);
    }

    @Override
    public UserDto getUserDtoById(Long id) throws NotExistingRecordException {
        return userRepository.findById(id)
                .map(userMapper::userToDto)
                .orElseThrow(new NotExistingRecordException("User with id " + id + " does not exist."));
    }

    @Override
    public UserDto getUsedDtoByEmail(String email) throws NotExistingRecordException {
        return userRepository.findFirstByEmailIgnoringCase(email)
                .map(userMapper::userToDto)
                .orElseThrow(new NotExistingRecordException("User with email " + email + " does not exist."));
    }

    // Used in update and delete
    private User getUser(Long id) throws NotExistingRecordException {
        Role roleUser = roleRepository.findFirstByNameIgnoringCase("ROLE_USER");
        return userRepository.findById(id)
                .filter(user -> user.getRoles().contains(roleUser))
                .orElseThrow(
                        new NotExistingRecordException("User with id " + id + " does not exist!"));
    }

    @Override
    public void update(UserDto userDto) throws NotExistingRecordException {
        User user = getUser(userDto.getId());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        userRepository.save(user);
    }

    @Override
    public void delete(Long id) throws NotExistingRecordException {
        User user = getUser(id);
        userRepository.delete(user);
    }

    @Override
    public List<UserDto> findAllUsers() {
        Role roleUser = roleRepository.findFirstByNameIgnoringCase("ROLE_USER");
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getRoles().contains(roleUser))
                .map(userMapper::userToDto)
                .collect(Collectors.toList());
    }

    //SECURITY
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmailIgnoreCase(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }


    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    //Authentication of the user by email
    @Override
    public User getPrincipal() throws NotExistingRecordException {
        String principalEmail = getPrincipalEmail();
        return userRepository.findFirstByEmailIgnoringCase(principalEmail).orElseThrow(
                new NotExistingRecordException("User with email " + principalEmail + " does not exist."));
    }

    @Override
    public String getPrincipalEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }
        return email;
    }
}

