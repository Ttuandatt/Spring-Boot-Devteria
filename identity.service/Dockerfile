# ============================
# Stage 1: Build (Maven + JDK 25)
# ============================
FROM maven:3.9.11-eclipse-temurin-25 AS builder

WORKDIR /app

# Copy pom.xml trước để cache dependencies
COPY pom.xml .

# Copy source code
COPY src ./src

# Build JAR (bỏ test)
RUN mvn -B -DskipTests clean package


# ============================
# Stage 2: Run (JDK 25 Runtime)
# ============================
FROM eclipse-temurin:25-jdk

WORKDIR /app

# Copy JAR từ stage builder
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
