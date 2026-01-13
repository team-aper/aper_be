# 경량 JRE 이미지 사용 (500MB -> 200MB)
FROM eclipse-temurin:17-jre

# 타임존 설정
ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# CA 인증서 업데이트 (MongoDB Atlas SSL 연결용)
RUN apt-get update && \
    apt-get install -y ca-certificates && \
    update-ca-certificates && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

# JAR 파일만 복사 (secret은 환경변수로 관리)
COPY build/libs/*.jar /app/app.jar

# 설정 파일은 빌드 시 포함됨 (submodule)
# secret.yml은 이미지에 포함하지 않음 (보안)

EXPOSE 8080

# JVM 옵션 최적화 (메모리 제한)
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# 프로파일은 실행 시 지정 (docker-compose에서)
CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]