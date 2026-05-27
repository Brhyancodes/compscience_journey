package gui;

import models.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class DashboardFrame extends JFrame {

    private JTextField idField;
    private JTextField nameField;
    private JTextField ageField;
    private JTextField emailField;

    private JTable table;
    private DefaultTableModel model;

    private JComboBox<String> courseBox;
    private JComboBox<String> gradeBox;

    private ArrayList<Student> students = new ArrayList<>();

    public DashboardFrame() {

        setTitle("Student Management System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JLabel title = new JLabel(
                "Student Management System",
                SwingConstants.CENTER
        );

        title.setFont(new Font("Segoe UI", Font.BOLD, 24));

        mainPanel.add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 10));

        idField = new JTextField();
        nameField = new JTextField();
        ageField = new JTextField();
        emailField = new JTextField();

        courseBox = new JComboBox<>(new String[]{
            "Java Programming",
            "Database Systems",
            "Web Development"
        });

        gradeBox = new JComboBox<>(new String[]{
            "A",
            "B",
            "C",
            "D",
            "F"
        });

        formPanel.add(new JLabel("Student ID"));
        formPanel.add(idField);

        formPanel.add(new JLabel("Student Name"));
        formPanel.add(nameField);

        formPanel.add(new JLabel("Age"));
        formPanel.add(ageField);

        formPanel.add(new JLabel("Email"));
        formPanel.add(emailField);

        formPanel.add(new JLabel("Course"));
        formPanel.add(courseBox);

        formPanel.add(new JLabel("Grade"));
        formPanel.add(gradeBox);

        JButton addButton = new JButton("Add Student");
        JButton updateButton = new JButton("Update Student");
        JButton enrollButton = new JButton("Enroll Course");
        JButton gradeButton = new JButton("Assign Grade");

        formPanel.add(addButton);
        formPanel.add(updateButton);

        formPanel.add(enrollButton);
        formPanel.add(gradeButton);

        mainPanel.add(formPanel, BorderLayout.WEST);

        model = new DefaultTableModel();

        model.addColumn("ID");
        model.addColumn("Name");
        model.addColumn("Age");
        model.addColumn("Email");
        model.addColumn("Course");
        model.addColumn("Grade");

        table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);

        addButton.addActionListener(e -> addStudent());

        updateButton.addActionListener(e -> updateStudent());

        enrollButton.addActionListener(e -> enrollStudent());

        gradeButton.addActionListener(e -> assignGrade());

        table.getSelectionModel().addListSelectionListener(e -> {
            loadSelectedStudent();
        });

        setVisible(true);
    }

    private void addStudent() {

        try {

            String id = idField.getText();
            String name = nameField.getText();
            int age = Integer.parseInt(ageField.getText());
            String email = emailField.getText();

            if (id.isEmpty() || name.isEmpty() || email.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please fill all fields.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            Student student = new Student(id, name, age, email);

            students.add(student);

            model.addRow(new Object[]{
                id,
                name,
                age,
                email,
                courseBox.getSelectedItem(),
                "-"
            });

            clearFields();

            JOptionPane.showMessageDialog(
                    this,
                    "Student added successfully!"
            );

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Age must be a number.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void updateStudent() {

        int row = table.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a student."
            );

            return;
        }

        model.setValueAt(nameField.getText(), row, 1);
        model.setValueAt(ageField.getText(), row, 2);
        model.setValueAt(emailField.getText(), row, 3);

        JOptionPane.showMessageDialog(
                this,
                "Student updated successfully!"
        );
    }

    private void enrollStudent() {

        int row = table.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a student."
            );

            return;
        }

        model.setValueAt(courseBox.getSelectedItem(), row, 4);

        JOptionPane.showMessageDialog(
                this,
                "Course enrollment successful!"
        );
    }

    private void assignGrade() {

        int row = table.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a student."
            );

            return;
        }

        model.setValueAt(gradeBox.getSelectedItem(), row, 5);

        JOptionPane.showMessageDialog(
                this,
                "Grade assigned successfully!"
        );
    }

    private void loadSelectedStudent() {

        int row = table.getSelectedRow();

        if (row != -1) {

            idField.setText(model.getValueAt(row, 0).toString());

            nameField.setText(model.getValueAt(row, 1).toString());

            ageField.setText(model.getValueAt(row, 2).toString());

            emailField.setText(model.getValueAt(row, 3).toString());
        }
    }

    private void clearFields() {

        idField.setText("");
        nameField.setText("");
        ageField.setText("");
        emailField.setText("");
    }
}
