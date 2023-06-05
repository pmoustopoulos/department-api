package com.ainigma100.departmentapi.utils.email;

import jakarta.activation.DataSource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequest {

    private String from;
    private List<String> toRecipients;
    private List<String> ccRecipients;
    private String subject;
    private String emailBody;
    private Map<String, DataSource> attachments;
    private Map<String, Object> dynamicVariables;
    private Map<String, String> imagePaths;

}
