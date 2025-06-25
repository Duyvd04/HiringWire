package com.hiringwire.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Cv {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //    private Long userId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private String personalInfo;
    private String education;
    private String experience;
    private String skills;

}
