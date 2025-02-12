package in.jgssolution.study.Models;

public class ExamQuestionModel {
    private String examquestionid;
    private String questionid;
    private String question;
    private String optionalone;
    private String optionaltwo;
    private String optionalthree;
    private String optionalfoure;
    private String answer;
    private String assesmentno;
    private String imagename;
    private String imagetype;
    private String images;
    private String batchno;
    private String batchname;
    private String examstatus;
    private String answerstatus;
    private String imagefiles;
    private String questiontypeno;
    private String noofquestion;

    public ExamQuestionModel(String examquestionid, String questionid, String question, String optionalone, String optionaltwo, String optionalthree, String optionalfoure,  String answer, String assesmentno, String imagetype, String imagename,  String images, String batchno, String batchname, String examstatus,  String answerstatus, String imagefiles, String questiontypeno, String noofquestion) {
        this.examquestionid = examquestionid;
        this.questionid = questionid;
        this.question = question;
        this.optionalone = optionalone;
        this.optionaltwo = optionaltwo;
        this.optionalfoure = optionalfoure;
        this.optionalthree = optionalthree;
        this.answer = answer;
        this.assesmentno = assesmentno;
        this.imagename = imagename;
        this.imagetype = imagetype;
        this.images = images;
        this.batchno = batchno;
        this.examstatus = examstatus;
        this.batchname = batchname;
        this.answerstatus = answerstatus;
        this.imagefiles = imagefiles;
        this.questiontypeno = questiontypeno;
        this.noofquestion = noofquestion;
    }

    public String getExamquestionid() {
        return examquestionid;
    }

    public String getQuestionid() {
        return questionid;
    }

    public String getQuestion() {
        return question;
    }

    public String getOptionalone() {
        return optionalone;
    }

    public String getOptionaltwo() {
        return optionaltwo;
    }

    public String getOptionalthree() {
        return optionalthree;
    }

    public String getOptionalfoure() {
        return optionalfoure;
    }

    public String getAnswer() {
        return answer;
    }

    public String getAssesmentno() {
        return assesmentno;
    }

    public String getImagename() {
        return imagename;
    }

    public String getImagetype() {
        return imagetype;
    }

    public String getBatchno() {
        return batchno;
    }

    public String getImages() {
        return images;
    }

    public String getAnswerstatus() {
        return answerstatus;
    }

    public String getImagefiles() {
        return imagefiles;
    }

    public String getExamstatus() {
        return examstatus;
    }

    public String getBatchname() {
        return batchname;
    }
    public String getQuestiontypeno() {
        return questiontypeno;
    }
    public String getNoofquestion() {
        return noofquestion;
    }

}
