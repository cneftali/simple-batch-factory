Simple Batch Factory
=====================

This is a quick tech demo how write a simple batch factory:
- [Spring Boot](http://projects.spring.io/spring-boot/),
- [Spring Data Rest](http://projects.spring.io/spring-data-rest/),
- [Spring Data JPA](http://projects.spring.io/spring-data-jpa/),
- [Spring Batch](http://projects.spring.io/spring-batch/),
- [Spring Integration](http://projects.spring.io/spring-integration/),
- [Undertow](http://undertow.io/),
- [Docker](https://www.docker.io/),
- [Microservices](http://martinfowler.com/articles/microservices.html).

Diagram
-------

Requirements
------------

1. [Maven 3.3.9](https://maven.apache.org/)
1. [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
1. [Lombok](https://projectlombok.org/download.html)
1. [Docker Compose](https://docs.docker.com/compose/install/)
    
Building from Source
--------------------

Clone the git repository using the URL on the github home page:

```sh
$ git clone https://github.com/cneftali/simple-batch-factory.git
$ cd simple-batch-factory
$ mvn clean install
```
Usage
-----

1. Maven compile the Spring Boot Docker Project

    ```bash
    $ mvn clean install
    ```
1. Build Images

    ```bash
    $ docker-compose build
    ```
1. Start all the containers

    ```bash    
    $ docker-compose up -d
    ```
1. Logs

    ```bash
    $ docker-compose logs
    ```
1. Stopping All Services

    ```bash
    $ docker-compose stop
    ```

Try it !!
---------

1. Get list of Jobs

    ```bash
    $ curl http://localhost/jobs
    {
     "_links" : {
       "self" : {
         "href" : "http://localhost/jobs{?page,size,sort}",
         "templated" : true
       }
     },
     "page" : {
       "size" : 20,
       "totalElements" : 0,
       "totalPages" : 0,
       "number" : 0
     
    }
    ```
1. launch job 'jobName' in future :

    ```bash
    $ curl -i -X POST -H "Content-Type:application/json" -d '{ "jobName":"jobName", "jobParameter": {"id":1, "param1": "value"}, "dateStart":"2050-07-10T14:49:04.206Z"}' http://localhost/jobs
    HTTP/1.1 201 Created
    Server: Apache-Coyote/1.1
    Location: http://localhost/jobs/1
    Content-Length: 0
    Date: Thu, 20 Aug 2015 13:08:45 GMT
        
    $ curl -i http://localhost/jobs/1
    {
     "jobName" : "jobName",
     "jobParameter" : {
       "id" : 1,
       "param1" : "value"
     },
     "dateStart" : :2541077344206,
     "jobStatus" : "WAITING",
     "_links" : {
       "self" : {
         "href" : "http://localhost/jobs/1"
       }
     }
    }
    ```
1. launch job 'jobName' now :

    ```bash
    $ curl -i -X POST -H "Content-Type:application/json" -d '{ "jobName":"jobName", "jobParameter": {"id":1, "param1": "value"}, "dateStart":"2015-07-10T14:49:04.206Z"}' http://localhost/jobs
    HTTP/1.1 201 Created
    Server: Apache-Coyote/1.1
    Location: http://localhost/jobs/1
    Content-Length: 0
    Date: Thu, 20 Aug 2015 13:08:45 GMT
            
    $ curl -i http://localhost/jobs/1
    HTTP/1.1 404 Not Found
    Connection: keep-alive
    Content-Length: 0
    Date: Wed, 26 Aug 2015 14:51:27 GMT
    ```
1.  send directly in engine (docker host engine-servicex1.cne):

    ```bash
    $ curl -i -X POST -H "Content-Type:application/json" -d '{"jobName":"jobName","jobParameters":{"parameters":{"id":{"identifying":true,"value":"1","type":"STRING"},"param1":{"identifying":true,"value":"value","type":"STRING"}}},"scheduleId":1,"createTime":1440599326140}' http://localhost:8080/process
    HTTP/1.1 201 Created
    Connection: keep-alive
    Transfer-Encoding: chunked
    Content-Type: application/json;charset=UTF-8
    Date: Wed, 26 Aug 2015 14:55:46 GMT
    ```
