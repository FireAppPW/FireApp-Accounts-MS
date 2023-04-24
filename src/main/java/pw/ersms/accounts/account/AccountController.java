package pw.ersms.accounts.account;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping(path = "/account")
    public ResponseEntity<Object> getAccount() {
        return ResponseEntity.ok().body(accountService.getAccount());
    }
}
