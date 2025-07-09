# What If I want to run the project for the first time

**You'll need to follow the following steps to be able to run the project**

1. Install Java SDK 11
2. Install Maven
3. Set up authentication with Bloomreach Experience Manager 15
4. Clone the project repo from Github

## 1. Install Java SDK

1. Download and install the Java SDK 11 [from here](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) or use OpenJDK 11 via your package manager.
2. Set up the `JAVA_HOME` environment variable in Terminal (or add it to your bash profile)

		export JAVA_HOME=/Library/Java/JavaVirtualMachines/[YOUR_JDK_VERSION]/Contents/Home

## 2. Install Maven

1. Download Maven [from here](https://maven.apache.org/download.cgi) and copy the zip contents to `~/.mvn`.
2. Add the Maven binary to your `PATH`:

		export PATH=$PATH:~/.mvn/bin

## 3. Authenticate with Bloomreach Experience Manager 15

1. Create settings.xml file for your Bloomreach 15 credentials:

		$ touch ~/.m2/settings.xml

2. And add your auth details as below:

```XML
<settings>
 <servers>
   <server>
     <id>hippo-maven2-enterprise</id>
     <username>USERNAME</username>
     <password>PASSWORD</password>
   </server>
 </servers>
</settings>
```

## 4. Clone the repo from Github

	$ git clone https://github.com/NHS-digital-website/hippo.git

You're ready to run the project!