package com.example.jpatest;


import com.example.jpatest.Entity.Account;
import com.example.jpatest.Entity.Post;
import com.example.jpatest.Repository.AccountRepository;
import com.example.jpatest.Repository.PostRepository;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;

    @Autowired
    AccountRepository accountRepository;

    @Test
    public void createPost(){
        Account account = new Account();
        account.setUsername("tedigom");
        account.setPassword("1234");

        accountRepository.save(account);

        Post post = new Post();
        post.setTitle("Spring data JPA TEST");
        post.setContents("test start");
        post.setAccounts(account);

        postRepository.save(post);

        List<Post> getPosts = postRepository.findAll();

        for(Post post1 : getPosts){
            Assertions.assertThat(post1.getAccounts().getUsername().equals("tedigo"));
            Assertions.assertThat(post1.getAccounts().getPassword().equals("123"));
            Assertions.assertThat(post1.getTitle().equals("Spring data JPA TES"));
            Assertions.assertThat(post1.getContents().equals("test star"));

        }
    }

}
