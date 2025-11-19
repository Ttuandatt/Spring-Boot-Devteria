# Identity Service - Docker Setup
Hướng dẫn build và chạy project Spring Boot với MySQL sử dụng Docker và Docker Compose.

---

## **1. Dọn dẹp môi trường cũ**

Xóa container, network và volume cũ:

```bash
docker compose down -v
```

## **2. Build Docker Image**
Từ thư mục chứa Dockerfile và docker-compose.yml:
```bash
docker-compose build
```

## **3. Chạy Docker Compose**
Chạy các container trong nền (Chạy MySQL và Spring Boot):
```bash
docker-compose up -d
```

## **4. Kiểm tra trạng thái container**
Kiểm tra các container đang chạy:
```bash
docker ps
```

## **5. Kiểm tra logs**
Xem logs của container Spring Boot:
```bash
docker logs -f <spring-boot-container-name>
```

## **6. Truy cập ứng dụng**
Mở trình duyệt hoặc Postman:
```bash
http://localhost:9090/identity
```
- 9090 là port host, 8080 là port container Spring Boot.

## **7. Dừng Docker Compose**
Dừng và xóa các container:
```bash
docker compose down
```

---

## **Tóm tắt**

- docker compose down -v          # Dọn sạch container, volume, network cũ
- docker-compose build            # Build image Spring Boot
- docker-compose up -d            # Chạy container (MySQL + Spring Boot)
- docker-compose logs -f app      # Xem logs app
- docker ps                       # Kiểm tra trạng thái container
- Truy cập ứng dụng ở http://localhost:9090/identity
- docker-compose down             # Dừng container khi cần



---

Hướng dẫn publish Docker Image lên Docker Hub.

## **1. Đăng nhập Docker Hub**
```bash
docker login
```
## **2. Kiểm tra danh sách image đang có**
```bash
docker images
```

## **3. Tag Docker Image**
```bash
docker tag <local-image>:<tag> <dockerhub-username>/<repo-name>:<tag>
```
Ví dụ: docker tag identity-service:latest tuandatdev/identity-service:latest


## **4. Push Docker Image lên Docker Hub**
```bash
docker push <dockerhub-username>/<repo-name>:<tag>
```
Ví dụ: docker push tuandatdev/identity-service:latest  

## **5. Kiểm tra trên Docker Hub**
Truy cập trang Docker Hub của bạn để xác nhận image đã được upload thành công.

https://hub.docker.com/repositories

---
## **Tóm tắt lệnh publish Docker Image**
- docker login                                           # Đăng nhập Docker Hub
- docker images                                          # Kiểm tra danh sách image
- docker tag <local-image>:<tag> <dockerhub-username>/<repo-name>:<tag>   # Tag image
- docker push <dockerhub-username>/<repo-name>:<tag>          # Push image lên Docker Hub
- Kiểm tra trên trang Docker Hub của bạn: https://hub.docker.com/repositories
