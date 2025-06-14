package petadoptionapp;

import javax.swing.SwingUtilities;
import javax.swing.JFrame; // Import JFrame

public class Main {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			MainFrame frame = new MainFrame();
			// Set the frame to fullscreen
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			frame.setLocationRelativeTo(null); // Center the window (though less relevant for fullscreen)
			frame.setVisible(true);
		});
	}
}