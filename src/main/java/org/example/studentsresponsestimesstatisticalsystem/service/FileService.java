package org.example.studentsresponsestimesstatisticalsystem.service;

import lombok.extern.log4j.Log4j2;
import org.example.studentsresponsestimesstatisticalsystem.member.Student;
import org.example.studentsresponsestimesstatisticalsystem.util.FileTool;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Canary
 * @version 1.0.0
 * @title FileService
 * @description
 * @creat 2024/10/12 下午1:24
 **/
@Log4j2
public class FileService {

    private final FileTool fileTool;

    public static FileService fileService;

    private static List<Student> students;

    private FileService(FileTool fileTool){
        this.fileTool=fileTool;
    }

    public static FileService getFileService(){
        log.info("获取文件服务器对象");
        if(fileService==null){
            return new FileService(new FileTool());
        }
        return fileService;
    }

    public String readStudentExcelFile(String excelFilePath){
        try{
            log.info("清空当前学生列表，读取新列表：{}", excelFilePath);
            clearStudents();
            students = fileTool.getStudentList(excelFilePath);
            return "读取列表"+excelFilePath+"成功";
        } catch (Exception e) {
            log.error("读取新列表：{}错误，错误信息：{}", excelFilePath, e.getMessage());
            return "读取列表失败，更多错误信息请查看项目下’logs/error.log‘文件";
        }

    }

    public String updateStudentResponsesTimes(List<String> txtFilePaths){
        if(students==null){
            log.error("在当前学生数据为空时调用更新学生抢答次数方法，文件路径：{}", txtFilePaths);
            return "当前学生数据为空，你应该先上传学生信息表格";
        }else{
            try{
                for (String txtFilePath : txtFilePaths) {
                    log.info("更新学生抢答次数，文件路径：{}", txtFilePath);
                    fileTool.updateStudentListData(students,txtFilePath);
                }
                return "更新数据成功，可以继续选择下一个抢答数据文件。本次读取了"+txtFilePaths.size()+"个文件";
            } catch (Exception e) {
                return "更新数据失败，更多错误信息请查看项目下’logs/error.log‘文件";
            }

        }
    }

    public String statisticalTOExcel(String filePath){
        try {
            log.info("导出数据到Excel：{}",filePath);
            fileTool.getExcelFile(students,filePath);
            return "成功导出文件到："+filePath;
        } catch (Exception e) {
            log.error("导出数据到Excel：{}错误，错误信息：{}", filePath, e.getMessage());
            return "导出文件失败，更多错误信息请查看项目下’logs/error.log‘文件";
        }
    }

    private void clearStudents(){
        if(students!=null){
            students.clear();
        }else {
            students=new ArrayList<>();
        }
    }
}
