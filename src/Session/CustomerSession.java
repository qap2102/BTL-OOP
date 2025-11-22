package Session;

import model.Customer;

public class CustomerSession {

    // Biến tĩnh (static) lưu trữ đối tượng Khách hàng đang đăng nhập
    // Giúp truy cập thông tin khách hàng từ bất kỳ form nào trong ứng dụng
    private static Customer loggedInCustomer;

    // [LƯU SESSION] Thiết lập khách hàng đang đăng nhập (Gọi khi Login thành công)
    public static void setLoggedInCustomer(Customer c){
        loggedInCustomer = c;
    }

    // [LẤY SESSION] Lấy thông tin khách hàng hiện tại 
    // (Dùng để hiển thị tên, lấy ID để truy vấn số dư, lịch sử giao dịch...)
    public static Customer getLoggedInCustomer(){
        return loggedInCustomer;
    }

    // [XÓA SESSION] Đăng xuất, xóa thông tin khách hàng khỏi bộ nhớ
    public static void clear(){
        loggedInCustomer = null;
    }
}