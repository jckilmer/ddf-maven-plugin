# DDF Maven Plugin
The ddf-maven-plugin enables configuration of the ddf from within a maven project and is intended for use during integration tests.

[![Build Status](https://travis-ci.org/betteridiots/ddf-maven-plugin.png?branch=master)](https://travis-ci.org/betteridiots/ddf-maven-plugin)

# Issue Tracking
[JIRA](http://issues.betteridiots.org/browse/DMP)

<script type="text/javascript" src="http://issues.betteridiots.org/s/d41d8cd98f00b204e9800998ecf8427e/en_USfldw7o-1988229788/6252/12/1.4.7/_/download/batch/com.atlassian.jira.collector.plugin.jira-issue-collector-plugin:issuecollector/com.atlassian.jira.collector.plugin.jira-issue-collector-plugin:issuecollector.js?collectorId=3653817d"></script>


## Current Status
Currently the plugin is in the very early stages. As it is currently designed the ddf-maven-plugin simply takes in several parameters and connects to the ddf over ssh to pass in commands for configuration.

## Future Plans
The plugin will eventually support more advanced configuration options, to be passed in as parameters in a projects pom. All commands passed in will be checked for errors and all outputs from commands will be used to determine status and catch errors.

## Building
### Requirements
* JDK 1.7
* Maven 3

### How To Build

```
git clone https://github.com/betteridiots/ddf-maven-plugin.git
cd ddf-maven-plugin
mvn install -DskipTests
```
Note: skipping tests is currently necessary due to the tests expecting ddf to be running locally

## Usage
Place the following in the build section of your projects pom for basic usage against a single ddf host

    <plugin>
      <groupId>org.betteridiots.ddf</groupId>
      <artifactId>ddf-maven-plugin</artifactId>
      <version>0.1.0-SNAPSHOT</version>
      <configuration>
        <host>localhost</host>
        <port>8101</port>
        <user>admin</user>
        <password>admin</password>
      </configuration>
    </plugin>

### config-ddf goal
    <execution>
      <id>configure-something</id>
      <phase>pre-integration-test</phase>
      <goals>
        <goal>config-ddf</goal>
      </goals>
      <configuration>
        <config>ddf.platform.config</config>
        <props>
          <prop>host localhost</prop>
          <prop>port 8181</prop>
          <prop>http://</prop>
          <prop>id ddf.local</prop>
        </props>
      </configuration>
    </execution>

### install-features goal
    <execution>
      <id>install-some-features</id>
      <phase>pre-integration-test</phase>
      <goals>
        <goal>install-features</goal>
      </goal>
      <configuration>
        <features>
          <feature>catalog-schematron-plugin</feature>
          <feature>catalog-transformer-tika</feature>
        </features>
      </configuration>
    </execution>

### uninstall-features goal
    <execution>
      <id>uninstall-some-features</id>
      <phase>pre-integration-test</phase>
      <goals>
        <goal>uninstall-features</goal>
      </goal>
      <configuration>
        <features>
          <feature>catalog-schematron-plugin</feature>
          <feature>catalog-transformer-tika</feature>
        </features>
      </configuration>
    </execution>
