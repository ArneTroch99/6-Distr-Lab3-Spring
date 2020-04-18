package be.uantwerpen.fti.ei.distributed.lab3.Server;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
public class HttpController {

    private AccountService accountService;

    @PostConstruct
    public void setup() {
        accountService = new AccountServiceImpl();
        accountService.init("Accounts");
    }

    @RequestMapping(value = "/getAccount", method = RequestMethod.GET)
    public Account getAccountInfo(@RequestParam("account") String accountName) {
        return accountService.getAccountByName(accountName);
    }

    @RequestMapping(value = "/getAccountInfo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public StringResponse getAccountParam(@RequestParam("account") String accountName,
                                          @RequestParam("param") String param) {
        return new StringResponse(accountService.getParam(accountName, param));
    }

    @RequestMapping(value = "/getAllAccounts", method = RequestMethod.GET)
    public List<Account> getAllAccountsInfo() {
        return accountService.getAll();
    }

    @RequestMapping(value = "/addAccount", method = RequestMethod.POST)
    public ResponseEntity addAccountToDB(@RequestParam("name") String name,
                                         @RequestParam("balance") int balance,
                                         @RequestParam(value = "address", defaultValue = "") String address,
                                         @RequestParam(value = "children", defaultValue = "0") int children,
                                         @RequestParam(value = "partner", defaultValue = "") String partner) {
        Account account = new Account(name, balance, address, children, partner);
        System.out.println(account.toString());
        if (accountService.store(account)) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("An account with that name already exists");
        }
    }

    @RequestMapping(value = "/changeAccount", method = RequestMethod.PUT)
    public ResponseEntity changeAccount(@RequestParam("account") String accountName,
                                        @RequestParam("param") String param,
                                        @RequestParam("newValue") String newValue){
        if (accountService.changeParam(accountName, param, newValue)){
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error occurred");
        }
    }

    @RequestMapping(value = "/changeBalance", method = RequestMethod.PUT)
    public ResponseEntity changeBalance(@RequestParam("account") String accountName,
                                        @RequestParam("type") String type,
                                        @RequestParam("amount") String amount){
        if (accountService.changeBalance(accountName, type, amount)){
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error occurred");
        }
    }
}
