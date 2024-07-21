package com.ainigma100.departmentapi.service;


import net.sf.jasperreports.engine.JRException;

import java.util.concurrent.CompletableFuture;

public interface EmailService {

    CompletableFuture<Boolean> sendEmailWithoutAttachment();

    CompletableFuture<Boolean> sendEmailWithAttachment() throws JRException;
}
