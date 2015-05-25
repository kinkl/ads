package ru.kinkl.ads.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kinkl.ads.model.Advertisement;

import java.util.List;

public interface AdvertisementDao extends JpaRepository<Advertisement, Long> {
    List<Advertisement> findAll();
}
