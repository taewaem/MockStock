# 베이스 이미지 설정 (Java 11 버전 사용)
FROM openjdk:17-jdk

# JAR 파일 복사 (빌드된 JAR 파일 경로를 지정)
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8080
ENV JAR_PATH=/app/build/libs

# 애플리케이션 실행 명령어
ENTRYPOINT ["java","-jar","-Duser.timezone=Asia/Seoul","/app.jar"]