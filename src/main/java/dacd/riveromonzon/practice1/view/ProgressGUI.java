package dacd.riveromonzon.practice1.view;

import javax.swing.*;
import java.awt.*;

public class ProgressGUI {
	private final JProgressBar progressBar;
	private final JLabel statusLabel;

	public ProgressGUI() {
		JFrame frame = new JFrame("Weather Data Processing");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(300, 100);
		frame.setLayout(new BorderLayout());

		statusLabel = new JLabel("Starting...", JLabel.CENTER);

		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);

		frame.add(statusLabel, BorderLayout.NORTH);
		frame.add(progressBar, BorderLayout.CENTER);

		frame.setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(ProgressGUI::new);
	}

	public void updateProgress(int value, String status) {
		progressBar.setValue(value);
		statusLabel.setText(status);
	}
}
