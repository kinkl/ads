package ru.kinkl.ads.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "votes")
public class Vote {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "votes_seq")
    @SequenceGenerator(name = "votes_seq", sequenceName = "votes_seq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "advertisement_id")
    private Advertisement advertisement;

    @ManyToOne
    @JoinColumn(name = "username")
    private User user;

    @Column(name = "vote_value")
    private Integer value;
}
