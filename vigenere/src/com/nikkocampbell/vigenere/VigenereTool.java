/**
 * Author: Nikko Campbell
 * StudentID: 0505046
 * File: VigenereTool.java
 * 
 * A graphical implementation of the operations involving the Vigenere cipher
 * supplied in the Vigenere class.
 */
package com.nikkocampbell.vigenere;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;


public class VigenereTool {

	private JFrame frmVigenereTool;
	private JTextField keywordTextField;
	private JTextArea plaintextArea, ciphertextArea, ciphertextArea2,
			plaintextArea2;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton encryptRadioButton, decryptRadioButton;
	private JComboBox<String> maxKeyLengthComboBox, minKeyLengthComboBox;
	private JTextField txtKeyLengthKasiski;
	private JTextField txtKeyLengthEqn;
	private JTextField keyField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VigenereTool window = new VigenereTool();
					window.frmVigenereTool.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public VigenereTool() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmVigenereTool = new JFrame();
		frmVigenereTool.setTitle("Vigenere Tool");
		frmVigenereTool.setBounds(100, 100, 450, 530);
		frmVigenereTool.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmVigenereTool.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		JPanel encyrptDecryptPanel = new JPanel();
		tabbedPane.addTab("Encrypt/Decrypt", null, encyrptDecryptPanel, null);
		tabbedPane.setEnabledAt(0, true);
		encyrptDecryptPanel.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		encyrptDecryptPanel.add(panel, BorderLayout.CENTER);
		panel.setLayout(new FormLayout(new ColumnSpec[] { ColumnSpec
				.decode("429px:grow"), }, new RowSpec[] {
				FormFactory.PREF_ROWSPEC, FormFactory.PREF_ROWSPEC,
				FormFactory.PREF_ROWSPEC, FormFactory.PREF_ROWSPEC,
				FormFactory.PREF_ROWSPEC, FormFactory.PREF_ROWSPEC,
				RowSpec.decode("max(5dlu;pref)"), FormFactory.PREF_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.PREF_ROWSPEC,
				RowSpec.decode("pref:grow"), }));

		JLabel plaintextLabel = new JLabel("Plaintext");
		panel.add(plaintextLabel, "1, 1, left, center");

		plaintextArea = new JTextArea();
		plaintextArea.setWrapStyleWord(true);
		plaintextArea.setTabSize(4);
		plaintextArea.setLineWrap(true);
		plaintextArea.setRows(5);
		plaintextArea.setColumns(30);

		JScrollPane plaintextScrollPane = new JScrollPane(plaintextArea);
		plaintextScrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panel.add(plaintextScrollPane, "1, 2, center, center");

		JLabel keywordLabel = new JLabel("Keyword");
		panel.add(keywordLabel, "1, 3, left, center");

		keywordTextField = new JTextField();
		keywordTextField.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(keywordTextField, "1, 4, center, center");
		keywordTextField.setColumns(30);

		JLabel ciphertextLabel = new JLabel("Ciphertext");
		panel.add(ciphertextLabel, "1, 5, left, center");

		ciphertextArea = new JTextArea();
		ciphertextArea.setWrapStyleWord(true);
		ciphertextArea.setLineWrap(true);
		ciphertextArea.setTabSize(4);
		ciphertextArea.setRows(5);
		ciphertextArea.setColumns(30);

		JScrollPane ciphertextScrollPane = new JScrollPane(ciphertextArea);
		panel.add(ciphertextScrollPane, "1, 6, center, center");

		JPanel EncryptDecryptOptionPanel = new JPanel();
		panel.add(EncryptDecryptOptionPanel, "1, 8, center, default");
		EncryptDecryptOptionPanel.setLayout(new BoxLayout(
				EncryptDecryptOptionPanel, BoxLayout.X_AXIS));

		encryptRadioButton = new JRadioButton("Encrypt");
		buttonGroup.add(encryptRadioButton);
		encryptRadioButton.setSelected(true);
		EncryptDecryptOptionPanel.add(encryptRadioButton);

		decryptRadioButton = new JRadioButton("Decrypt");
		buttonGroup.add(decryptRadioButton);
		EncryptDecryptOptionPanel.add(decryptRadioButton);

