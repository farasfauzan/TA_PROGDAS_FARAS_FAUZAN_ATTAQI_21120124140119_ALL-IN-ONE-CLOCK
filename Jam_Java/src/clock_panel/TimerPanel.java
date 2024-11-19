package clock_panel;

import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.time.Duration;
import javax.swing.*;

public class TimerPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	DecimalFormat formatter = new DecimalFormat("00");
	long hours, minutes, seconds;
	long inputTime, lastTickTime, runningTime, timeLeft;

	JLabel labelTime, h, min, sec;
	JComboBox<String> hourComboBox, minutesComboBox, secondsComboBox;
	JButton reset, start, pause;

	Timer timer;
	JTextField manualInput;

	public TimerPanel() {
		hours = minutes = seconds = 0;

		setLayout(null);
		setSize(300, 400);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				createAndShowGUI();
			}
		});
	}

	JButton inputManual;

	public void createAndShowGUI() {
		manualInput = new JTextField();
		manualInput.setBounds(50, 170, 200, 30);
		manualInput.setFont(new Font("Arial", Font.PLAIN, 16));
		add(manualInput);

		labelTime = new JLabel();
		changeLabelTimer();
		labelTime.setFont(new Font("Arial", Font.PLAIN, 28));
		labelTime.setBounds(50, 20, 200, 80);
		add(labelTime);

		manualInput.addActionListener(new ActionListener() {
										  @Override
										  public void actionPerformed(ActionEvent e) {
											  String timeInput = manualInput.getText();
											  String[] timeParts = timeInput.split(":");
											  if (timeParts.length == 3) {
												  hours = Integer.parseInt(timeParts[0]);
												  minutes = Integer.parseInt(timeParts[1]);
												  seconds = Integer.parseInt(timeParts[2]);
												  changeLabelTimer();
											  }
										  }
									  });


		hourComboBox = new JComboBox<String>();
		for (int i = 0; i <= 24; i++) {
			hourComboBox.addItem(formatter.format(i));
		}
		hourComboBox.setBounds(50, 120, 50, 40);
		hourComboBox.addActionListener(this);
		hourComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
		((JLabel) hourComboBox.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
		add(hourComboBox);

		minutesComboBox = new JComboBox<String>();
		for (int i = 0; i < 60; i++) {
			minutesComboBox.addItem(formatter.format(i));
		}
		minutesComboBox.setBounds(100, 120, 50, 40);
		minutesComboBox.addActionListener(this);
		minutesComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
		((JLabel) minutesComboBox.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
		add(minutesComboBox);

		secondsComboBox = new JComboBox<String>();
		for (int i = 0; i < 60; i++) {
			secondsComboBox.addItem(formatter.format(i));
		}
		secondsComboBox.setBounds(150, 120, 50, 40);
		secondsComboBox.addActionListener(this);
		secondsComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
		((JLabel) secondsComboBox.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
		add(secondsComboBox);

		h = new JLabel("h");
		h.setBounds(70, 165, 20, 20);
		h.setFont(new Font("Arial", Font.ITALIC, 13));
		add(h);

		min = new JLabel("min");
		min.setBounds(115, 165, 40, 20);
		min.setFont(new Font("Arial", Font.ITALIC, 13));
		add(min);

		sec = new JLabel("sec");
		sec.setBounds(165, 165, 40, 20);
		sec.setFont(new Font("Arial", Font.ITALIC, 13));
		add(sec);

		Image resetImage = Toolkit.getDefaultToolkit().getImage("reset-icon.png").getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		reset = new JButton(new ImageIcon(resetImage));
		reset.setBounds(30, 220, resetImage.getWidth(getParent()), resetImage.getHeight(getParent()));
		reset.setBorder(BorderFactory.createEmptyBorder());
		reset.setContentAreaFilled(false);
		reset.addActionListener(this);
		reset.setEnabled(false);
		add(reset);

		Image startImage = Toolkit.getDefaultToolkit().getImage("play-icon.png").getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		start = new JButton(new ImageIcon(startImage));
		start.setBounds(100, 220, startImage.getWidth(getParent()), startImage.getHeight(getParent()));
		start.setBorder(BorderFactory.createEmptyBorder());
		start.setContentAreaFilled(false);
		start.addActionListener(this);
		add(start);

		Image pauseImage = Toolkit.getDefaultToolkit().getImage("pause-icon.jpg").getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		pause = new JButton(new ImageIcon(pauseImage));
		pause.setBounds(170, 220, pauseImage.getWidth(getParent()), pauseImage.getHeight(getParent()));
		pause.setBorder(BorderFactory.createEmptyBorder());
		pause.setContentAreaFilled(false);
		pause.addActionListener(this);
		pause.setEnabled(false);
		add(pause);
	}

	public long inputTimeInMilliseconds() {
		return (hours * 60 * 60 * 1000) + (minutes * 60 * 1000) + (seconds * 1000);
	}

	public void update() {
		Duration duration = Duration.ofMillis(timeLeft);
		hours = duration.toHours();
		duration = duration.minusHours(hours);
		minutes = duration.toMinutes();
		duration = duration.minusMinutes(minutes);
		seconds = duration.toMillis() / 1000;
	}

	public void changeLabelTimer() {
		labelTime.setText(formatter.format(hours) + " : " + formatter.format(minutes) + " : " + formatter.format(seconds));
	}

	public void reset() {
		hours = minutes = seconds = 0;
		changeLabelTimer();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == hourComboBox) {
			hours = Integer.parseInt(hourComboBox.getItemAt(hourComboBox.getSelectedIndex()));
			changeLabelTimer();
		}

		if (e.getSource() == minutesComboBox) {
			minutes = Integer.parseInt(minutesComboBox.getItemAt(minutesComboBox.getSelectedIndex()));
			changeLabelTimer();
		}

		if (e.getSource() == secondsComboBox) {
			seconds = Integer.parseInt(secondsComboBox.getItemAt(secondsComboBox.getSelectedIndex()));
			changeLabelTimer();
		}

		if (e.getSource() == start) {
			reset.setEnabled(true);
			pause.setEnabled(true);
			start.setEnabled(false);
			hourComboBox.setEnabled(false);
			minutesComboBox.setEnabled(false);
			secondsComboBox.setEnabled(false);

			inputTime = inputTimeInMilliseconds();
			lastTickTime = System.currentTimeMillis();

			timer = new Timer(1000, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					runningTime = System.currentTimeMillis() - lastTickTime;
					timeLeft = inputTime - runningTime;

					update();
					changeLabelTimer();

					if (timeLeft <= 0) {
						Toolkit.getDefaultToolkit().beep();

						String timerName = manualInput.getText().trim();
						if (timerName.isEmpty()) {
							timerName = "Tanpa Nama";  // Default name if empty
						}

						JOptionPane.showMessageDialog(
								null,
								"Waktu " + timerName + " selesai!",
								"Pemberitahuan",
								JOptionPane.INFORMATION_MESSAGE
								);
						timer.stop();
						reset();
						reset.setEnabled(false);
						pause.setEnabled(false);
						start.setEnabled(true);
						hourComboBox.setEnabled(true);
						minutesComboBox.setEnabled(true);
						secondsComboBox.setEnabled(true);
					}
				}
			});

			timer.start();
		}

		if (e.getSource() == pause) {
			timer.stop();
			pause.setEnabled(false);
			start.setEnabled(true);
		}

		if (e.getSource() == reset) {
			timer.stop();
			reset();
			reset.setEnabled(false);
			pause.setEnabled(false);
			start.setEnabled(true);
			hourComboBox.setSelectedIndex(0);
			minutesComboBox.setSelectedIndex(0);
			secondsComboBox.setSelectedIndex(0);
			hourComboBox.setEnabled(true);
			minutesComboBox.setEnabled(true);
			secondsComboBox.setSelectedIndex(0);				//resetting comboboxes to their initial values
			hourComboBox.setEnabled(true);
			minutesComboBox.setEnabled(true);
			secondsComboBox.setEnabled(true);


			//enabling comboboxes after timer is completed



		}
	}

}
