package com.ww.repository.shop;

import com.ww.model.constant.shop.ChestType;
import com.ww.model.entity.shop.Chest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChestRepository extends CrudRepository<Chest, Long> {
    Chest findFirstByType(ChestType chestType);
}
