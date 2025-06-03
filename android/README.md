# 📱 Hướng dẫn chạy dự án Android (Mobile App)

## 🧰 Yêu cầu cài đặt

Trước khi chạy dự án Android, bạn cần cài đặt:

- [Android Studio (Arctic Fox trở lên)](https://developer.android.com/studio)
- [Java JDK 17+](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- Thiết bị Android thực tế hoặc máy ảo (AVD)

## 📦 Thiết lập môi trường

### 1. Clone dự án

```bash
git clone https://github.com/tunguyen1104/food-app.git
cd food-app/android
```

### 2. Mở dự án bằng Android Studio

- Mở **Android Studio**
- Chọn **"Open an existing project"**
- Dẫn đến thư mục `food-app/android`
- Chờ quá trình **sync Gradle** hoàn tất

### 3. Cấu hình file `local.properties`

Thêm đường dẫn đến SDK của bạn nếu chưa có:

```
properties
sdk.dir=/Users/your-username/Library/Android/sdk  # macOS
sdk.dir=C:\\Users\\your-username\\AppData\\Local\\Android\\Sdk  # Windows

```

### 4. Cấu hình API endpoint

Mở file `Constants.java` và chỉnh sửa cho khớp với địa chỉ backend của bạn:

```java
public static final String URL_HOST_SERVER = "http://10.0.2.2:8081";
public static final String SOCKET_URL = "ws://10.0.2.2:8081/ws";

```

> 🔁 Nếu bạn dùng thiết bị thật, thay 10.0.2.2 bằng địa chỉ IP local của máy tính chạy backend.

### 5. Build và chạy ứng dụng

- Nhấn **Run (Shift + F10)** hoặc chọn thiết bị ảo/thật và nhấn ▶️ Run
- Ứng dụng sẽ được cài đặt và khởi chạy trên thiết bị

## 🧪 Kiểm tra nhanh

- Đăng nhập bằng tài khoản đã tạo từ phía backend
- Kiểm tra các chức năng như: xem danh sách món ăn, đặt món, cập nhật đơn hàng...
