package ru.kinkl.ads.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.kinkl.ads.model.Advertisement;
import ru.kinkl.ads.model.User;
import ru.kinkl.ads.model.Vote;

public interface VoteDao extends JpaRepository<Vote, Long> {
    Vote findByAdvertisementAndUser(Advertisement advertisement, User user);

    @Query("SELECT SUM(v.value) FROM Vote v WHERE v.advertisement = :avertisement")
    Integer getAdvertisementRate(@Param("avertisement") Advertisement advertisement);
}
