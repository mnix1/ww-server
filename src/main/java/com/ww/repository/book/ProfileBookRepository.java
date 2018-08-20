package com.ww.repository.book;

import com.ww.model.entity.book.ProfileBook;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileBookRepository extends CrudRepository<ProfileBook, Long> {
    Optional<ProfileBook> findById(Long id);

    Optional<ProfileBook> findByIdAndProfile_Id(Long id, Long profileId);

    List<ProfileBook> findByProfile_Id(Long profileId);
}
