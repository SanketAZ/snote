package com.sxy.snote.service.impl;


import com.sxy.snote.model.Client;
import com.sxy.snote.model.MyUserDetails;
import com.sxy.snote.repository.ClientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private ClientRepo clientRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Client> op=clientRepo.findByUsername(username);

        if(op.isEmpty())
        {
            System.out.println("User not found1"+ username);
            throw new UsernameNotFoundException("Username not found");
        }
        return new MyUserDetails(op.get());
    }
}
