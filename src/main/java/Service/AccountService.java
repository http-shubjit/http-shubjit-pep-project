package Service;

import java.sql.SQLException;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public Account registerAccount(String username, String password)   {

        if (username == ""  || password == "" || password.length() < 4) {
            return null;
        }
        if (accountDAO.getAccountByUsername(username) != null) {
            return null;
        }
        return accountDAO.createAccount(username, password);
    }

    public Account login(String username, String password) {
        return accountDAO.getAccountByUsernameAndPassword(username, password);
    }
}
