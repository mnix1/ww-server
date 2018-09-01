package com.ww.repository.wisie;

import com.ww.model.entity.wisie.ProfileWisie;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileWisieRepository extends CrudRepository<ProfileWisie, Long> {
    List<ProfileWisie> findAllByProfile_Id(Long profileId);
    List<ProfileWisie> findAllByProfile_IdAndIdIn(Long profileId, List<Long> ids);
    Optional<ProfileWisie> findByIdAndProfile_Id(Long id, Long profileId);
    List<ProfileWisie> findAllByProfile_IdAndInTeam(Long profileId, Boolean inTeam);
}
