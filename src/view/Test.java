package view;
// Lấy giá trị nhập vào
String giatri = txtGiatri1.getText().trim();

// 1.1: Kiểm tra rỗng
if (giatri.isEmpty()) {
    JOptionPane.showMessageDialog(this, "Giá trị ưu đãi không được để trống");
    return false;
}

// 1.2: Kiểm tra định dạng chỉ cho phép số và dấu %
if (!giatri.matches("^\\d+%$")) {
    JOptionPane.showMessageDialog(this, "Giá trị ưu đãi chỉ được chứa số và kết thúc bằng dấu '%' (không chứa chữ hay ký tự đặc biệt khác)");
    return false;
}

// 1.3: Lấy phần số và kiểm tra min/max
int giaTriSo;
try {
    giaTriSo = Integer.parseInt(giatri.replace("%", ""));
} catch (NumberFormatException e) {
    JOptionPane.showMessageDialog(this, "Giá trị ưu đãi không hợp lệ");
    return false;
}

if (giaTriSo < 5 || giaTriSo > 80) {
    JOptionPane.showMessageDialog(this, "Giá trị ưu đãi phải từ 5% đến 80%");
    return false;
}