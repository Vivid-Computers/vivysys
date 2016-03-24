A sales system written in the year 2000.

Development tools needed
------------------------

You need to have:

* A java JDK, preferably version 6 or later
* Maven https://maven.apache.org/download.cgi

Optionally, download Intellij from https://www.jetbrains.com/idea/download/ and Import the project by importing `pom.xml`

From your IDE, you can launch the program by running the `main` method in `vivyclient.Client`

Building
--------

From the command line, in the root of this project folder run: `mvn releaser:release`

It will create a jar file in `target/vivysys-{version}.jar`

Run it with `java -jar {jarfile}`