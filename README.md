SpringMVCTest
=============

spring mvc - pro spring 3 - chapter 17

How to test
============
In sts rightclick > run on server.
Then go to URL http://localhost/SpringMVCTest/contacts

Internationalisation
====================
Use http://localhost/SpringMVCTest/contacts/lang=zk_HK to view page in Chinese

Integration with apache tiles
=============================
WEB-INF/views => contains all components - building blocks - use to create a template/page
WEB-INF/layouts => contains template definitions (jspx files) and a layout.xml storing all the page layout definitions

Added testing with mockito
===========================
issues with the testing... because the the java-ee-webapi lacks a reference implementation.
Need to fix... find out more here : http://www.oracle.com/technetwork/articles/java/unittesting-455385.html
Need the following dependency BEFORE the javaee-..-api dependency:
<dependency>
            <groupId>org.glassfish.main.extras</groupId>
            <artifactId>glassfish-embedded-all</artifactId>
            <version>4.0</version>
            <scope>test</scope>
        </dependency>
Now unit test works but only if run from test class not if run from project, then somehow the abstract
class is searched for tests in stead of the child class with the actual tests ... TODO



