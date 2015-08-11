package ru.kinkl.ads.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class VoteRegistrationResultDto {

    private Boolean isNewVoteRegistered;
    private String message;

    public VoteRegistrationResultDto(Boolean isNewVoteRegistered) {
        this.isNewVoteRegistered = isNewVoteRegistered;
    }

}
