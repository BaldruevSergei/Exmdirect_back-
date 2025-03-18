package org.example.exmdirect_new.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StartExamRequest {
    private Long studentId;
    private Long examId;
}
