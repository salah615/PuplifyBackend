package com.example.puplify.Services;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import com.example.puplify.Entities.Note;
import com.example.puplify.Entities.Task;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExportService {

    public ByteArrayInputStream notesPDFReport(List<Note> notes) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Add content to the PDF document
            for (Note note : notes) {
                document.add(new Paragraph("Title: " + note.getTitle()));
                document.add(new Paragraph("Description: " + note.getDescription()));
                document.add(new Paragraph("Label: " + note.getLabel()));
                document.add(new Paragraph(" "));
            }

            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
    public ByteArrayInputStream tasksPDFReport(List<Task> tasks) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Add content to the PDF document
            for (Task task : tasks) {
                document.add(new Paragraph("Title: " + task.getTitle()));
                document.add(new Paragraph("Completed: " + task.isCompleted()));
                document.add(new Paragraph("Priority: " + task.getPriority()));
                document.add(new Paragraph("Due Date: " + task.getDueDate()));
                document.add(new Paragraph(" "));
            }

            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }


    public ByteArrayInputStream tasksExcelReport(List<Task> tasks) {
        String[] columns = {"Title", "Description", "Priority", "Due Date", "Created At", "Updated At", "Tags", "Course"};
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            // Create "Complete Tasks" sheet
            Sheet completeSheet = workbook.createSheet("Complete Tasks");
            createTaskSheet(completeSheet, columns, tasks.stream().filter(Task::isCompleted).collect(Collectors.toList()));

            // Create "Incomplete Tasks" sheet
            Sheet incompleteSheet = workbook.createSheet("Incomplete Tasks");
            createTaskSheet(incompleteSheet, columns, tasks.stream().filter(task -> !task.isCompleted()).collect(Collectors.toList()));

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Failed to export tasks to Excel file: " + e.getMessage());
        }
    }

    private static void createTaskSheet(Sheet sheet, String[] columns, List<Task> tasks) {
        // Create header row
        Row headerRow = sheet.createRow(0);
        for (int col = 0; col < columns.length; col++) {
            Cell cell = headerRow.createCell(col);
            cell.setCellValue(columns[col]);
        }

        // Create data rows
        int rowIdx = 1;
        for (Task task : tasks) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(task.getTitle());
            row.createCell(1).setCellValue(task.getDescription());
            row.createCell(2).setCellValue(task.getPriority().toString());
            row.createCell(3).setCellValue(task.getDueDate().toString());
            row.createCell(4).setCellValue(task.getCreatedAt().toString());
            row.createCell(5).setCellValue(task.getUpdatedAt().toString());
            row.createCell(6).setCellValue(String.join(", ", task.getTags()));
            row.createCell(7).setCellValue(task.getCourse().getName());

        }

        // Auto-size columns
        for (int col = 0; col < columns.length; col++) {
            sheet.autoSizeColumn(col);
        }
    }




}

