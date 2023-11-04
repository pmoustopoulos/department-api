package com.ainigma100.departmentapi.controller;

import com.ainigma100.departmentapi.dto.APIResponse;
import com.ainigma100.departmentapi.service.EmailService;
import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RequestMapping("/api/v1/emails")
@RestController
public class EmailController {

    private final EmailService emailService;
    private static final String SUCCESS = "Success";

    @GetMapping
    public ResponseEntity<APIResponse<Boolean>> sendEmailWithoutAttachment() {

        Boolean answer = emailService.sendEmailWithoutAttachment();

        // Builder Design pattern
        APIResponse<Boolean> responseDTO = APIResponse
                .<Boolean>builder()
                .status(SUCCESS)
                .results(answer)
                .build();

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/with-attachment")
    public ResponseEntity<APIResponse<Boolean>> sendEmailWithAttachment() throws JRException {

        Boolean answer = emailService.sendEmailWithAttachment();

        // Builder Design pattern
        APIResponse<Boolean> responseDTO = APIResponse
                .<Boolean>builder()
                .status(SUCCESS)
                .results(answer)
                .build();

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

}
