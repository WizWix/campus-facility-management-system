package io.github.wizwix.cfms.global.config.dev;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.github.wizwix.cfms.global.config.dev.base.BaseDevLoader;
import io.github.wizwix.cfms.global.config.dev.base.EntityReferenceDeserializer;
import io.github.wizwix.cfms.model.User;
import io.github.wizwix.cfms.model.club.Club;
import io.github.wizwix.cfms.model.club.ClubMember;
import io.github.wizwix.cfms.model.enums.ClubMemberStatus;
import io.github.wizwix.cfms.model.enums.ClubRole;
import io.github.wizwix.cfms.model.enums.ClubStatus;
import io.github.wizwix.cfms.repo.UserRepository;
import io.github.wizwix.cfms.repo.club.ClubMemberRepository;
import io.github.wizwix.cfms.repo.club.ClubRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.LongAdder;

@Component
@Profile("dev")
@Slf4j
public class ClubDevLoader extends BaseDevLoader<Club> {
  private final ClubRepository clubRepo;
  private final ClubMemberRepository clubMemberRepo;
  private final UserRepository userRepo;

  public ClubDevLoader(ResourceLoader loader, ObjectMapper mapper, ClubRepository clubRepo, ClubMemberRepository clubMemberRepo, UserRepository userRepo) {
    super(loader, mapper, Club.class, "data/dev/clubs.jsonc");
    this.clubRepo = clubRepo;
    this.clubMemberRepo = clubMemberRepo;
    this.userRepo = userRepo;
  }

  @Override
  protected void configureMapper(ObjectMapper mapper) {
    SimpleModule module = new SimpleModule();
    module.addDeserializer(User.class, new EntityReferenceDeserializer<>(
        president -> userRepo.findByNumber(president)
            .orElseThrow(() -> new RuntimeException("User[" + president + "] not found"))
    ));
    mapper.registerModule(module);
  }

  @Override
  public void load() {
    LongAdder adder = new LongAdder();
    processItems(club -> {
      if (!clubRepo.existsBySlug(club.getSlug())) {
        clubRepo.save(club);

        // APPROVED 동아리는 회장을 ClubMember(ROLE_PRESIDENT, APPROVED)로 자동 등록
        // — ClubService.updateClubStatus() 승인 로직과 동일한 패턴
        if (club.getStatus() == ClubStatus.APPROVED && club.getPresident() != null) {
          if (!clubMemberRepo.existsByUserAndClub(club.getPresident(), club)) {
            ClubMember president = new ClubMember();
            president.setClub(club);
            president.setUser(club.getPresident());
            president.setRole(ClubRole.ROLE_PRESIDENT);
            president.setStatus(ClubMemberStatus.APPROVED);
            president.setJoinedAt(LocalDateTime.now());
            clubMemberRepo.save(president);
          }
        }

        adder.increment();
      }
    });
    log.info("Dev Profile: Loaded {} clubs", adder.sum());
  }

  @Override
  public void unload() {
    LongAdder adder = new LongAdder();
    processItems(club -> clubRepo.findBySlug(club.getSlug()).ifPresent(existing -> {
      clubRepo.delete(existing);
      adder.increment();
    }));
    log.info("Dev Profile: Unloaded {} clubs", adder.sum());
  }
}