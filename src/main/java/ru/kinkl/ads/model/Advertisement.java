package ru.kinkl.ads.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "advertisements")
public class Advertisement {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "advertisements_seq")
    @SequenceGenerator(name = "advertisements_seq", sequenceName = "advertisements_seq")
    private Long id;

    @Column(name = "text")
    private String text;

    @ManyToOne
    @JoinColumn(name = "username")
    private User user;

    @Column(name = "datetime")
    private Date dateTime;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "advertisement")
    private List<Vote> votes;
}
