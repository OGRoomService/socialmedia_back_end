package com.mantarays.socialbackend.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecoveryQuestion
{
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long recovery_id;

    private Long user_id;

    private String question;

    private String answer;
}
