# Hướng dẫn chạy dự án Backend
## Yêu cầu cài đặt
- [JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Docker](https://docs.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/install/)
## Thiết lập môi trường
### 1. Clone dự án
```sh
    git clone https://github.com/tunguyen1104/food-app.git
    cd food-app
```
### 2. Tạo vùng nhớ cho database mysql và redis
Tạo folder để lưu trữ dữ liệu với mysql và redis:
```sh
    mkdir -p mysql-data redis-data
```
Folder này sẽ chứa dữ liệu của mysql và redis ở local
### 3. Chạy docker
Sử dụng câu lệnh:
```sh
    docker compose up -d
```
### 4. Kiểm hoạt động của container
Sử dụng câu lệnh để kiểm tra các services container đang chạy:
```shell
    docker ps
```
## Chi tiết các services
| Service          | Mô Tả                    | URL/Port |
|-----------------|--------------------------|---------|
| MySQL           | Database của chương trình | 3306    |
| Redis           | In-memory data store     | 6379    |
| phpMyAdmin      | MySQL Web UI             | [http://localhost:8080](http://localhost:8080) |
| Redis Commander | Redis Web UI             | [http://localhost:8078](http://localhost:8078) |

## Dừng container
Dừng các container đang chạy:
```shell
    docker compose down
```
Dừng và xóa dữ liệu database:
```shell
    docker compose down -v
```