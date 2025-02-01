package com.example.kacper.inzynierka.email;

// Importing required classes
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Annotations
@Data
@AllArgsConstructor
@NoArgsConstructor

// Class
public class EmailDetails {

    // Class data members
    private String recipient;
    private String msgBody;
    private String subject;
    private String attachment;

    public void setMsgBody(String max_points, String user_points, int negative_points, double percent, String test_name, String test_day, double pass_percent, String fullname, String message) {
        String msgBody = "<h1>Witaj "+fullname
                +"!</h1><h4> Wyniki z testu: "+test_name+" z dnia: "+test_day+"</h4><br>"
                +"<p>Punktów uzyskanych: <b>"+user_points+"</b> na <b>"+max_points+"</b> możliwych do zdobycia</p>"
                +"<p>Punkty ujemne: <b>"+negative_points+"</p>"
                + "<p>Wynik procentowy: <b>"+percent+"%</b></p>"
                +"<p>Zaliczenie od: <b>"+pass_percent+"%</b></p>"
                +"<p>Status: <b>"+isPass(percent,pass_percent)+"</b></p>"
                +"<p>Wiadomość od egzaminujacego:</p>"
                +"<p><i>"+message+"</i></p>";
        this.msgBody = msgBody;
    }

    public void setSubject(String test_name, String test_day, String userfullname) {
        String subject = "Wyniki z testu: "+test_name+" z dnia: "+test_day+" dla: "+userfullname;
        this.subject = subject;
    }

    public String isPass(double percent, double pass_percent){
        if(percent > pass_percent){
            return "<span style='color: green;'><b>Zaliczono!</b>";
        }
        return "<span style='color: red;'><b>Nie zaliczono!</b>";
    }

}
