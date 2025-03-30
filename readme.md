
# Microservice for internal ACL and validation

### Introduction

* This **microservice** will respond to the rabbitmq message that the first microservice "chargepoint" would sent.

* In this case, the microservice will **validate** the message against an ACL table with some data.
 
* If everything is ok will try to communicate with the callback url with a webclient.



The data for the ACL is on the /resources/data.sql to try to modify it if needed.
