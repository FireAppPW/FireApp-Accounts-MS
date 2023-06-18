package pw.ersms.accounts.account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pw.ersms.accounts.role.Role;
import pw.ersms.accounts.role.RoleRepository;
import pw.ersms.accounts.role.RoleService;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private AccountService accountService;
    @InjectMocks
    private RoleService roleService;

    private Role role;
    private Account account;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        role = new Role(10, "x", Collections.singleton(account));
        account = new Account(0, "x", "x", "x", null, 8, 8, "x", role, "x", "x", "x", "x", "x", true, "x", false);
    }

    @Test
    void get() {
        when(accountRepository.findAll()).thenReturn(Arrays.asList(account));
        assertNotNull(accountService.get());
    }

    @Test
    void getAccountById() {
        when(accountRepository.findById(account.getId())).thenReturn(Optional.ofNullable(account));
        assertNotNull(accountService.getAccountById(account.getId()));
    }

    @Test
    void create() {
        when(accountRepository.findAccountByEmail(account.getEmail())).thenReturn(Optional.empty());
        when(roleRepository.findById(role.getId())).thenReturn(Optional.of(role));
        when(accountRepository.save(account)).thenReturn(account);

        // Create an instance of the service and inject the mock repositories
        AccountService accountService = new AccountService(accountRepository, roleRepository);

        // Call the method under test
        Object result = accountService.create(account);

        assertNotNull(result);
        assertEquals(account, result);
    }

    @Test
    void deleteAccount() {
        when(accountRepository.existsById(account.getId())).thenReturn(true);
        assertDoesNotThrow(() -> accountService.deleteAccount(account.getId()));
    }

    @Test
    void updateAccount() {
        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
        when(accountRepository.findAccountByEmail(account.getEmail())).thenReturn(Optional.empty());
        when(roleRepository.findById(role.getId())).thenReturn(Optional.of(role));
        when(accountRepository.save(account)).thenReturn(account);

        // Create an instance of the service and inject the mock repositories
        AccountService accountService = new AccountService(accountRepository, roleRepository);

        // Call the method under test
        Object result = accountService.updateAccount(account.getId(), account);

        assertNotNull(result);
        assertEquals(account, result);
    }
}