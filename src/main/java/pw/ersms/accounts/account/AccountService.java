package pw.ersms.accounts.account;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public Object getAccount() {
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        // TODO: Implement this method
        // resp.put("data", ...);
        resp.put("message", "Successful fetching data");
        return resp;
    }
}
