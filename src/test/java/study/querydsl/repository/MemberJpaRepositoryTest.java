package study.querydsl.repository;


import static org.assertj.core.api.Assertions.*;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import study.querydsl.entity.Member;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

	@Autowired
	EntityManager em;
	@Autowired
	MemberJpaRepository memberJpaRepository;

	@Test
	public void basicTest() {
		Member member = new Member("member1", 10);
		memberJpaRepository.save(member);

		Member foundMember = memberJpaRepository.findById(member.getId())
			.orElseThrow();
		assertThat(foundMember).isEqualTo(member);

		List<Member> result1 = memberJpaRepository.findAll();
		assertThat(result1).containsExactly(member);

		List<Member> result2 = memberJpaRepository.findByUsername("member1");
		assertThat(result2).containsExactly(member);
	}

	@Test
	public void basicQueryDSLTest() {
		Member member = new Member("member1", 10);
		memberJpaRepository.save(member);

		Member foundMember = memberJpaRepository.findById(member.getId())
			.orElseThrow();
		assertThat(foundMember).isEqualTo(member);

		List<Member> result1 = memberJpaRepository.findAll_QueryDSL();
		assertThat(result1).containsExactly(member);

		List<Member> result2 = memberJpaRepository.findByUsername_QueryDSL("member1");
		assertThat(result2).containsExactly(member);
	}

}