package study.querydsl;

import static org.assertj.core.api.Assertions.*;
import static study.querydsl.entity.QMember.*;

import javax.persistence.EntityManager;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.jpa.impl.JPAQueryFactory;

import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.Team;

@SpringBootTest
@Transactional
public class QueryDSLBasicTest {

	@Autowired
	EntityManager em;
	JPAQueryFactory queryFactory;

	@BeforeEach
	public void before() {
		queryFactory = new JPAQueryFactory(em);

		Team teamA = new Team("teamA");
		Team teamB = new Team("teamB");
		em.persist(teamA);
		em.persist(teamB);

		Member member1 = new Member("member1", 10, teamA);
		Member member2 = new Member("member2", 20, teamA);
		em.persist(member1);
		em.persist(member2);

		Member member3 = new Member("member3", 30, teamB);
		Member member4 = new Member("member4", 40, teamB);
		em.persist(member3);
		em.persist(member4);
	}

	@Test
	public void startJPQL() {
		// find member1
		String username = "member1";

		Member foundMember = em.createQuery("select m from Member m where m.username = :username", Member.class)
			.setParameter("username", username)
			.getSingleResult();

		assertThat(foundMember.getUsername()).isEqualTo(username);
	}

	@Test
	public void startQueryDSL() {
		// find member1
		String username = "member1";

		Member foundMember = queryFactory
			.select(member)
			.from(member)
			.where(member.username.eq(username))
			.fetchOne();

		assertThat(foundMember.getUsername()).isEqualTo(username);
	}

	@Test
	public void search() {
		Member foundMember = queryFactory
			.selectFrom(member)
			.where(member.username.eq("member1").and(member.age.eq(10)))
			.fetchOne();

		assertThat(foundMember.getUsername()).isEqualTo("member1");
		assertThat(foundMember.getAge()).isEqualTo(10);
	}

	@Test
	public void searchAndParam() {
		Member foundMember = queryFactory
			.selectFrom(member)
			.where(
				member.username.eq("member1"),
				member.age.eq(10)
			)
			.fetchOne();

		assertThat(foundMember.getUsername()).isEqualTo("member1");
		assertThat(foundMember.getAge()).isEqualTo(10);
	}
}
