This project provides the core jDocBook APIs and functionality.  Generally speaking, tools are built _on top of_
jdocbook-core to allow users to leverage its usefulness.  2 such tools are the plugins for Maven and Gradle.

Building
========

jDocBook Core uses Gradle (http://gradle.org) as its build tool.  Here is a list of resources to obtain more
information about Gradle:

* The Gradle User Guide : http://gradle.org/latest/docs/userguide/userguide_single.html
* Gradle DSL Guide : http://gradle.org/latest/docs/dsl/index.html
* Additional Hibernate/Gradle information : http://community.jboss.org/wiki/GradleFAQ

Release procedure
=================

    ./gradlew clean build
    // Change "version = " to $releaseVersion
    gedit build.gradle
    git commit -m "Preparing release $releaseVersion"
    git tag -a $releaseVersion -m "Tagging $releaseVersion"
    ./gradlew clean build publish
    // Close and release staging repo
    // Change "version = " to next SNAPSHOT
    gedit build.gradle
    git commit -m "Done release $releaseVersion, prepare next SNAPSHOT"
    git push origin master
    git push origin $releaseVersion
