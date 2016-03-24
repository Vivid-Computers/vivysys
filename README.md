A sales system written in the year 2000.

Development tools needed
------------------------

You need to have:

* A java JDK, preferably version 6 or later
* Maven https://maven.apache.org/download.cgi

Optionally, download Intellij from https://www.jetbrains.com/idea/download/ and Import the project by importing `pom.xml`

Developing
----------

From your IDE, you can launch the program by running the `main` method in `vivyclient.Client`
(in IntelliJ, right click Client and select Run)

Releasing
---------

Push your changes to git hub:

    git add .
    git commit -m "Updated something"
    git push origin master

Release it:

    mvn releaser:release

Copy `target/vivysys-{version}.jar` somewhere and run it with `java -jar {jarname}`
