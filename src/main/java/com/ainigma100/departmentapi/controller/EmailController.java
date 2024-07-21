package com.ainigma100.departmentapi.controller;

import com.ainigma100.departmentapi.dto.APIResponse;
import com.ainigma100.departmentapi.enums.Status;
import com.ainigma100.departmentapi.service.EmailService;
import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
@RequestMapping("/api/v1/emails")
@RestController
public class EmailController {

    private final EmailService emailService;


    @GetMapping
    public CompletableFuture<ResponseEntity<APIResponse<Boolean>>> sendEmailWithoutAttachment() {

        return emailService.sendEmailWithoutAttachment().thenApply(answer -> {

            // Builder Design pattern
            APIResponse<Boolean> responseDTO = APIResponse.<Boolean>builder()
                    .status(Status.SUCCESS.getValue())
                    .results(answer)
                    .build();

            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        });
    }

    @GetMapping("/with-attachment")
    public CompletableFuture<ResponseEntity<APIResponse<Boolean>>> sendEmailWithAttachment() throws JRException {

        return emailService.sendEmailWithAttachment().thenApply(answer -> {

            // Builder Design pattern
            APIResponse<Boolean> responseDTO = APIResponse.<Boolean>builder()
                    .status(Status.SUCCESS.getValue())
                    .results(answer)
                    .build();

            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        });
    }

}
