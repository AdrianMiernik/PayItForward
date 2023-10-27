package pl.miernik.payitforward.user;

import org.springframework.security.core.userdetails.UserDetailsService;
import pl.miernik.payitforward.exception.NotExistingRecordException;

import java.util.*;

public interface IUserService extends UserDetailsService {

    //CRUD
    void saveUser(UserDto userDto);

    UserDto getUserDtoById(Long id) throws NotExistingRecordException;

    void update(UserDto userDto) throws NotExistingRecordException;

    void delete(Long id) throws NotExistingRecordException;

    List<UserDto> findAllUsers();

    //ADDITIONAL
    UserDto getUsedDtoByEmail(String email) throws NotExistingRecordException;

    User getPrincipal() throws NotExistingRecordException;

    String getPrincipalEmail();
}