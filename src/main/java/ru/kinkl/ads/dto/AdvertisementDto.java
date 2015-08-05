package ru.kinkl.ads.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdvertisementDto {
    private Long id;
    private String text;
    private String username;
    private Date dateTime;
}
