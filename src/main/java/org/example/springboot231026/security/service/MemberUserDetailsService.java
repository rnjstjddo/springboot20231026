package org.example.springboot231026.security.service;

import org.example.springboot231026.domain.member.Member;
import org.example.springboot231026.domain.member.MemberRepository;
import org.example.springboot231026.dto.member.MemberDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.Optional;

import static org.example.springboot231026.dto.member.MemberDTO.setAuthorities;


@Service
public class MemberUserDetailsService implements UserDetailsService {

    @Autowired
    private MemberRepository mr;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //System.out.println("security-service MemberUserDetailsService 오버라이딩 loadUserByUsername() 진입 파라미터 username -> "+ username );

        Optional<Member> o = mr.findByEmail(username);

        if(o.isPresent()) {
            //System.out.println("security-service MemberUserDetailsService 오버라이딩 loadUserByUsername() 진입 파라미터 username으로 DB엔티티 가져옴 Member 존재할경우 진입 ->" + o.get().toString());

            Member entity = o.get();

            //System.out.println("security-service MemberUserDetailsService 오버라이딩 loadUserByUsername() 진입 " +
                    //" Member엔티티가 존재하고 fromSocial 값이 true/false인 경우 진입 entity -> " + entity.toString());

            MemberDTO dto = new MemberDTO(
                    entity.getEmail(),
                    entity.getPassword(),
                    entity.getFromSocial(),
                    setAuthorities(entity.getRole().toString())

            );
            dto.setName(entity.getName());
            dto.setModifiedDate(entity.getModifiedDate());
            dto.setCreatedDate(entity.getCreatedDate());
            dto.setAttrs(null);
            dto.setRole(entity.getRole());

            //System.out.println("security-service MemberUserDetailsService 오버라이딩 loadUserByUsername() 진입 MemberDTO(UserDetails반환타입) ->" + dto.toString());

            return dto;
        }
        return null;
    }
}
