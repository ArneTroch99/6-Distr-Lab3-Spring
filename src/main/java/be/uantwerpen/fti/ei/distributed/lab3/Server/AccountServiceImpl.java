package be.uantwerpen.fti.ei.distributed.lab3.Server;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AccountServiceImpl implements AccountService {

    private final ObjectMapper mapper = new ObjectMapper();
    private final String extension = ".json";
    private String accountDir = null;

    @Override
    public boolean init(String accountfolder) {
        File temp = new File(accountfolder);
        if (!temp.exists()) {
            temp.mkdir();
        }
        accountDir = accountfolder;
        return true;
    }

    @Override
    public boolean store(Account account) {
        try {
            Stream<Path> walk = Files.walk(Paths.get(accountDir));
            if (walk.map(x -> x.toString()).filter(f -> f.contains(account.getName() + extension)).collect(Collectors.toList()).size() == 0) {
                mapper.writeValue(new File(accountDir, account.getName() + extension), account);
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Account> getAll() {
        try (Stream<Path> walk = Files.walk(Paths.get(accountDir))) {
            List<String> results = walk.filter(Files::isRegularFile).map(x -> x.toString()).collect(Collectors.toList());
            List<Account> accounts = new ArrayList<>();
            for (String file : results) {
                accounts.add(mapper.readValue(new File(file), Account.class));
            }
            return accounts;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Account getAccountByName(String accountName) {
        try (Stream<Path> walk = Files.walk(Paths.get(accountDir))) {
            List<String> file = walk.map(x -> x.toString())
                    .filter(f -> f.contains(accountName + extension))
                    .collect(Collectors.toList());
            if (file.size() == 1) {
                return mapper.readValue(new File(file.get(0)), Account.class);
            } else {
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public synchronized String getParam(String accountName, String param) {
        try (Stream<Path> walk = Files.walk(Paths.get(accountDir))) {
            List<String> file = walk.map(x -> x.toString())
                    .filter(f -> f.contains(accountName + extension))
                    .collect(Collectors.toList());
            if (file.size() == 1) {
                Account account = mapper.readValue(new File(file.get(0)), Account.class);
                switch (param) {
                    case "balance":
                        return Integer.toString(account.getBalance());
                    case "address":
                        return account.getAddress();
                    case "children":
                        return Integer.toString(account.getChildren());
                    case "partner":
                        return account.getPartner();
                    default:
                        return null;
                }
            } else {
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean changeParam(String accountName, String param, String newParam) {
        try (Stream<Path> walk = Files.walk(Paths.get(accountDir))) {
            List<String> file = walk.map(x -> x.toString())
                    .filter(f -> f.contains(accountName + extension))
                    .collect(Collectors.toList());
            if (file.size() == 1) {
                Account account = mapper.readValue(new File(file.get(0)), Account.class);
                switch (param) {
                    case "address":
                        account.setAddress(newParam);
                        overwriteAccount(account);
                        return true;
                    case "children":
                        account.setChildren(Integer.parseInt(newParam));
                        overwriteAccount(account);
                        return true;
                    case "partner":
                        account.setPartner(newParam);
                        overwriteAccount(account);
                        return true;
                    default:
                        return false;
                }
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public synchronized boolean changeBalance(String accountName, String type, String amount) {
        try (Stream<Path> walk = Files.walk(Paths.get(accountDir))) {
            List<String> file = walk.map(x -> x.toString())
                    .filter(f -> f.contains(accountName + extension))
                    .collect(Collectors.toList());
            if (file.size() == 1) {
                Account account = mapper.readValue(new File(file.get(0)), Account.class);
                if (type.equals("plus")){
                    account.setBalance(account.getBalance() + Integer.parseInt(amount));
                    overwriteAccount(account);
                    return true;
                } else if (type.equals("min")){
                    account.setBalance(account.getBalance() - Integer.parseInt(amount));
                    overwriteAccount(account);
                    return true;

                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private synchronized void overwriteAccount(Account account) {
        try {
            File temp = new File(accountDir, account.getName() + extension);
            while (SynchronizedFileList.fileQueue.contains(temp)){
            }
            SynchronizedFileList.addToList(temp);
            mapper.writeValue(temp, account);
            SynchronizedFileList.removeFromList(temp);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
