package com.example.quizapt;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class QuizTest {
    @Test
    public void toStringWhenMaru() {
        Quiz quiz = new Quiz("問題文",true);
        assertThat(quiz.toString(), is("問題文 ○"));
    }

    @Test
    public void toStringWhenBatsu() {
        Quiz quiz = new Quiz("問題文",false);
        assertThat(quiz.toString(), is("問題文 ×"));
    }

    @Test
    public void fromStringWhenBatsu() {
        String line = "問題文1 ○";
        Quiz quiz = Quiz.fromString(line);
        assertThat(quiz.getQuestion(),is("問題文1"));
        assertThat(quiz.isAnswer(), is(true));
    }

    @Test
    public void fromStringWhenMaru() {
        String line = "問題文1 ×";
        Quiz quiz = Quiz.fromString(line);
        assertThat(quiz.getQuestion(),is("問題文1"));
        assertThat(quiz.isAnswer(), is(false));
    }
}
