# Spring-Boot-Devteria

## #15 Validation nâng cao, trích xuất thông tin từ annotation

Video này tiếp tục chủ đề xác thực (validation) và tập trung vào kỹ thuật nâng cao: trích xuất thông tin từ các annotation để xây dựng các thông báo lỗi linh hoạt và chi tiết hơn. Việc nắm vững kỹ thuật này rất quan trọng vì nó giúp đáp ứng các yêu cầu nghiệp vụ phức tạp.

Mục đích chính của kỹ thuật này:

**1. Đáp ứng yêu cầu nghiệp vụ chi tiết:** Trong thực tế, các thông báo lỗi cần phải rất cụ thể (ví dụ: "Bạn phải đủ 18 tuổi") thay vì chỉ là lỗi chung chung ("invalid DOB").

**2. Linh hoạt và dễ bảo trì:** Kỹ thuật này cho phép các tham số cấu hình trong annotation (như tuổi tối thiểu) có thể thay đổi (ví dụ từ 18 thành 16) mà không cần phải cập nhật lại mã lỗi (error code) hoặc thông báo lỗi thủ công. Điều này làm cho công việc trở nên nhẹ nhàng và linh hoạt hơn.

**Các bước thực hiện trích xuất thông tin:**

Kỹ thuật này được xử lý trong lớp Global Exception Handler:

**1. Bắt ngoại lệ:** Khi xảy ra vi phạm ràng buộc xác thực, hệ thống sẽ trả về ngoại lệ MethodArgumentNotValidException
.
**2. Truy cập Binding Result:** Từ ngoại lệ (exception), bạn có thể truy cập vào binding result.

**3. Trích xuất Constraint Violation:** Từ binding result, bạn có thể unwrap để lấy ra Constraint Violation.

**4. Lấy Attribute Map:** Từ Constraint Violation, bạn có thể lấy được bản đồ thuộc tính (attribute map), đây là một Map chứa các tham số chi tiết đã được truyền vào annotation (ví dụ: giá trị Min là 18).

**5. Xây dựng thông báo động:** Sử dụng giá trị đã trích xuất từ attribute map để thay thế các placeholder (ví dụ: {Min}) trong thông báo lỗi tiêu chuẩn. Điều này giúp hệ thống tự động thay đổi nội dung thông báo lỗi khi cấu hình xác thực thay đổi.

**Ví dụ cụ thể:** Xác thực Ngày sinh (DOB Constraint)

Trong các video trước, đã có việc xây dựng annotation tùy chỉnh DobConstraint để xác thực người dùng phải đạt một độ tuổi tối thiểu.

Tình huống:

• Giả sử bạn đã cấu hình trong annotation: Min = 16.

• Bạn muốn thông báo lỗi cho người dùng là: "Your age must be at least {Min}".

Thực hiện:

**1.** Trong quá trình xử lý ngoại lệ MethodArgumentNotValidException, hệ thống trích xuất được Attribute Map.

**2.** Trong Map này, key Min sẽ chứa giá trị là 16.

**3.** Hàm xử lý thông báo sẽ sử dụng giá trị này để thay thế placeholder {Min} trong thông báo lỗi.

**4.** Kết quả: Khi người dùng nhập ngày sinh không hợp lệ, thông báo trả về sẽ là: "Your age must be at least 16".
Lợi ích: Nếu sau này yêu cầu nghiệp vụ thay đổi và bạn sửa Min thành 10 trong annotation, hệ thống sẽ tự động trả về thông báo "Your age must be at least 10" mà không cần phải sửa bất kỳ mã code lỗi nào khác.
Kỹ thuật này cũng được áp dụng tương tự cho các lỗi xác thực khác, ví dụ như lỗi invalid password (yêu cầu độ dài tối thiểu của mật khẩu).
