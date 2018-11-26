FROM java:8
VOLUME /tmp
ADD ${project.build.finalName}.war app.war
RUN bash -c 'touch /app.war'
ENTRYPOINT ${docker.entrypoint}

