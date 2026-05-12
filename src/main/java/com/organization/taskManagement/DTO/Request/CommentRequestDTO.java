package com.organization.taskManagement.DTO.Request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CommentRequestDTO {
    public String message;
    public String employeeId;
    public String name;
}
