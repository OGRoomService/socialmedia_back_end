package com.mantarays.socialbackend.ServiceInterfaces;


import com.mantarays.socialbackend.Models.RecoveryQuestion;

public interface RecoveryQuestionServiceIntf
{
    void createRecoveryQuestion(RecoveryQuestion recoveryQuestion);
    RecoveryQuestion getRecoveryQuestion(Long recoveryQuestionId);

    //Update commands
    void updateRecoveryQuestionQuestion(RecoveryQuestion recoveryQuestion, String question);
    void updateRecoveryQuestionAnswer(RecoveryQuestion recoveryQuestion, String answer);
    void saveRecoveryQuestion(RecoveryQuestion recoveryQuestion);

    String getRecoveryQuestionAnswer(RecoveryQuestion recoveryQuestion);
}
