package com.organization.taskManagement.DTO.Request;

import lombok.Data;
import java.util.List;

@Data
public class TeamRequestDTO {
    private String name;
    private List<Long> memberIds;
}
