package petadoptionapp;

import javax.swing.SwingUtilities;
import javax.swing.JFrame; // Import JFrame

public class Main {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			MainFrame frame = new MainFrame();
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
		});
	}
}