package pw.ersms.accounts.account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pw.ersms.accounts.role.Role;
import pw.ersms.accounts.role.RoleRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private AccountService accountService;

    private Role role1, role2, role3;
    private Account account1, account2, account3;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        role1 = new Role(10, "x", Collections.singleton(account1));
        role2 = new Role(11, "x", Collections.singleton(account2));
        role3 = new Role(12, "x", Collections.singleton(account3));
        account1 = new Account(0, "x", "x", "x", null, 8, 8, "x", role1, "x", "x", "x", "x", "x", true, "x", false);
        account2 = new Account(1, "x", "x", "x", null, 9, 8, "x", role2, "x", "x", "x", "x", "x", true, "x", false);
        account3 = new Account(2, "x", "x", "x", null, 10, 8, "x", role3, "x", "x", "x", "x", "x", true, "x", false);
    }

    @Test
    void get() {
        Integer departmentId = 1;
        List<Account> accounts = Arrays.asList(account1, account2, account3);

        when(accountRepository.findAll()).thenReturn(accounts);

        List<Account> result = accountService.get(departmentId);

        List<Account> expectedAccounts = accounts.stream()
                .filter(account -> account.getFireDepartmentId().equals(departmentId))
                .collect(Collectors.toList());

        assertEquals(expectedAccounts.size(), result.size());
        assertEquals(expectedAccounts, result);
    }

    @Test
    void getAccountById() {
        when(accountRepository.findById(account1.getId())).thenReturn(Optional.ofNullable(account1));
        assertNotNull(accountService.getAccountById(account1.getId()));
    }

    @Test
    void create() {
        when(accountRepository.findAccountByEmail(account1.getEmail())).thenReturn(Optional.empty());
        when(roleRepository.findById(role1.getId())).thenReturn(Optional.of(role1));
        when(accountRepository.save(account1)).thenReturn(account1);

        // Create an instance of the service and inject the mock repositories
        AccountService accountService = new AccountService(accountRepository, roleRepository);

        // Call the method under test
        Object result = accountService.create(account1);

        assertNotNull(result);
        assertEquals(account1, result);
    }

    @Test
    void deleteAccount() {
        when(accountRepository.existsById(account1.getId())).thenReturn(true);
        assertDoesNotThrow(() -> accountService.deleteAccount(account1.getId()));
    }

    @Test
    void updateAccount() {
        when(accountRepository.findById(account1.getId())).thenReturn(Optional.of(account1));
        when(accountRepository.findAccountByEmail(account1.getEmail())).thenReturn(Optional.empty());
        when(roleRepository.findById(role1.getId())).thenReturn(Optional.of(role1));
        when(accountRepository.save(account1)).thenReturn(account1);

        // Create an instance of the service and inject the mock repositories
        AccountService accountService = new AccountService(accountRepository, roleRepository);

        // Call the method under test
        Object result = accountService.updateAccount(account1.getId(), account1);

        assertNotNull(result);
        assertEquals(account1, result);
    }

    @Test
    void findAccountByEmail() {
        when(accountRepository.findAccountByEmail(account1.getEmail())).thenReturn(Optional.of(account1));

        Account result = accountService.findAccountByEmail(account1.getEmail());

        assertEquals(account1, result);
    }
}