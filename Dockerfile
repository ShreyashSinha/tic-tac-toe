FROM openjdk:17
EXPOSE 8080
ADD target/tic-tac-toe.jar tic-tac-toe.jar
ENTRYPOINT [ "java","-jar","tic-tac-toe.jar"]