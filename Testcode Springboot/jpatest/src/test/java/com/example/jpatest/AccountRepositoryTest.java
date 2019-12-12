package com.example.jpatest;

import com.example.jpatest.Entity.Account;
import com.example.jpatest.Repository.AccountRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AccountRepositoryTest {

    @Autowired
    AccountRepository accountRepository;

    @Test
    public void join(){
        Account account = new Account();
        account.setUsername("tedigom");
        account.setPassword("1234");

        accountRepository.save(account);

        List<Account> getAccounts = accountRepository.findAll();

        for(Account accountInfor : getAccounts){
            Assertions.assertThat(accountInfor.getUsername().equals("tedigom"));
            Assertions.assertThat(accountInfor.getPassword().equals("1234"));
        }
    }

}
