package org.example.GUI;

import org.example.api.Dto.ParachuteDTO;
import org.example.api.Factory.ParachuteFactory;
import org.example.api.Misc.Archiver;
import org.example.persistence.Repositories.AbstractStorage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Comparator;

public class ParachuteGUI {

    private AbstractStorage<ParachuteDTO> storage;
    private JFrame frame;
    private JTextField costField, nameField, descriptionField;
    private JTable table;
    private DefaultTableModel tableModel;

    public ParachuteGUI() {
        storage = ParachuteFactory.getInstance();
    }

    public void createAndShowGUI() {
        frame = new JFrame("Parachute Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2));

        inputPanel.add(new JLabel("Cost:"));
        costField = new JTextField();
        inputPanel.add(costField);

        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Description:"));
        descriptionField = new JTextField();
        inputPanel.add(descriptionField);

        JButton addButton = new JButton("Add Parachute");
        addButton.addActionListener(new AddButtonListener());

        inputPanel.add(addButton);

        tableModel = new DefaultTableModel(new Object[] {"Cost", "Name", "Description"}, 0);
        table = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table);

        JPanel Panel = new JPanel();
        JButton readButton = new JButton("Read Data");
        readButton.addActionListener(new ReadButtonListener());

        JButton writeButton = new JButton("Write Data");
        writeButton.addActionListener(new WriteButtonListener());

        Panel.add(readButton);
        Panel.add(writeButton);

        JButton sortButton = new JButton("Sort Data");
        sortButton.addActionListener(new SortButtonListener());

        Panel.add(sortButton);

        JButton archiveButton = new JButton("Create Archive");
        archiveButton.addActionListener(new ArchiveButtonListener());

        Panel.add(archiveButton);

        frame.setLayout(new BorderLayout());
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(tableScrollPane, BorderLayout.CENTER);
        frame.add(Panel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String cost = costField.getText();
            String name = nameField.getText();
            String description = descriptionField.getText();

            if (!cost.isEmpty() && !name.isEmpty() && !description.isEmpty()) {
                try {
                    int costInt = Integer.parseInt(cost);
                    ParachuteDTO parachute = new ParachuteDTO(costInt, name, description);

                    boolean isDuplicate = storage.getList().stream()
                            .anyMatch(p -> p.getName().equalsIgnoreCase(name));

                    if (isDuplicate) {
                        JOptionPane.showMessageDialog(frame, "Parachute with the same name already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        storage.addToListStorage(parachute);
                        storage.addToMapStorage(costInt, parachute);

                        tableModel.addRow(new Object[] {parachute.getCost(), parachute.getName(), parachute.getDescription()});
                    }

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid cost format", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "All fields are required", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class ReadButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String[] options = {"parachute.txt", "parachute.xml", "parachute.json"};
            String fileType = (String) JOptionPane.showInputDialog(frame,
                    "Select file to read from", "Select File",
                    JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

            if (fileType != null) {
                new Thread(() -> {
                    try {
                        tableModel.setRowCount(0);
                        storage.getList().clear();

                        switch (fileType) {
                            case "parachute.txt":
                                storage.readFromFile(fileType);
                                break;
                            case "parachute.xml":
                                storage.setListStorage(storage.readFromXml(fileType));
                                break;
                            case "parachute.json":
                                storage.setListStorage(storage.readDataFromJsonFile(fileType));
                                break;
                            default:
                                throw new IOException("Unsupported file format");
                        }

                        updateTable();

                        JOptionPane.showMessageDialog(frame, "Data successfully loaded from " + fileType,
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(frame, "Error reading file: " + ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }).start();
            }
        }
    }

    private class WriteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new Thread(() -> {
                Thread txtWriter = new Thread(() -> storage.writeToFile("parachute.txt"));
                Thread xmlWriter = new Thread(() -> storage.writeToXml("parachute.xml", storage.getList()));
                Thread jsonWriter = new Thread(() -> storage.writeDataToJsonFile("parachute.json", storage.getList()));

                txtWriter.start();
                xmlWriter.start();
                jsonWriter.start();

                try {
                    txtWriter.join();
                    xmlWriter.join();
                    jsonWriter.join();
                } catch (InterruptedException ex) {
                    JOptionPane.showMessageDialog(frame, "Error during file writing: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                JOptionPane.showMessageDialog(frame, "Data written to all files", "Success", JOptionPane.INFORMATION_MESSAGE);
            }).start();
        }
    }

    private class SortButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String[] options = {"cost", "name", "description"};
            String field = (String) JOptionPane.showInputDialog(frame,
                    "Select sorting field", "Sort by",
                    JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

            if (field != null) {
                new Thread(() -> {
                    switch (field) {
                        case "cost":
                            storage.getList().sort(Comparator.comparingInt(ParachuteDTO::getCost));
                            break;
                        case "name":
                            storage.getList().sort(Comparator.comparing(ParachuteDTO::getName));
                            break;
                        case "description":
                            storage.getList().sort(Comparator.comparing(ParachuteDTO::getDescription));
                            break;
                    }
                    updateTable();
                }).start();
            }
        }
    }

    private class ArchiveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Archiver archiver = new Archiver();
            String[] files = {"parachute.txt", "parachute.json", "parachute.xml"};

            new Thread(() -> {
                try {
                    archiver.createZipArchive("ParachuteArchive.zip", files);
                    archiver.createJarArchive("ParachuteArchive.jar", files);
                    JOptionPane.showMessageDialog(frame, "Archive Created", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }).start();
        }
    }

    private void updateTable() {
        tableModel.setRowCount(0);
        for (ParachuteDTO parachute : storage.getList()) {
            tableModel.addRow(new Object[] {parachute.getCost(), parachute.getName(), parachute.getDescription()});
        }
    }
}
