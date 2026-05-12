
package com.organization.taskManagement.DTO.Request;

import com.organization.taskManagement.Model.EmployeeRegisterModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDTO {

    private String message;
    private String employeeId;
    private String name;


    public Object getAssignedToId() {
        return employeeId;
    }
}
