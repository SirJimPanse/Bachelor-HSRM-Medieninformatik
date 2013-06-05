import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

class TRechner_GUI extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private TRechner rechner;
	private JButton display;
	private JPanel panel;

	public TRechner_GUI() {

		setLayout(new BorderLayout());
		rechner = new TRechnerImpl();
		display = new JButton(" ");
		display.setEnabled(false);
		
		panel = new JPanel();
		panel.setLayout(new GridLayout(5, 4));
		
		setUpGUI();
	}
	
	private void setUpGUI() {
		/**
		 * Diese Aktion fügt den String der Tastenaktion an das Ende des
		 * Anzeigetextes an.
		 */
		ActionListener insert = new ActionListener () {
			public void actionPerformed(ActionEvent event) {
				String input = event.getActionCommand();

				if (input.equals("="))
					display.setText(rechner.eval(display.getText()));

				else if (input != "<-" && input != "CLR")
					display.setText(display.getText() + input);

				else if (input == "<-")
					display.setText(display.getText().substring(0,display.getText().length() - 1));

				else if (input == "CLR")
					display.setText(" ");

				else
					display.setText("");
			}
		};

		addButton("CLR", insert);
		addButton("(", insert);
		addButton(")", insert);
		addButton("+", insert);

		addButton("7", insert);
		addButton("8", insert);
		addButton("9", insert);
		addButton("-", insert);

		addButton("4", insert);
		addButton("5", insert);
		addButton("6", insert);
		addButton("*", insert);

		addButton("1", insert);
		addButton("2", insert);
		addButton("3", insert);
		addButton("/", insert);

		addButton("0", insert);
		addButton(".", insert);
		addButton("<-", insert);
		addButton("=", insert);

		add(display, BorderLayout.NORTH);
		add(panel, BorderLayout.CENTER);
	}

	/**
	 * @param label Beschriftung der Taste
	 * @param listener der Ereignisempfänger für die Taste
	 */
	private void addButton(String label, ActionListener listener) {
		JButton button = new JButton(label);
		// Ereignisempfänger für die Schaltfläche
		button.addActionListener(listener);
		panel.add(button);
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setTitle("Calculator");
		frame.add(new TRechner_GUI());
		frame.setMinimumSize(new Dimension(250,300));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}