# Domain-Object-Filter-Sorter-Strategy-Pattern-App

### Overview

The application provides a platform to read, filter and sort a list of entities (Represented by json) from a .txt file. This is done through a **strategy pattern** whereby string values are mapped to concrete implementation of classes. These string values can be specified from config in the application.properties file contained within src/main/resource directory.

As of now - the only strategy implemented is for customer objects with fields userId, name, longitude and latitude. This strategy is selected at runtime by specifiying domainObject.name=customers in the application.properties file. 

New strategies for sorting and filtering customer objects may also be implemented through this platform. Right now the only strategy for filtering and sorting customers is to first filter by haversine distance to Intercom's Dublin office (This location is also specified in config, so if office location changed there would be no need to update source code), then sorting by userId ascending. 

File reading may also be determined by a strategy and specified by the configuration filereader.type. The only current implementation is a local file reading strategy - reading remote files will be added at a future date.  

Classes have been extensively tested, using both unit and integration tests. 

Tests are written in groovy using the Spock framework. The Spock framework allows us to write tests in a BDD style, making them easily linked to Acceptance Criteria.

A scripting language like groovy makes testing quicker and more flexible

### Running project

The project is built using Apache Maven 

Running "mvn package" in the root directory of the project will generate a /target folder containing a .jar executable file

Run java -jar {path to jar} to execute this jar.

Files to be read must be placed in the src/main/resources folder before packaging occurs and have {domainObjectName}.txt where {domainObjectName} refers to the value set for domainObject.name in the application.properties file

Once the jar is executed, the sorted filtered customers.txt file will be used to generate a output.txt file in the same directory where the .jar file lives. 

**Note that a pre-built jar built with all depedencies (including customers.txt and application.properties) has been included as part of this project. Simply clone project and run this jar if you do not seek to make any modifications to configuration or files to be read**

Of course, the project can also be imported into an IDE as a maven project and run from there (Only tested in Eclipse)


