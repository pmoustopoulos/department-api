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
        return fillReport(report, parameters, dataSource);

    }

    public JasperReport compileReport(String templateFileName) throws JRException {

        InputStream reportStream = getClass().getResourceAsStream("/".concat(templateFileName).concat(".jrxml"));
        return JasperCompileManager.compileReport(reportStream);

    }

    public JasperPrint fillReport(JasperReport report, Map<String, Object> parameters, JRBeanCollectionDataSource dataSource) throws JRException {

        return JasperFillManager.fillReport(report, parameters, dataSource);

    }


}
