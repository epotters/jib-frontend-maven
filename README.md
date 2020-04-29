# README

### What
JIB-Frontend Maven is a Maven plugin for containerizing frontend projects. 


### Why
If you deploy to a cloud service, you have to wrap your frontend apps in a container.
There are several approaches possible to realize this.
You can create a docker file, build it and push it to a registry like Docker Hub.
You can create docker-compose file with a service based on a standard image, copy your app files to a handy location and map them on a volume
You can include your frontend app in the backend container and use your application server as a web server (yikes)

For Java services, Google's open source JIB project offers a seamless way to build docker containers directly in Maven without the need of a docker agent or daemon.

Eirik Slettenberg's frontend-maven-plugin does an excellent job building frontend projects from Maven.
This way building the frontend image is simply a part of the regular Maven build.


### How
JIB has a base project called jib-core that does the containerizing. I just build a maven-plugin that uses this to build images for Angular projects.
All heavy lifting is done by the jib-core library.

