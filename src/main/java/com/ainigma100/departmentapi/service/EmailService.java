package com.ainigma100.departmentapi.service;


import net.sf.jasperreports.engine.JRException;

public interface EmailService {

    Boolean sendEmailWithoutAttachment();

    Boolean sendEmailWithAttachment() throws JRException;
}
