package pw.ersms.accounts.account;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("account")
public class AccountController {
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

}
