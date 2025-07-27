# Phần 1: Lập kế hoạch và thiết kế kiểm thử

## 1. Chức năng kiểm thử đã chọn
- Quản lý khóa học (Course Management)
- Học video (Video Learning)
- Tham gia khóa học (Course Enrollment)
- Quản lý bài tập (Assignment Management)
- Nộp bài tập (Assignment Submission)

### 1.1. Mục tiêu kiểm thử
- Đảm bảo các chức năng trên hoạt động đúng theo yêu cầu nghiệp vụ.
- Phát hiện lỗi chức năng, giao diện và luồng xử lý.
- Đảm bảo tính ổn định khi tích hợp với các module khác.

### 1.2. Loại kiểm thử áp dụng
- **Kiểm thử chức năng (Functional Testing):** Đảm bảo các chức năng chính thực hiện đúng yêu cầu.
- **Kiểm thử tích hợp (Integration Testing):** Đảm bảo các module phối hợp đúng khi tích hợp.
- **Kiểm thử hồi quy (Regression Testing):** Đảm bảo các chức năng cũ không bị ảnh hưởng khi cập nhật mới.
- **Kiểm thử đơn vị (Unit Testing):** Kiểm tra logic từng hàm/module nhỏ.

**Lý do chọn:** Các loại kiểm thử này giúp phát hiện lỗi ở nhiều cấp độ, đảm bảo chất lượng tổng thể của hệ thống.

### 1.3. Thiết kế ca kiểm thử (Test Case)
| ID   | Tên ca kiểm thử                | Bước thực hiện                                                                 | Dữ liệu vào                | Kết quả mong đợi                                  |
|------|-------------------------------|-------------------------------------------------------------------------------|----------------------------|---------------------------------------------------|
| TC01 | Tạo khóa học mới              | 1. Đăng nhập admin\n2. Vào trang quản lý khóa học\n3. Nhấn "Tạo mới"\n4. Nhập thông tin\n5. Lưu | Thông tin khóa học mới     | Khóa học được tạo thành công                      |
| TC02 | Sửa thông tin khóa học        | 1. Đăng nhập admin\n2. Chọn khóa học\n3. Nhấn "Sửa"\n4. Thay đổi thông tin\n5. Lưu | Thông tin sửa đổi          | Thông tin khóa học được cập nhật                  |
| TC03 | Xóa khóa học                  | 1. Đăng nhập admin\n2. Chọn khóa học\n3. Nhấn "Xóa"\n4. Xác nhận                | ID khóa học                | Khóa học bị xóa khỏi hệ thống                     |
| TC04 | Xem danh sách khóa học        | 1. Đăng nhập\n2. Vào trang danh sách khóa học                                 | -                          | Hiển thị đúng danh sách khóa học                  |
| TC05 | Đăng ký tham gia khóa học     | 1. Đăng nhập user\n2. Chọn khóa học\n3. Nhấn "Đăng ký"                          | ID user, ID khóa học       | User được thêm vào danh sách học viên             |
| TC06 | Học video trong khóa học      | 1. Đăng nhập user\n2. Vào khóa học đã đăng ký\n3. Chọn video\n4. Xem video     | ID user, ID video          | Video phát đúng, ghi nhận tiến độ học             |
| TC07 | Đánh dấu hoàn thành video     | 1. Xem hết video\n2. Nhấn "Hoàn thành"                                        | ID user, ID video          | Trạng thái video chuyển sang "Đã hoàn thành"      |
| TC08 | Kiểm tra tiến độ học tập      | 1. Đăng nhập user\n2. Vào trang tiến độ học tập                                | ID user                    | Hiển thị đúng tiến độ các khóa học đã tham gia     |
| TC09 | Đăng xuất                     | 1. Đăng nhập\n2. Nhấn "Đăng xuất"                                             | -                          | Người dùng đăng xuất thành công                    |
| TC10 | Đăng nhập sai thông tin       | 1. Nhập sai tài khoản/mật khẩu\n2. Nhấn "Đăng nhập"                            | Tài khoản/mật khẩu sai     | Hiển thị thông báo lỗi đăng nhập                   |
| TC11 | Tạo bài tập mới               | 1. Đăng nhập instructor\n2. Vào khóa học\n3. Chọn nội dung\n4. Tạo bài tập\n5. Nhập thông tin\n6. Lưu | Thông tin bài tập mới      | Bài tập được tạo thành công                       |
| TC12 | Sửa thông tin bài tập         | 1. Đăng nhập instructor\n2. Chọn bài tập\n3. Nhấn "Sửa"\n4. Thay đổi thông tin\n5. Lưu | Thông tin sửa đổi          | Thông tin bài tập được cập nhật                   |
| TC13 | Xóa bài tập                   | 1. Đăng nhập instructor\n2. Chọn bài tập\n3. Nhấn "Xóa"\n4. Xác nhận          | ID bài tập                 | Bài tập bị xóa khỏi hệ thống                      |
| TC14 | Nộp bài tập                   | 1. Đăng nhập student\n2. Vào bài tập\n3. Nhập nội dung\n4. Upload file\n5. Nộp bài | Nội dung bài làm + file    | Bài tập được nộp thành công                       |
| TC15 | Chấm điểm bài tập             | 1. Đăng nhập instructor\n2. Xem bài nộp\n3. Nhập điểm\n4. Nhập feedback\n5. Lưu | Điểm số + feedback         | Bài tập được chấm điểm thành công                 |
| TC16 | Xem danh sách bài nộp         | 1. Đăng nhập instructor\n2. Vào bài tập\n3. Xem danh sách bài nộp             | ID bài tập                 | Hiển thị đúng danh sách bài nộp của học viên      |
| TC17 | Xem bài tập đã nộp (student)  | 1. Đăng nhập student\n2. Vào trang bài tập của tôi                            | ID student                 | Hiển thị đúng danh sách bài tập đã nộp            |
| TC18 | Nộp bài trùng lặp             | 1. Đăng nhập student\n2. Nộp bài tập\n3. Thử nộp lại bài tập đó               | Bài tập đã nộp             | Hiển thị thông báo lỗi không được nộp lại         |
| TC19 | Nộp bài sau deadline          | 1. Đăng nhập student\n2. Thử nộp bài tập đã quá hạn                           | Bài tập quá hạn            | Hiển thị thông báo không được nộp sau deadline    |
| TC20 | Xem feedback của bài tập      | 1. Đăng nhập student\n2. Vào bài tập đã được chấm điểm                        | ID bài tập đã chấm         | Hiển thị điểm số và feedback từ giảng viên        |

> (Có thể bổ sung thêm test case tùy thực tế)
