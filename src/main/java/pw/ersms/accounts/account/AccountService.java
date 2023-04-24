package pw.ersms.accounts.account;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public List<Account> get() {
        return accountRepository.findAll();
    }

    public void addNewAccount(Account account) {
        Optional<Account> accountOptional = accountRepository.findAccountByEmail(account.getEmail());
        if (accountOptional.isPresent()) {
            throw new IllegalStateException("email taken");
        }
        accountRepository.save(account);
    }

    public void deleteAccount(Integer accountId) {
        boolean exists = accountRepository.existsById(accountId);
        if (!exists) {
            throw new IllegalStateException("account with id " + accountId + " does not exist");
        }
        accountRepository.deleteById(accountId);
    }

    public void updateAccount(Integer accountId, Account account) {
        Account accountNew = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalStateException(
                        "account with id " + accountId + " does not exist"
                ));
        //for every field in account, if it is not null, set the corresponding field in the account with the id accountId to the value of the field in account
        if(account.getEmail() != null && account.getEmail().length() > 0) {
            accountNew.setEmail(account.getEmail());
        }
        if(account.getIsActivated() != null) {
            accountNew.setIsActivated(account.getIsActivated());
        }
        if(account.getIsDeleted() != null) {
            accountNew.setIsDeleted(account.getIsDeleted());
        }
        if(account.getAddressLine1() != null && account.getAddressLine1().length() > 0) {
            accountNew.setAddressLine1(account.getAddressLine1());
        }

        if(account.getAddressLine2() != null && account.getAddressLine2().length() > 0) {
            accountNew.setAddressLine2(account.getAddressLine2());
        }
    }
}
