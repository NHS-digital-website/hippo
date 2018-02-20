# What If I want to run the project for the first time

**You'll need to follow the following steps to be able to run the project**

1. Install Java SDK 1.8 (please note that the project won't work with newer versions!)
2. Install Maven
3. Set up authentication with Bloomreach experience
4. Clone the project repo from Github

## 1. Install Java SDK

1. Download and install the JavaSDK 1.8 [from here](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html).
2. Set up the `JAVA_HOME` environment variable in Terminal (or add it to your bash probile file)

		export JAVA_HOME=/Library/Java/JavaVirtualMachines/[YOUR_JDK_VERSION]/Contents/Home

## 2. Install Maven

1. Download Maven [from here](https://maven.apache.org/download.cgi) and copy the zip contents to `~/.mvn`.
2. Add the Maven binary to your `PATH`:

		export PATH=$PATH:~/.mvn/bin

## 3. Authenticate with Bloomreach Experience

1. Create settings.xml file for your Bloomreach creds:

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