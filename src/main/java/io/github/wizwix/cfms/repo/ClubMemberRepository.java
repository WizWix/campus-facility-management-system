package io.github.wizwix.cfms.repo;

import io.github.wizwix.cfms.model.ClubMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {}
