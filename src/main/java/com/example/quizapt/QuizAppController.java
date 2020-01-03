package com.example.quizapt;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Controller
@RequestMapping("page")
public class QuizAppController {
    // Class Quiz
    private List<Quiz> quizzes = new ArrayList<>();
    private QuizFileDao quizfileDao = new QuizFileDao();

    /**
     * quiz metho
     * @param model
     * @return
     */
    @GetMapping("/quiz")
    public String quiz(Model model){
        int index = new Random().nextInt(quizzes.size());

        model.addAttribute("quiz",quizzes.get(index));
        return "quiz";
    }

    /**
     * show method
     * @return quizzes
     */
    @GetMapping("/show")
    public String show(Model model) {
        model.addAttribute("quizzes",quizzes);
        return "list";
    }

    /**
     * create method
     * @param question
     * @param answer
     */
    @PostMapping("/create")
    public String create(@RequestParam String question, @RequestParam boolean answer) {
        Quiz quiz = new Quiz(question, answer);
        quizzes.add(quiz);

        return "redirect:/page/show";
    }

    /**
     * check method
     * @param model
     * @param question
     * @param answer
     * @return
     */
    @GetMapping("/check")
    public String check(Model model,@RequestParam String question,@RequestParam boolean answer){
        // 問題の回答が正解か不正解かをチェックする
        // 指定されたquestionを登録済みのクイズから検索する
        for (Quiz quiz:quizzes) {
            // もしクイズが見つかったら
            if (quiz.getQuestion().equals(question)) {
                model.addAttribute("quiz",quiz);
                if (quiz.isAnswer() == answer) {
                    // 登録されているanswerと回答して渡ってきたanswerが一致している場合、「正解」と返却する
                    model.addAttribute("result","正解！");
                } else {
                    //もし一致していなければ「不正解」と返却する
                    model.addAttribute("result","不正解！");
                }
            }
        }
        //クイズが見つからなかった場合は、「問題がありません」と返却する
        return "answer";
    }

    /**
     * save method
     * @param attributes
     * @return
     */
    @PostMapping("/save")
    public String save(RedirectAttributes attributes) {
        try {
            quizfileDao.write(quizzes);
            attributes.addFlashAttribute("successMessage","ファイルに保存しました");
        } catch (IOException e) {
            e.printStackTrace();
            attributes.addFlashAttribute("errorMessage","ファイルの保存に失敗しました");
        }
        return "redirect:/page/show";
    }

    /**
     * load method
     * @param attributes
     * @return
     */
    @GetMapping("/load")
    public String load(RedirectAttributes attributes) {
        try {
            quizzes = quizfileDao.read();
            attributes.addFlashAttribute("successMessage","ファイルの読み込みに成功しました");
        } catch (IOException e) {
            e.printStackTrace();
            attributes.addFlashAttribute("errorMessage","ファイルの読み込みに失敗しました");
        }
        return "redirect:/page/show";
    }
}
