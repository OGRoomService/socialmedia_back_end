package com.mantarays.socialbackend.Services;

import com.mantarays.socialbackend.Models.RecoveryQuestion;
import com.mantarays.socialbackend.Repositories.RecoveryQuestionRepository;
import com.mantarays.socialbackend.ServiceInterfaces.RecoveryQuestionServiceIntf;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RecoveryQuestionService implements RecoveryQuestionServiceIntf
{
    private final RecoveryQuestionRepository recoveryQuestionRepository;

    @Override
    public void createRecoveryQuestion(RecoveryQuestion recoveryQuestion)
    {
        recoveryQuestionRepository.save(recoveryQuestion);
    }

    @Override
    public RecoveryQuestion getRecoveryQuestion(Long recoveryQuestionId)
    {
        return recoveryQuestionRepository.getById(recoveryQuestionId);
    }

    @Override
    public void saveRecoveryQuestion(RecoveryQuestion recoveryQuestion)
    {
        recoveryQuestionRepository.save(recoveryQuestion);
    }

    @Override
    public void updateRecoveryQuestionQuestion(RecoveryQuestion recoveryQuestion, String question)
    {
        recoveryQuestion.setQuestion(question);
        saveRecoveryQuestion(recoveryQuestion);
    }

    @Override
    public void updateRecoveryQuestionAnswer(RecoveryQuestion recoveryQuestion, String answer)
    {
        recoveryQuestion.setAnswer(answer);
        saveRecoveryQuestion(recoveryQuestion);
    }

    @Override
    public String getRecoveryQuestionAnswer(RecoveryQuestion recoveryQuestion)
    {
        return recoveryQuestion.getAnswer();
    }
}