		JButton btnGo = new JButton("GO!");
		btnGo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (encryptRadioButton.isSelected()) {
					encrypt();
				} else {
					decrypt();
				}
			}
		});
		panel.add(btnGo, "1, 10, center, default");

		JPanel cryptanalysisPanel = new JPanel();
		tabbedPane.addTab("Cryptanalysis", null, cryptanalysisPanel, null);
		cryptanalysisPanel.setLayout(new BorderLayout(0, 0));

		JPanel panel_1 = new JPanel();
		cryptanalysisPanel.add(panel_1);
		panel_1.setLayout(new FormLayout(new ColumnSpec[] { ColumnSpec
				.decode("429px:grow"), }, new RowSpec[] {
				FormFactory.PREF_ROWSPEC, FormFactory.PREF_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.PREF_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.PREF_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC, FormFactory.PREF_ROWSPEC,
				FormFactory.PREF_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("pref:grow"), }));

		JLabel lblCiphertext = new JLabel("Ciphertext");
		panel_1.add(lblCiphertext, "1, 1, left, center");

		JScrollPane ciphertextScrollPane2 = new JScrollPane();
		panel_1.add(ciphertextScrollPane2, "1, 2, center, center");

		ciphertextArea2 = new JTextArea();
		ciphertextArea2.setLineWrap(true);
		ciphertextArea2.setWrapStyleWord(true);
		ciphertextArea2.setTabSize(4);
		ciphertextArea2.setRows(5);
		ciphertextArea2.setColumns(30);
		ciphertextScrollPane2.setViewportView(ciphertextArea2);

		JPanel keyLengthPanel = new JPanel();
		panel_1.add(keyLengthPanel, "1, 4, center, default");
		keyLengthPanel
				.setLayout(new BoxLayout(keyLengthPanel, BoxLayout.X_AXIS));

		JLabel lblMinKeyLength = new JLabel("Min Key Length");
		keyLengthPanel.add(lblMinKeyLength);

		minKeyLengthComboBox = new JComboBox<String>();
		minKeyLengthComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {
				"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12",
				"13", "14", "15" }));
		minKeyLengthComboBox.setSelectedIndex(3);
		keyLengthPanel.add(minKeyLengthComboBox);

		Component horizontalStrut = Box.createHorizontalStrut(20);
		keyLengthPanel.add(horizontalStrut);

		JLabel lblMaxKeyLength = new JLabel("Max Key Length");
		keyLengthPanel.add(lblMaxKeyLength);

		maxKeyLengthComboBox = new JComboBox<String>();
		maxKeyLengthComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {
				"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12",
				"13", "14", "15" }));
		maxKeyLengthComboBox.setSelectedIndex(9);
		keyLengthPanel.add(maxKeyLengthComboBox);

		JButton estimateKeyLengthBtn = new JButton("Estimate Key Length");
		estimateKeyLengthBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				performKasiskiTest();
				performICKeylengthTest();

			}
		});
		panel_1.add(estimateKeyLengthBtn, "1, 6, center, default");

		JLabel lblKeyLengthKasiski = new JLabel(
				"Key Length (From Kasiski Test)");
		panel_1.add(lblKeyLengthKasiski, "1, 8");

		txtKeyLengthKasiski = new JTextField();
		panel_1.add(txtKeyLengthKasiski, "1, 9, center, default");
		txtKeyLengthKasiski.setColumns(30);

		JLabel lblKeyLengthEqn = new JLabel("Key Length (From Friedman Test)");
		panel_1.add(lblKeyLengthEqn, "1, 10");

		txtKeyLengthEqn = new JTextField();
		panel_1.add(txtKeyLengthEqn, "1, 11, center, default");
		txtKeyLengthEqn.setColumns(30);

		JButton estimateKeyBtn = new JButton("Estimate Key");
		estimateKeyBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				keyField.setText(Vigenere.estimateKey(
						ciphertextArea2.getText(),
						Integer.parseInt(txtKeyLengthKasiski.getText())));
			}
		});
		panel_1.add(estimateKeyBtn, "1, 13, center, default");

		JLabel lblKey = new JLabel("Key");
		panel_1.add(lblKey, "1, 15");

		keyField = new JTextField();
		panel_1.add(keyField, "1, 16, center, default");
		keyField.setColumns(30);

		JButton btnDecrypt = new JButton("Decrypt");
		btnDecrypt.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				crpytanalysisDecrypt();
			}
		});
		panel_1.add(btnDecrypt, "1, 18, center, default");

		JLabel lblPlaintext = new JLabel("Plaintext");
		panel_1.add(lblPlaintext, "1, 20");

		JScrollPane plaintextScrollPane2 = new JScrollPane();
		panel_1.add(plaintextScrollPane2, "1, 21, center, top");

		plaintextArea2 = new JTextArea();
		plaintextArea2.setLineWrap(true);
		plaintextArea2.setWrapStyleWord(true);
		plaintextArea2.setTabSize(4);
		plaintextArea2.setRows(5);
		plaintextArea2.setColumns(30);
		plaintextScrollPane2.setViewportView(plaintextArea2);

	}

	private void encrypt() {
		ciphertextArea.setText(Vigenere.encrypt(plaintextArea.getText(),
				keywordTextField.getText()));
	}

	private void decrypt() {
		plaintextArea.setText(Vigenere.decrypt(ciphertextArea.getText(),
				keywordTextField.getText()));
	}

	private void crpytanalysisDecrypt() {
		plaintextArea2.setText(Vigenere.decrypt(ciphertextArea2.getText(),
				keyField.getText()));
	}

	private void performKasiskiTest() {
		int minKeyLength = minKeyLengthComboBox.getSelectedIndex() + 1;
		int maxKeyLength = maxKeyLengthComboBox.getSelectedIndex() + 1;
		if (minKeyLength > maxKeyLength) {
			minKeyLength = 1;
			maxKeyLength = 10;
		}
		int keyLength = Vigenere.kasiski(ciphertextArea2.getText(),
				minKeyLength, maxKeyLength);
		txtKeyLengthKasiski.setText(Integer.toString(keyLength));
	}

	private void performICKeylengthTest() {
		txtKeyLengthEqn.setText(Double.toString(Vigenere
				.estimateKeyLength(ciphertextArea2.getText())));
	}

}
