module org.example.studentsresponsestimesstatisticalsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires org.apache.logging.log4j;


    opens org.example.studentsresponsestimesstatisticalsystem to javafx.fxml;
    exports org.example.studentsresponsestimesstatisticalsystem;
    exports org.example.studentsresponsestimesstatisticalsystem.controller;
    opens org.example.studentsresponsestimesstatisticalsystem.controller to javafx.fxml;
}