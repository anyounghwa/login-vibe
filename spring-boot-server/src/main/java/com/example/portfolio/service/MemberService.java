package com.example.portfolio.service;

import com.example.portfolio.domain.Member;
import com.example.portfolio.dto.MemberFormDto;
import com.example.portfolio.repository.MemberRepository;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Long join(MemberFormDto memberFormDto) {
        validateDuplicateLoginId(memberFormDto.getLoginId());

        String encodedPassword = passwordEncoder.encode(memberFormDto.getPassword());
        Member member = Member.createUser(
                memberFormDto.getLoginId(),
                encodedPassword,
                memberFormDto.getName()
        );

        return memberRepository.save(member).getId();
    }

    @Transactional(readOnly = true)
    public void validateDuplicateLoginId(String loginId) {
        if (memberRepository.existsByLoginId(loginId)) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }
    }

    @Transactional(readOnly = true)
    public Member findByLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
    }

    @Transactional(readOnly = true)
    public List<Member> findAll() {
        return memberRepository.findAll();
    }
}

