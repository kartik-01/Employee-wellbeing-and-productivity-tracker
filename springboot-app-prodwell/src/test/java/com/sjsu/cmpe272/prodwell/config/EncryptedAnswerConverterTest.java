package com.sjsu.cmpe272.prodwell.config;

import com.sjsu.cmpe272.prodwell.entity.PersonalityAnswer.QuestionAnswer;
import com.sjsu.cmpe272.prodwell.security.EncryptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EncryptedAnswerConverterTest {

    @Mock
    private EncryptionService encryptionService;

    private EncryptedAnswerReadingConverter readingConverter;
    private EncryptedAnswerWritingConverter writingConverter;

    @BeforeEach
    void setUp() {
        readingConverter = new EncryptedAnswerReadingConverter(encryptionService);
        writingConverter = new EncryptedAnswerWritingConverter(encryptionService);
    }

    @Test
    void shouldConvertEncryptedToAnswers() {
        String json = "[{\"questionId\":\"Q1\",\"answer\":[\"A1\"]}]";
        when(encryptionService.decrypt(anyString())).thenReturn(json);

        List<QuestionAnswer> result = readingConverter.convert("encrypted");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Q1", result.get(0).getQuestionId());
    }

    @Test
    void shouldConvertAnswersToEncrypted() {
        List<QuestionAnswer> answers = Arrays.asList(
                new QuestionAnswer("Q1", Arrays.asList("A1"))
        );
        when(encryptionService.encrypt(anyString())).thenReturn("encrypted");

        String result = writingConverter.convert(answers);
        assertEquals("encrypted", result);
    }
}