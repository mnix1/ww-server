package com.ww.repository.shop;

import com.ww.model.entity.shop.ProfileChest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileChestRepository extends CrudRepository<ProfileChest, Long> {

    Optional<ProfileChest> findById(Long id);
}
