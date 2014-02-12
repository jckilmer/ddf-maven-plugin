# DDF Client Maven Plugin
The ddfclient-maven-plugin enables configuration of the ddf from within a maven project and is intended for use during integration tests.

## Current Status
Currently the plugin is in the very early stages. As it is currently designed the ddfclient-maven-plugin simply takes in several parameters and connects to the ddf over ssh to pass in commands for configuration.

## Future Plans
The plugin will eventually support more advanced configuration options, to be passed in as parameters in a projects pom. All commands passed in will be checked for errors and all outputs from commands will be used to determine status and catch errors.

## Building
### Requirements
* JDK 1.7
* Maven 3

### How To Build
Clone the project then run

```
cd ddf-maven-plugin
```

```
mvn install
```

## Usage
Place the following in the build section of your projects pom for basic usage

    <plugin>
      <groupId>org.betteridiots.ddf</groupId>
      <artifactId>ddf-maven-plugin</artifactId>
      <version>0.1-SNAPSHOT</version>
      <configuration>
        <paramsFile>path/to/ddfCommands/file</paramsFile>
        <host>localhost</host>
        <port>8101</port>
        <user>admin</user>
        <password>admin</password>
      </configuration>
      <executions>
        <execution>
          <goals>
            <goal>config-ddf</goal>
          </goals>
          <phase>pre-integration-test</phase>
        </execution>
      </executions>
    </plugin>
