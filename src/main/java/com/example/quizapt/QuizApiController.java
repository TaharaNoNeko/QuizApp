package com.example.quizapt;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
public class QuizApiController {
    // Class Quiz
    private List<Quiz> quizzes = new ArrayList<>();
    private QuizFileDao quizfileDao = new QuizFileDao();

    @GetMapping("/quiz")
    public Quiz quiz(){
        int index = new Random().nextInt(quizzes.size());

        return quizzes.get(index);
    }

    /**
     * show method
     * @return quizzes
     */
    @GetMapping("/show")
    public List<Quiz> show() {
        return quizzes;
    }

    /**
     * create method
     * @param question
     * @param answer
     */
    @PostMapping("/create")
    public void create(@RequestParam String question, @RequestParam boolean answer) {
        Quiz quiz = new Quiz(question, answer);
        quizzes.add(quiz);
    }

    //check method
    //@param question
    //@param answer
    //return true/false(String)
    @GetMapping("/check")
    public String check(@RequestParam String question,@RequestParam boolean answer){
        // check question true or false
        // 指定されたquestionを登録済みのクイズから検索する
        for (Quiz quiz:quizzes) {
            // もしクイズが見つかったら
            if (quiz.getQuestion().equals(question)) {
                if (quiz.isAnswer() == answer) {
                    // 登録されているanswerと回答して渡ってきたanswerが一致している場合、「正解」と返却する
                    return "正解!";
                } else {
                    //もし一致していなければ「不正解」と返却する
                    return "不正解!";
                }
            }
        }
        //クイズが見つからなかった場合は、「問題がありません」と返却する
        return "問題がありません!";
    }

    @PostMapping("/save")
    public String save() {
        try {
            quizfileDao.write(quizzes);
            return "Saved File";
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed Saved File";
        }
    }

    @GetMapping("/load")
    public String load() {
        try {
            quizzes = quizfileDao.read();
            return "Read File";
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed Read File";
        }
    }
}
