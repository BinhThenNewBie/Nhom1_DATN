
package view;

import DAO.HoaDonDAO111;
import Model.HoaDon;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ThongKe extends javax.swing.JFrame {

    private HoaDonDAO111 hdd = new HoaDonDAO111();
    private DecimalFormat vndFormat = new DecimalFormat("#,###");
    private DefaultTableModel modelThongKe;

    // Components
    private JPanel pnlMain;
    private JPanel pnlHeader;
    private JPanel pnlStats;
    private JPanel pnlTable;
    private JLabel lblTitle;
    private JLabel lblTongDoanhThu;
    private JLabel lblTongHoaDon;
    private JLabel lblDoanhThuTB;
    private JLabel lblHoaDonHomNay;
    private JComboBox<String> cmbThongKe;
    private JTable tblThongKe;
    private JScrollPane scrollTable;
    private JButton btnLamMoi;

    public ThongKe() {
        initComponents();
        setupEventListeners();
        loadThongKe();
        setLocationRelativeTo(null);
        setTitle("Thống Kê Doanh Thu");
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(1500, 1080);

        setSize(1200, 825);

        // Main panel
        pnlMain = new JPanel(new BorderLayout());
        pnlMain.setBackground(new Color(240, 248, 255));

        // Header panel
        pnlHeader = new JPanel(new FlowLayout());
        pnlHeader.setBackground(new Color(31, 51, 86));
        pnlHeader.setPreferredSize(new Dimension(1200, 50));

        lblTitle = new JLabel("THỐNG KÊ DOANH THU & HÓA ĐƠN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        pnlHeader.add(lblTitle);

        // Stats panel
        pnlStats = new JPanel(new GridLayout(2, 2, 20, 20));
        pnlStats.setBackground(new Color(240, 248, 255));
        pnlStats.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(31, 51, 86), 2),
                "Tổng Quan", 0, 0, new Font("Segoe UI", Font.BOLD, 16),
                new Color(31, 51, 86)
        ));
        pnlStats.setPreferredSize(new Dimension(1200, 200));

        // Create stat cards
        lblTongDoanhThu = createStatCard("TỔNG DOANH THU", "0 ₫", new Color(46, 125, 50));
        lblTongHoaDon = createStatCard("TỔNG HÓA ĐƠN", "0", new Color(21, 101, 192));
        lblDoanhThuTB = createStatCard("DOANH THU TRUNG BÌNH/HÓA ĐƠN", "0 ₫", new Color(156, 39, 176));
        lblHoaDonHomNay = createStatCard("HÓA ĐƠN HÔM NAY", "0", new Color(255, 87, 34));

        pnlStats.add(lblTongDoanhThu);
        pnlStats.add(lblTongHoaDon);
        pnlStats.add(lblDoanhThuTB);
        pnlStats.add(lblHoaDonHomNay);

        // Control panel
        JPanel pnlControls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlControls.setBackground(new Color(240, 248, 255));

        JLabel lblFilter = new JLabel("THỐNG KÊ THEO:");
        lblFilter.setFont(new Font("Segoe UI", Font.BOLD, 14));

        String[] options = {"TẤT CẢ", "HÔM NAY", "TUẦN NÀY", "THÁNG NÀY", "NĂM NAY"};
        cmbThongKe = new JComboBox<>(options);
        cmbThongKe.setPreferredSize(new Dimension(150, 30));

        btnLamMoi = new JButton("LÀM MỚI");
        btnLamMoi.setBackground(new Color(31, 51, 86));
        btnLamMoi.setForeground(Color.WHITE);
        btnLamMoi.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLamMoi.setPreferredSize(new Dimension(100, 30));

        pnlControls.add(lblFilter);
        pnlControls.add(cmbThongKe);
        pnlControls.add(Box.createHorizontalStrut(20));
        pnlControls.add(btnLamMoi);

        // Table panel
        pnlTable = new JPanel(new BorderLayout());
        pnlTable.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(31, 51, 86), 2),
                "CHI TIẾT THỐNG KÊ", 0, 0,
                new Font("Segoe UI", Font.BOLD, 16), new Color(31, 51, 86)
        ));

        // Initialize table
        String[] columns = {"NGÀY", "SỐ HÓA ĐƠN", "DOANH THU", "DOANH THU TRUNG BÌNH"};
        modelThongKe = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblThongKe = new JTable(modelThongKe);
        tblThongKe.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblThongKe.setRowHeight(25);
        tblThongKe.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tblThongKe.getTableHeader().setBackground(new Color(31, 51, 86));
        tblThongKe.getTableHeader().setForeground(Color.BLACK);

        scrollTable = new JScrollPane(tblThongKe);

        pnlTable.add(scrollTable, BorderLayout.CENTER);

        // Add components to main panel
        pnlMain.add(pnlHeader, BorderLayout.NORTH);

        JPanel pnlContent = new JPanel(new BorderLayout(5, 5));
        pnlContent.setBackground(new Color(240, 248, 255));
        pnlContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        pnlContent.add(pnlStats, BorderLayout.NORTH);
        pnlContent.add(pnlControls, BorderLayout.CENTER);
        pnlContent.add(pnlTable, BorderLayout.SOUTH);

        pnlMain.add(pnlContent, BorderLayout.CENTER);

        add(pnlMain);
    }

    private JLabel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitle.setForeground(color);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblValue.setForeground(Color.BLACK);
        lblValue.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(lblTitle);
        card.add(Box.createVerticalStrut(10));
        card.add(lblValue);

        // Return the value label for updating
        JLabel container = new JLabel();
        container.setLayout(new BorderLayout());
        container.add(card, BorderLayout.CENTER);
        container.putClientProperty("valueLabel", lblValue);

        return container;
    }

    private void setupEventListeners() {
        cmbThongKe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadThongKe();
            }
        });

        btnLamMoi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadThongKe();
                JOptionPane.showMessageDialog(ThongKe.this,
                        "Đã làm mới dữ liệu thống kê!",
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    private void loadThongKe() {
        try {
            List<HoaDon> allHoaDon = hdd.getALL();
            List<HoaDon> filteredHoaDon = filterHoaDonByPeriod(allHoaDon);

            // Update stats cards
            updateStatsCards(filteredHoaDon, allHoaDon);

            // Update table
            updateThongKeTable(filteredHoaDon);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tải dữ liệu thống kê: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private List<HoaDon> filterHoaDonByPeriod(List<HoaDon> hoaDonList) {
        String selectedPeriod = (String) cmbThongKe.getSelectedItem();
        List<HoaDon> filtered = new ArrayList<>();

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(cal.getTime());

        for (HoaDon hd : hoaDonList) {
            if (!"Đã thanh toán".equalsIgnoreCase(hd.getTrangThai())) {
                continue;
            }

            boolean shouldInclude = false;

            switch (selectedPeriod) {
                case "Tất cả":
                    shouldInclude = true;
                    break;
                case "Hôm nay":
                    shouldInclude = hd.getNgayThangNam().equals(today);
                    break;
                case "Tuần này":
                    shouldInclude = isThisWeek(hd.getNgayThangNam());
                    break;
                case "Tháng này":
                    shouldInclude = isThisMonth(hd.getNgayThangNam());
                    break;
                case "Năm này":
                    shouldInclude = isThisYear(hd.getNgayThangNam());
                    break;
            }

            if (shouldInclude) {
                filtered.add(hd);
            }
        }

        return filtered;
    }

    private boolean isThisWeek(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(dateStr);
            Calendar cal = Calendar.getInstance();
            int currentWeek = cal.get(Calendar.WEEK_OF_YEAR);
            int currentYear = cal.get(Calendar.YEAR);

            cal.setTime(date);
            int dateWeek = cal.get(Calendar.WEEK_OF_YEAR);
            int dateYear = cal.get(Calendar.YEAR);

            return currentWeek == dateWeek && currentYear == dateYear;
        } catch (ParseException e) {
            return false;
        }
    }

    private boolean isThisMonth(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(dateStr);
            Calendar cal = Calendar.getInstance();
            int currentMonth = cal.get(Calendar.MONTH);
            int currentYear = cal.get(Calendar.YEAR);

            cal.setTime(date);
            int dateMonth = cal.get(Calendar.MONTH);
            int dateYear = cal.get(Calendar.YEAR);

            return currentMonth == dateMonth && currentYear == dateYear;
        } catch (ParseException e) {
            return false;
        }
    }

    private boolean isThisYear(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(dateStr);
            Calendar cal = Calendar.getInstance();
            int currentYear = cal.get(Calendar.YEAR);

            cal.setTime(date);
            int dateYear = cal.get(Calendar.YEAR);

            return currentYear == dateYear;
        } catch (ParseException e) {
            return false;
        }
    }

    private void updateStatsCards(List<HoaDon> filteredHoaDon, List<HoaDon> allHoaDon) {
        // Calculate total revenue for filtered period
        float tongDoanhThu = 0;
        for (HoaDon hd : filteredHoaDon) {
            tongDoanhThu += hd.getTongTien();
        }

        // Calculate total bills for filtered period
        int tongHoaDon = filteredHoaDon.size();

        // Calculate average revenue per bill
        float doanhThuTB = tongHoaDon > 0 ? tongDoanhThu / tongHoaDon : 0;

        // Calculate today's bills
        int hoaDonHomNay = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(new Date());

        for (HoaDon hd : allHoaDon) {
            if ("Đã thanh toán".equalsIgnoreCase(hd.getTrangThai())
                    && hd.getNgayThangNam().equals(today)) {
                hoaDonHomNay++;
            }
        }

        // Update UI
        updateStatCard(lblTongDoanhThu, vndFormat.format(tongDoanhThu) + " ₫");
        updateStatCard(lblTongHoaDon, String.valueOf(tongHoaDon));
        updateStatCard(lblDoanhThuTB, vndFormat.format(doanhThuTB) + " ₫");
        updateStatCard(lblHoaDonHomNay, String.valueOf(hoaDonHomNay));
    }

    private void updateStatCard(JLabel cardContainer, String value) {
        JLabel valueLabel = (JLabel) cardContainer.getClientProperty("valueLabel");
        if (valueLabel != null) {
            valueLabel.setText(value);
        }
    }

    private void updateThongKeTable(List<HoaDon> hoaDonList) {
        modelThongKe.setRowCount(0);

        // Group by date
        Map<String, List<HoaDon>> groupedByDate = new HashMap<>();
        for (HoaDon hd : hoaDonList) {
            String date = hd.getNgayThangNam();
            groupedByDate.computeIfAbsent(date, k -> new ArrayList<>()).add(hd);
        }

        // Sort dates
        List<String> sortedDates = new ArrayList<>(groupedByDate.keySet());
        Collections.sort(sortedDates, Collections.reverseOrder());

        // Add rows to table
        for (String date : sortedDates) {
            List<HoaDon> dailyHoaDon = groupedByDate.get(date);
            int soHoaDon = dailyHoaDon.size();
            float doanhThu = 0;

            for (HoaDon hd : dailyHoaDon) {
                doanhThu += hd.getTongTien();
            }

            float doanhThuTB = soHoaDon > 0 ? doanhThu / soHoaDon : 0;

            modelThongKe.addRow(new Object[]{
                date,
                soHoaDon,
                vndFormat.format(doanhThu) + " ₫",
                vndFormat.format(doanhThuTB) + " ₫"
            });
        }
    }

    public JPanel getPanelThongKe() {
        return pnlMain;
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ThongKe().setVisible(true);
            }
        });
    }
}
