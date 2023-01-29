# 🤹‍♀️ mULti4J

[![mULti4J](https://img.shields.io/badge/mULti4J-Java%20wrapper%20for%20the%20Universit%C3%A9%20de%20Lorraine%20mULti%20web%20application%20GQL%20API-blue?style=flat-square)](mULti4J)

![GitHub release (latest by date)](https://img.shields.io/github/v/release/MAXOUXAX/mULti4J?style=flat-square)
![GitHub Workflow Status](https://img.shields.io/github/actions/workflow/status/MAXOUXAX/mULti4J/deploy.yml?branch=main&style=flat-square)
![GitHub issues](https://img.shields.io/github/issues/MAXOUXAX/mULti4J?style=flat-square)
![GitHub](https://img.shields.io/github/license/MAXOUXAX/mULti4J?style=flat-square)
![GitHub last commit](https://img.shields.io/github/last-commit/MAXOUXAX/mULti4J?style=flat-square)

A Java Wrapper for the Université de Lorraine "mULti" web application GQL API (multi.univ-lorraine.fr)

## Table of contents

* [🔧 Installation](#-installation)
  * [👁️ Prerequisites](#-prerequisites)
  * [🧨 Maven](#-maven)
  * [🛞 Gradle](#-gradle)
* [📖 Wiki](#-wiki--documentation)

## 🔧 Installation

### 👁️ Prerequisites

First, you will need to authenticate to GitHub Packages to be able to download the package.
> ⚠️ Warning: This step is very important, please read the documentation carefully. You'll need to create a personal
> access token with the `read:packages` scope.

Authentication is different depending on the build tool you use, here are the links to the documentation:

* [Authentication with Maven](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry#authenticating-to-github-packages)
* [Authentication with Gradle](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-gradle-registry#authenticating-to-github-packages)

### 🧨 Maven

1. Add the following to your pom.xml repositories section:

```xml

<repository>
    <id>github</id>
    <url>https://maven.pkg.github.com/MAXOUXAX/mULti4J</url>
    <snapshots>
        <enabled>true</enabled>
    </snapshots>
</repository>
```

2. And to the dependencies section:

```xml

<dependency>
    <groupId>me.maxouxax.multi4j</groupId>
    <artifactId>multi4j</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 🛞 Gradle

1. Add the following to your build.gradle repositories section:

```groovy
repositories {
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/MAXOUXAX/mULti4J")
        credentials {
            username = project.findProperty("gpr.user") ?: System.getenv("USERNAME")
            password = project.findProperty("gpr.key") ?: System.getenv("TOKEN")
        }
    }
}
```

2. And to the dependencies section:

```groovy
dependencies {
    implementation 'me.maxouxax.multi4j:multi4j:1.0.0'
}
```

## 📖 Wiki / Documentation

The wiki / documentation is available [here](https://github.com/MAXOUXAX/mULti4J/wiki)