package org.example.studentsresponsestimesstatisticalsystem.util;

import org.apache.poi.ss.formula.eval.ErrorEval;
import org.example.studentsresponsestimesstatisticalsystem.member.Student;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author 张文博
 * @version 1.0.0
 * @title FileTool
 * @description 文件处理
 * @creat 2024/10/12 上午11:28
 **/
public class FileTool {

    public List<Student> getStudentList(String excelFilePath) throws IOException {
        List<Student> students = new ArrayList<>();
        try (InputStream inputStream = Files.newInputStream(Paths.get(excelFilePath))) {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet firstSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = firstSheet.iterator();

            int rowNumber = 0;
            while (iterator.hasNext()) {
                Row nextRow = iterator.next();
                if (rowNumber == 0) {
                    // 跳过标题行
                    rowNumber++;
                    continue;
                }
                Iterator<Cell> cellIterator = nextRow.cellIterator();
                Student student = new Student("", "", "");
                while (cellIterator.hasNext()) {
                    Cell nextCell = cellIterator.next();
                    int columnIndex = nextCell.getColumnIndex();
                    switch (columnIndex) {
                        case 0:
                            student.setStudentId(getCellValueAsString(nextCell));
                            break;
                        case 1:
                            student.setName(getCellValueAsString(nextCell));
                            break;
                        case 2:
                            student.setGrade(getCellValueAsString(nextCell));
                            break;
                        case 3:
                            if (nextCell.getCellType() == CellType.NUMERIC) {
                                student.addTime((int) nextCell.getNumericCellValue());
                            }
                            break;
                        default:
                            break;
                    }
                }
                students.add(student);
            }
        }
        System.out.println("读取到的数据为"+students);
        return students;
    }

    private String getCellValueAsString(Cell cell) {
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> Double.toString(cell.getNumericCellValue());
            case BOOLEAN -> Boolean.toString(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            case ERROR -> ErrorEval.getText(cell.getErrorCellValue());
            default -> "";
        };
    }

    public void updateStudentListData(List<Student> students, String attendanceFilePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(attendanceFilePath, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // 使用UTF-8编码读取，避免中文乱码
                System.out.println(line);
                if (line.equals("抢答记录")) {
                    continue;
                }
                if (line.contains("人")) {
                    int numStudents = line.split(" ")[2].charAt(0) - '0';
                    for (int i = 0; i < numStudents; i++) {
                        String number = reader.readLine();
                        String name = reader.readLine().trim();
                        String id = reader.readLine().trim();
                        String time = reader.readLine();
                        System.out.println("读取到数据为：序号：" + number + " 姓名：" + name + " 学号：" + id + " 抢答时间：" + time);
                        for (Student student : students) {
                            if (student.getName().equals(name) && student.getStudentId().equals(id)) {
                                student.addTime(1);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void getExcelFile(List<Student> students, String fileName) throws IOException {
        try(Workbook workbook = new XSSFWorkbook();
            FileOutputStream outputStream = new FileOutputStream(fileName)){
            // 创建一个工作表sheet
            Sheet sheet = workbook.createSheet("学生信息");
            // 创建表头行
            Row headerRow = sheet.createRow(0);
            String[] columns = {"学号", "姓名", "班级", "抢答次数"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }
            // 填充数据
            int rowNum = 1;
            for (Student student : students) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(student.getStudentId());
                row.createCell(1).setCellValue(student.getName());
                row.createCell(2).setCellValue(student.getGrade());
                row.createCell(3).setCellValue(student.getTime());
            }
            // 自动调整列宽
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }
            workbook.write(outputStream);
            System.out.println("导出Excel文件到："+fileName);
        }
    }
}
