package pw.ersms.accounts.account;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import pw.ersms.accounts.config.AuthRequest;
import pw.ersms.accounts.config.JwtTokenUtil;
import pw.ersms.accounts.config.AuthResponse;
import org.springframework.http.*;
import org.springframework.security.authentication.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("account")
public class AccountController {
    @Autowired
    JwtTokenUtil jwtUtil;
    private final AccountService accountService;

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);


    @GetMapping
    public ResponseEntity<List<Account>> getAccount() {
        return ResponseEntity.ok().body(accountService.get());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok().body(accountService.getAccountById(id));
    }

    @DeleteMapping(path = "/{id}")
    public void deleteAccount(@PathVariable("id") Integer id) {
        accountService.deleteAccount(id);
    }

    @PostMapping
    public ResponseEntity<Object> registerNewAccount(@RequestBody Account account) {
        return ResponseEntity.ok().body(accountService.create(account));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Object> updateAccount(
            @PathVariable("id") Integer id,
            @RequestBody Account account) {
        return ResponseEntity.ok().body(accountService.updateAccount(id, account));
    }

    @GetMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        try {
            String url = "https://www.googleapis.com/oauth2/v3/userinfo";

            //put the token in the authorization header
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(authRequest.getCode());
            HttpEntity<String> entity = new HttpEntity<>(headers);

            try {
                //send the GET request, it will return a JSON response
                ResponseEntity<String> responseFromGoogle = new RestTemplate().exchange(url, HttpMethod.GET, entity, String.class);

                if (responseFromGoogle.getStatusCode() != HttpStatus.OK) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                }

                //get the user info
                String userInfo = responseFromGoogle.getBody();

                String emailFromGoogle = userInfo.substring(userInfo.indexOf("email") + 9, userInfo.indexOf("email_verified") - 6);

                Account user = accountService.findAccountByEmail(emailFromGoogle);
                String accessToken = jwtUtil.generateAccessToken(user);
                AuthResponse response = new AuthResponse();
                response.setAccessToken(accessToken);
                return ResponseEntity.ok().body(response);

            } catch (BadCredentialsException ex) {
                LOGGER.info(ex.getMessage());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
