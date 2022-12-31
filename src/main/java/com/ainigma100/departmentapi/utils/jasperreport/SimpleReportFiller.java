package com.ainigma100.departmentapi.utils.jasperreport;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Map;

@Component
public class SimpleReportFiller {


    public JasperPrint prepareReport(String templateFileName, Map<String, Object> parameters, JRBeanCollectionDataSource dataSource) throws JRException {

        JasperReport report = compileReport(templateFileName);
        JasperPrint print = fillReport(report, parameters, dataSource);
        return print;

    }

    public JasperReport compileReport(String templateFileName) throws JRException {

        InputStream reportStream = getClass().getResourceAsStream("/".concat(templateFileName).concat(".jrxml"));
        JasperReport report = JasperCompileManager.compileReport(reportStream);
        return report;

    }

    public JasperPrint fillReport(JasperReport report, Map<String, Object> parameters, JRBeanCollectionDataSource dataSource) throws JRException {

        JasperPrint print = JasperFillManager.fillReport(report, parameters, dataSource);
        return print;

    }


}
