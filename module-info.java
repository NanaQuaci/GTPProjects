module GTPProjects {
    requires javafx.controls;
    requires javafx.graphics;
    requires java.sql;
    requires org.junit.jupiter.api;
    requires org.junit.jupiter.engine;
    requires org.junit.platform.commons;


    opens Projects.Week4.employeemanagementenhancement to org.junit.platform.commons, org.junit.jupiter.api, org.junit.jupiter.engine;

    exports Projects.Week2.bankaccount;
    exports Projects.Week3.employeemanagement;
    exports Projects.Week4.employeemanagementenhancement;


}