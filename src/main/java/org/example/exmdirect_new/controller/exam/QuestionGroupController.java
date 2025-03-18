package org.example.exmdirect_new.controller.exam;

import org.example.exmdirect_new.entity.exam.QuestionGroup;
import org.example.exmdirect_new.service.QuestionGroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/question-groups")
@CrossOrigin(origins = "*")
public class QuestionGroupController {

    private final QuestionGroupService questionGroupService;

    public QuestionGroupController(QuestionGroupService questionGroupService) {
        this.questionGroupService = questionGroupService;
    }

    @PostMapping
    public ResponseEntity<QuestionGroup> createQuestionGroup(@RequestBody QuestionGroup questionGroup) {
        return ResponseEntity.ok(questionGroupService.createQuestionGroup(questionGroup));
    }
}
