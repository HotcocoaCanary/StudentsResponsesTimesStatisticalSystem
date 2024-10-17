package org.example.studentsresponsestimesstatisticalsystem.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import org.example.studentsresponsestimesstatisticalsystem.service.FileService;

public class AppController {

    @FXML
    public Label excelMassage;

    @FXML
    public Label txtMassage;

    @FXML
    public Label folderMassage;

    @FXML
    public Label exportMassage;

    @FXML
    private TextField excelFile;

    @FXML
    private TextField txtFile;

    @FXML
    private TextField folder;

    @FXML
    private TextField exportFileName;

    FileService fileService = FileService.getFileService();

    private final FileChooser fileChooser = new FileChooser();

    @FXML
    private void selectStudentExcelFile() {
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            excelFile.setText(file.getAbsolutePath());
            String massage = fileService.readStudentExcelFile(excelFile.getText());
            excelMassage.setText(massage);
        }
    }

    @FXML
    private void selectResponsesTimesFile() {
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);
        if (selectedFiles != null && !selectedFiles.isEmpty()) {
            List<String> filePaths = selectedFiles.stream()
                    .map(File::getAbsolutePath)
                    .collect(Collectors.toList());

            String paths = String.join(System.lineSeparator(), filePaths);
            String message = fileService.updateStudentResponsesTimes(filePaths);

            txtFile.setText(paths);
            txtMassage.setText(message != null ? message : "");
        }
    }



    @FXML
    private void selectFolder() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File directory = directoryChooser.showDialog(null);
        if (directory != null) {
            folder.setText(directory.getAbsolutePath());
            folderMassage.setText("设置导出位置为："+directory.getAbsolutePath());
        }
    }

    @FXML
    private void exportStudentExcelFile() {
        String fileName;
        String file;
        if (folder==null || folder.getText()==null|| folder.getText().isEmpty()){
            file ="C:\\studentsresponsestimesstatisticalsystem";
        }else{
            file= folder.getText();
        }
        if (exportFileName==null || exportFileName.getText()==null||exportFileName.getText().isEmpty()){
            fileName = "data";
        }else {
            fileName= exportFileName.getText();
        }
        String filePath = file+"\\"+fileName+".xlsx";
        String massage = fileService.statisticalTOExcel(filePath);
        exportMassage.setText(massage);
    }
}