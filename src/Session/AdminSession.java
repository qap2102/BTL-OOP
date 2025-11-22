package Session;

import model.Admin;

public class AdminSession {
    
    // Biến tĩnh (static) để lưu trữ đối tượng Admin đang đăng nhập duy nhất trong phiên làm việc
    private static Admin loggedInAdmin = null;

    // [LƯU SESSION] Thiết lập Admin đang đăng nhập (Gọi khi Login thành công)
    public static void setLoggedInAdmin(Admin admin) {
        loggedInAdmin = admin;
        System.out.println("Session set: " + (admin != null ? admin.getFullName() : "null"));
    }

    // [LẤY SESSION] Lấy thông tin Admin hiện tại (Để hiển thị tên, phân quyền...)
    public static Admin getLoggedInAdmin() {
        System.out.println("Session get: " + (loggedInAdmin != null ? loggedInAdmin.getFullName() : "null"));
        return loggedInAdmin;
    }

    // [XÓA SESSION] Đăng xuất, xóa thông tin Admin khỏi bộ nhớ
    public static void clear() {
        System.out.println("Clearing session");
        loggedInAdmin = null;
    }

    // [KIỂM TRA] Kiểm tra xem có Admin nào đang đăng nhập không
    public static boolean isLoggedIn() {
        return loggedInAdmin != null;
    }
}