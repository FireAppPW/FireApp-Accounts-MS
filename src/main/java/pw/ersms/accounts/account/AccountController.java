package pw.ersms.accounts.account;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("account/")
public class AccountController {
    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<List<Account>> getAccount() {
        return ResponseEntity.ok().body(accountService.get());
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<Account> getAccountById(Integer id) {
        return ResponseEntity.ok().body(accountService.get().get(id));
    }

    @DeleteMapping(path = "{id}")
    public void deleteAccount(Integer id) {
        accountService.deleteAccount(id);
    }

    @PostMapping
    public void registerNewAccount(@RequestBody Account account) {
        accountService.addNewAccount(account);
    }
    
    @PutMapping(path = "{id}")
    public void updateAccount(
            @PathVariable("id") Integer id,
            @RequestBody Account account) {
        accountService.updateAccount(id, account);
    }


}
