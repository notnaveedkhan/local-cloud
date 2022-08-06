# Local Cloud
Local cloud is a cloud that is local to the machine. It is a cloud that is not accessible from the internet. 
You can use local cloud to store your data locally on your machine. Also, you can download your data from local cloud.
It stores your data in a directory where your application is running.

## How to use local cloud
To run this application you need **JRE 11+** installed on your machine.

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
