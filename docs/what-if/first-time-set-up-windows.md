# What if I want to run the project for the first time on Windows

This project's development environment is designed to run on Ubuntu. For Windows users, you must use Windows Subsystem for Linux 2 (WSL 2) with Ubuntu.

## Requirements

- Windows 10 version 2004 or higher (Build 19041 or higher)
- WSL 2 installed with Ubuntu distribution
- Ubuntu on WSL 2 (either Ubuntu 20.04 LTS or 22.04 LTS recommended)

Please note:
- Native Windows development is not supported
- Other Linux distributions under WSL 2 are not supported
- WSL 1 is not supported

## Set up guide
This guide will work you through the process of setting up WSL 2 and Ubuntu on Windows, then installing the project's development environment, and hooking up the IntelliJ IDEA (the project's recommended IDE).

### 1. Install WSL 2 & Ubuntu

Open PowerShell **as Administrator** and run the following commands:


```powershell
wsl --install
```
This command:
- Installs WSL 2 and Ubuntu (default Linux distribution)
- Enables required Windows components
- Downloads and installs the latest Linux kernel
- Sets WSL 2 as the default version
- **Note**: A system restart may be required after this command

After restart (if required), verify your WSL installation and Ubuntu distribution:

```powershell
wsl --list --verbose
```
This command shows:
- All installed Linux distributions
- Their WSL version (should be "2")
- Which distribution is currently default
- The status of each distribution

Set Ubuntu as your default distribution:

```powershell
wsl --set-default ubuntu
```
This ensures that:
- Ubuntu is used by default when you use WSL
- All WSL commands will target Ubuntu unless specified otherwise

To access your Ubuntu environment (the first time and then every time):

```powershell
wsl ~
```

This command:
- Opens Ubuntu shell
- Places you in your Ubuntu home directory (`~`)
- From here you can proceed with project setup

After these steps, Ubuntu will be ready for project development. You'll be prompted to create a username and password for your Ubuntu environment during first launch.


### 2. Install Java (Via SDKMAN)

Install Java via SDKMAN allowing you to install multiple versions of Java, and makes switching between versions easy.

Run the following commands:

```bash
sudo apt update
sudo apt install zip
```

To install SDKMAN prerequisites tools.

```bash
curl -s "https://get.sdkman.io" | bash
```

To install SDKMAN itself.

```bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
```

To add SDKMAN to your PATH.

```bash
sdk install java 11
```

To set Java 11 as the default version (Bloomreach 15 needs Java 11).

### 3. Install Maven

Install Maven, as the project uses Maven for dependency management.

Run the following commands:

```bash
# Note you can skip these command as you
# should have already run `apt update` in step 2.
sudo apt update
```

To install update apt.

```bash
sudo apt install maven
```

To install Maven.

Then create a file named `~/.m2/settings.xml` with your Bloomreach credentials.

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                      https://maven.apache.org/xsd/settings-1.0.0.xsd">
  <servers>
    <server>
      <id>hippo-maven2-enterprise</id>
      <username>*********</username>
      <password>*********</password>
    </server>
  </servers>
<profiles>
<profile>
      <id>default</id>
      <activation>
        <activeByDefault>true</activeByDefault>
      </activation>
      <repositories>
        <repository>
          <id>hippo-maven2-enterprise</id>
          <name>Bloomreach Maven 2 Enterprise</name>
          <url>https://maven.bloomreach.com/repository/maven2-enterprise/</url>
          <releases>
            <updatePolicy>never</updatePolicy>
            <checksumPolicy>fail</checksumPolicy>
          </releases>
        </repository>
      </repositories>
      <pluginRepositories>
        <pluginRepository>
          <id>hippo-maven2-enterprise</id>
          <name>Bloomreach Maven 2 Enterprise</name>
          <url>https://maven.bloomreach.com/repository/maven2-enterprise/</url>
          <snapshots>
            <enabled>false</enabled>
          </snapshots>
          <releases>
            <enabled>true</enabled>
          </releases>
        </pluginRepository>
      </pluginRepositories>
    </profile>
</profiles>
</settings>
```


### 4. Install Node.js (Via NVM)

Install Node.js via NVM allowing you to install multiple versions of Node.js, and makes switching between versions easy.

| Note                                                                                                                                                   |
|--------------------------------------------------------------------------------------------------------------------------------------------------------|
| Node is only used when you run `make frontend`. When when you run `make serve` maven does the getting of node.js and building of the frontend for you. |

Run the following commands:

```bash
# Note you can skip these command as you
# should have already run these command in step 2.
sudo apt update
sudo apt install zip
```

To install NVM prerequisites tools.

```bash
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.40.3/install.sh | bash
```

To install NVM itself.

```bash
nvm install 22.18.0
```

This is the version of Node.js that the project uses to build the front end assets.

```bash
nvm use 22.18.0
```

This sets the version of Node.js to 22.18.0 in the current shell.

```bash
nvm alias default 22.18.0
```

This sets the default version of Node.js to 22.18.0 when you open a new shell.

### 5. Clone the project

Clone the project to your ~/ directory.

It's lickly `git` is already installed. If it isn't, it's `sudo apt-get install git`.

```bash
git clone git@github.com:NHS-digital-website/hippo.git
```

This will clone the project to your home directory.

```bash
cd hippo
```

This will change your current directory to the project directory.

```bash
mvn verify -DskipTests
```

This will get all the dependencies and verify that you're good to go.

### 6. Install Make

Install Make, as the project uses Make for running the `Makefile` scripts.

```bash
sudo apt get install make
```

This will install Make.

### 5. Install IntelliJ IDEA

Now **back in Windows**, install IntelliJ Ultimate IDEA (if you haven't already).

You can download it from [here](https://www.jetbrains.com/idea/download/#section=windows).

IntelliJ IDEA lets you create and open projects in the WSL file system, run, and debug applications in the WSL environment.

1. In IntelliJ, go to `file -> open`.
2. On the page file dialogue, in the `location` put `\\wsl.localhost\Ubuntu\home\<your name>\hippo` (or `\\wsl.localhost\Ubuntu\home\` and then navigate to hippo).
3. Once the project opens, you can set the JDK. IntelliJ should offer the WSL JDK.
4. You can now run `make serve` in the terminal and debug set up a remote debugger on port 8000.
5. You can get to `http://localhost:8000/site` from a web browser running on Windows.

## Testing guide

Before executing make test.site-running, run the following command once to install Playwright:

```bash
mvn verify -f acceptance-tests/pom.xml -Pplaywright-install
```