package pw.ersms.accounts.account;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pw.ersms.accounts.config.JwtTokenUtil;
import pw.ersms.accounts.config.AuthResponse;
import org.springframework.http.*;
import org.springframework.security.authentication.*;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("account")
public class AccountController {
    @Autowired
    JwtTokenUtil jwtUtil;
    private final AccountService accountService;

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

    @PostMapping("/login/{email}")
    public ResponseEntity<AuthResponse> login(@PathVariable String email) {
        try {

            Account user = accountService.findAccountByEmail(email);
            System.out.println(user.getCity());
            String accessToken = jwtUtil.generateAccessToken(user);
            System.out.println(accessToken);
            AuthResponse response = new AuthResponse(user.getEmail(), accessToken);
            System.out.println(response);
            return ResponseEntity.ok().body(response);

        } catch (BadCredentialsException ex) {
            System.out.println(ex);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
