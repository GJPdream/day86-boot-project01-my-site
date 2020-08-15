package cn.itcast.service.user.impl;

import cn.itcast.dao.UserDao;
import cn.itcast.dto.cond.UserCond;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
   @Autowired
    UserDao userDao;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserCond byUser = userDao.findByUser(s);
        if (byUser==null){
            return  null;
        }
        else {
            ArrayList<GrantedAuthority> authorities  = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("USER"));/*byUser.getPremise()*/
          /*  authorities.add(new GrantedAuthority() {
                @Override
                public String getAuthority() {
                    return "USER";
                }
            });*/
            User user = new User(byUser.getUsername(), new BCryptPasswordEncoder().encode(byUser.getPassword()), true,true,true,true,authorities);
            System.out.println("管理员信息："+user.getUsername()+"   "+passwordEncoder.encode(byUser.getPassword())+"  "+user.getAuthorities());
            return user;
        }
    }
}
