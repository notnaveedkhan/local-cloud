# Local Cloud
The local cloud is a cloud that exists on the machine itself. It is not accessible from the internet. You can use the local cloud to store your data locally on your machine. Additionally, you can download your data from the local cloud. It stores your data in a directory where your application is running.

## How to use local cloud
To run this application you will need **JRE 11+**

## How to override local cloud default username and password
You can override default username and password by passing these parameters to the application:

```text
--spring.security.user.name=<username>
--spring.security.user.password=<password>
```

**Example:**
```shell

java.exe -jar local-cloud.jar --spring.security.user.name=admin --spring.security.user.password=admin
```
