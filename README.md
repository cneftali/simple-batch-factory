# Simple Batch Factory - IN PROGRESS

This is a quick tech demo how write a simple batch factory:
[Spring Boot](http://projects.spring.io/spring-boot/),
[Spring Data Rest](http://projects.spring.io/spring-data-rest/),
[Spring Data JPA] (http://projects.spring.io/spring-data-jpa/),
[Spring Batch](http://projects.spring.io/spring-batch/),
[Spring Integration](http://projects.spring.io/spring-integration/),
[Undertow](http://undertow.io/),
[Docker](https://www.docker.io/) and
[Microservices](http://martinfowler.com/articles/microservices.html).

## Diagram

## Requirements
    1. Docker and Docker Compose
    2. maven and jdk 8
    
## Building from Source

Clone the git repository using the URL on the github home page:

    $ git clone https://github.com/cneftali/simple-batch-factory.git
    $ cd simple-batch-factory
    $ mvn clean install


## [Docker and Docker Compose](https://docs.docker.com/compose/#installation-and-set-up)

    # 1. Maven compile the Spring Boot Docker Project
    
    $ mvn clean install
    
    # 2. Build Images

    $ docker-compose build
   
    # 2. Start all the containers
    
    $ docker-compose up -d
        
    # 4. Try it!
    
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
        }
        
        $ curl -i -X POST -H "Content-Type:application/json" -d '{ "jobName":"jobName", "jobParameter": {"id":1, "param1": "value"}, "dateStart":"2015-07-10T14:49:04.206Z"}' http://localhost/jobs
        HTTP/1.1 201 Created
        Server: Apache-Coyote/1.1
        Location: http://localhost/jobs/1
        Content-Length: 0
        Date: Thu, 20 Aug 2015 13:08:45 GMT
        
        $ curl http://localhost/jobs/1
        {
          "jobName" : "jobName",
          "jobParameter" : {
            "id" : 1,
            "param1" : "value"
          },
          "dateStart" : 1436539744206,
          "jobStatus" : "WAITING",
          "_links" : {
            "self" : {
              "href" : "http://localhost/jobs/1"
            }
          }
        }
    
    # 5. Logs
    
    $ docker-compose logs
    
    # 6. Stopping All Services
    
    $ docker-compose stop 