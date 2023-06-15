package pw.ersms.accounts.account;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pw.ersms.accounts.role.Role;
import pw.ersms.accounts.role.RoleRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;

    public List<Account> get(Integer departmentId) {
        return accountRepository.findAll().stream()
                .filter(account -> account.getFireDepartmentId().equals(departmentId))
                .collect(Collectors.toList());
    }

    public Account getAccountById(Integer accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalStateException(
                        "account with id " + accountId + " does not exist"
                ));
    }

    public Object create(Account account) {
        //check if email is taken
        Optional<Account> accountOptional = accountRepository.findAccountByEmail(account.getEmail());
        if (accountOptional.isPresent()) {
            throw new IllegalStateException("email taken");
        }
        //check if role exists
        Optional<Role> roleOptional = roleRepository.findById(account.getRole().getId());

        if (!roleOptional.isPresent()) {
            throw new IllegalStateException("role does not exist");
        }

        //set role
        account.setRole(roleOptional.get());

        return accountRepository.save(account);
    }

    public void deleteAccount(Integer accountId) {
        boolean exists = accountRepository.existsById(accountId);
        if (!exists) {
            throw new IllegalStateException("account with id " + accountId + " does not exist");
        }
        accountRepository.deleteById(accountId);
    }

    public Object updateAccount(Integer accountId, Account account) {
        Account accountNew = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalStateException(
                        "account with id " + accountId + " does not exist"
                ));
        //check if email is taken
        Optional<Account> accountOptional = accountRepository.findAccountByEmail(account.getEmail());

        if (accountOptional.isPresent() && !accountOptional.get().getId().equals(accountId)) {
            throw new IllegalStateException("email taken");
        }

        //check if role exists
        Optional<Role> roleOptional = roleRepository.findById(account.getRole().getId());

        if (!roleOptional.isPresent()) {
            throw new IllegalStateException("role does not exist");
        }

        //set role
        account.setRole(roleOptional.get());

        //set id
        account.setId(accountNew.getId());

        return accountRepository.save(account);

    }

    public Account findAccountByEmail(String email) {
        Optional<Account> accountOptional = accountRepository.findAccountByEmail(email);
        if (accountOptional.isPresent()) {
            return accountOptional.get();
        } else {
            throw new IllegalStateException("account with email " + email + " does not exist");
        }
    }
}
