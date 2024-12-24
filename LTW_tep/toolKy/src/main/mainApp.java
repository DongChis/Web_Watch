package main;

import controller.ChuKi_Controller;
import model.ChuKi_model;
import view.ChuKi_View;

import java.awt.event.ActionEvent;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class mainApp {
	public static void main(String[] args) {
        // Sử dụng SwingUtilities để đảm bảo giao diện được tạo trong Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            // Tạo JFrame chính
            JFrame frame = new JFrame("Ứng dụng Chữ Ký Điện Tử");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            // Thêm panel ChuKi_View vào JFrame
            frame.add(new ChuKi_View());
            
            // Đặt kích thước và hiển thị cửa sổ
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null); // Hiển thị cửa sổ ở giữa màn hình
            frame.setVisible(true);
        });
    }
}
