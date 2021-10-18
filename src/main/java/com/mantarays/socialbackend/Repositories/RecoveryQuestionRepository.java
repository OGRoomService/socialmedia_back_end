package com.mantarays.socialbackend.Repositories;

import com.mantarays.socialbackend.Models.RecoveryQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecoveryQuestionRepository extends JpaRepository<RecoveryQuestion, Long>
{

}
