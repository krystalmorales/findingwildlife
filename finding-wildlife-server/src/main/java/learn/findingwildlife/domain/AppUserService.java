package learn.findingwildlife.domain;

import learn.findingwildlife.data.AppUserRepository;
import learn.findingwildlife.model.AppUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Objects;

@Service
public class AppUserService implements UserDetailsService {
    private final AppUserRepository repository;
    private final PasswordEncoder encoder;

    public AppUserService(AppUserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    public AppUser findByAppUserId(int appUserId) {
        return repository.findByAppUserId(appUserId);
    }

    public AppUser createUser(String username, String password) {
        validate(username);
        validatePassword(password);

        password = encoder.encode(password);

        AppUser appUser = new AppUser(0, username, password, false, List.of("USER"));

        return repository.createUser(appUser);
    }

    public Result<Void> validateUser(AppUser user) {
        Result<Void> result = new Result<>();

        if (user == null) {
            result.addErrorMessage("You must login to access these features.");
            return result;
        }
        return result;
    }

    public Result<Void> validateUserRoleAsAdmin(AppUser user) {
        Result<Void> result = validateUser(user);

        List<String> userRoles = AppUser.convertAuthoritiesToRoles(user.getAuthorities());

        if (!userRoles.contains("ADMIN")) {
            result.addErrorMessage("You do not have the authority to do this action.");
        }

        return result;
    }

    private void validate(String username) {
        if (username == null || username.isBlank()) {
            throw new ValidationException("Username is required.");
        }

        if (username.length() > 50) {
            throw new ValidationException("Username must be less than 50 characters.");
        }

    }

    private void validatePassword(String password) {
        if (password == null || password.length() < 8) {
            throw new ValidationException("Password must be atleast 8 characters.");
        }

        int digits = 0;
        int letters = 0;
        int others = 0;
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                digits++;
            } else if (Character.isLetter(c)) {
                letters++;
            } else {
                others++;
            }
        }

        if (digits == 0 || letters == 0 || others == 0) {
            throw new ValidationException("Password must contain a digit, a letter and a non-digit/non-letter.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        AppUser appUser = repository.findByUsername(s);

        if (appUser == null || !appUser.isEnabled()) {
            throw new UsernameNotFoundException(s + " not found");
        }

        return appUser;
    }

}
