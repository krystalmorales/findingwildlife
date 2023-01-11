package learn.findingwildlife.controller;

import learn.findingwildlife.security.JwtConverter;
import learn.findingwildlife.domain.AppUserService;
import learn.findingwildlife.model.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;

import org.springframework.web.bind.annotation.*;

import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtConverter converter;
    private final AppUserService service;

    public AuthController(AuthenticationManager authenticationManager, JwtConverter converter, AppUserService service) {
        this.authenticationManager = authenticationManager;
        this.converter = converter;
        this.service = service;
    }

    @GetMapping("/{appUserId}")
    public AppUser findByAppUserId(@PathVariable int appUserId) {
        return service.findByAppUserId(appUserId);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody Map<String, String> credentials) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(credentials.get("username"), credentials.get("password"));
        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            if (authentication.isAuthenticated()) {
                String jwtToken = converter.getTokenFromAppUser((AppUser) authentication.getPrincipal());

                HashMap<String, String> map = new HashMap<>();
                map.put("jwt_token", jwtToken);

                return new ResponseEntity<>(map, HttpStatus.OK);
            }
        } catch (AuthenticationException ex) {
            System.out.println(ex);
        }

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

//    @PostMapping("/refresh_token")
//    public ResponseEntity<Map<String, String>> refreshToken(@RequestHeader(name = "authorization") User user) {
//        String jwtToken = converter.getTokenFromAppUser(user);
//
//        HashMap<String, String> map = new HashMap<>();
//        map.put("jwt_token", jwtToken);
//
//        return new ResponseEntity<>(map, HttpStatus.OK);
//    }

    @PostMapping("/create_account")
    public ResponseEntity<?> createAccount(@RequestBody Map<String, String> credentials) {
        AppUser appUser = null;

        try {
            String username = credentials.get("username");
            String password = credentials.get("password");

            appUser = service.createUser(username, password);
        } catch (ValidationException ex) {
            return new ResponseEntity<>(List.of(ex.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (DuplicateKeyException ex) {
            return new ResponseEntity<>(List.of("The provided username already exists."), HttpStatus.BAD_REQUEST);
        }

        HashMap<String, Integer> map = new HashMap<>();
        map.put("appUserId", appUser.getAppUserId());

        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

}
