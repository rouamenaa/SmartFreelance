package com.esprit.microservice.pi.entites;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillMarket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 80)
    private String skill;

    @Column(nullable = false)
    private Integer demandCount;

    @Column(nullable = false)
    private Integer freelancerCount;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal avgBudget;
}
