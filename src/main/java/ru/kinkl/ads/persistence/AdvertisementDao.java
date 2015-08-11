package ru.kinkl.ads.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.kinkl.ads.model.Advertisement;

import java.util.List;

public interface AdvertisementDao extends JpaRepository<Advertisement, Long> {
    List<Advertisement> findAll();

    @Query(value = "SELECT CAST(a.id as INTEGER), a.text, u.username, a.datetime, CAST(SUM(v.vote_value) as INTEGER) AS total " +
                   "FROM votes v " +
                        "INNER JOIN advertisements a ON v.advertisement_id = a.id " +
                        "INNER JOIN users u ON a.username = u.username " +
                   "GROUP BY a.id, a.text, u.username, a.datetime " +
                   "ORDER BY total DESC " +
                   "LIMIT 3", nativeQuery = true)
    List<Object[]> getTopRated();

    Advertisement findById(Long id);
}
