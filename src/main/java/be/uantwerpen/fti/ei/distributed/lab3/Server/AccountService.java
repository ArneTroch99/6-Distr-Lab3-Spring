package be.uantwerpen.fti.ei.distributed.lab3.Server;

import java.util.List;

public interface AccountService {

    boolean init(String accountfolder);

    boolean store(Account account);

    List<Account> getAll();

    Account getAccountByName(String accountName);

    String getParam(String accountName, String param);

    boolean changeParam(String accountName, String param, String newParam);

    boolean changeBalance(String accountName, String type, String amount);
}
