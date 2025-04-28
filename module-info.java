module GTPProjects {
    requires javafx.controls;
    requires javafx.graphics;
    requires java.sql;
    requires org.junit.jupiter.api;
    requires org.junit.jupiter.engine;
    requires org.junit.platform.commons;


    opens Projects.Week4.employeemanagementenhancement to org.junit.platform.commons, org.junit.jupiter.api, org.junit.jupiter.engine;


    exports Projects.Week2.bankaccount.services;
    exports Projects.Week2.bankaccount.app;
    exports Projects.Week2.bankaccount.model;
    exports Projects.Week3.employeemanagement.database;
    exports Projects.Week3.employeemanagement.app;
    exports Projects.Week3.employeemanagement.model;
    exports Projects.Week3.employeemanagement.services;
    exports Projects.Week4.employeemanagementenhancement.app;
    opens Projects.Week4.employeemanagementenhancement.app to org.junit.jupiter.api, org.junit.jupiter.engine, org.junit.platform.commons;
    exports Projects.Week4.employeemanagementenhancement.database;
    opens Projects.Week4.employeemanagementenhancement.database to org.junit.jupiter.api, org.junit.jupiter.engine, org.junit.platform.commons;
    exports Projects.Week4.employeemanagementenhancement.model;
    opens Projects.Week4.employeemanagementenhancement.model to org.junit.jupiter.api, org.junit.jupiter.engine, org.junit.platform.commons;
    exports Projects.Week4.employeemanagementenhancement.services;
    opens Projects.Week4.employeemanagementenhancement.services to org.junit.jupiter.api, org.junit.jupiter.engine, org.junit.platform.commons;


}