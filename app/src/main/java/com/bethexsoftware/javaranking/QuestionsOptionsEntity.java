package com.bethexsoftware.javaranking;


/**
 * Created by Hardik on 7/6/2016.
 */
public class QuestionsOptionsEntity {
    private int qOptId;
    private int qOptQid;
    private byte qOptActive;
    private String qOptText;
    // private byte qIsAnswer;

   public int getqOptId() {
        return qOptId;
    }

    public void setqOptId(int qOptId) {
        this.qOptId = qOptId;
    }


    public int getqOptQid() {
        return qOptQid;
    }

    public void setqOptQid(int qOptQid) {
        this.qOptQid = qOptQid;
    }

    public byte getqOptActive() {
        return qOptActive;
    }

    public void setqOptActive(byte qOptActive) {
        this.qOptActive = qOptActive;
    }

    public String getqOptText() {
        return qOptText;
    }

    public void setqOptText(String qOptText) {
        this.qOptText = qOptText;
    }

    public byte getqIsAnswer() {
        return 0;
    }

    public void setqIsAnswer(byte qIsAnswer) {
        //this.qIsAnswer = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QuestionsOptionsEntity that = (QuestionsOptionsEntity) o;

        if (qOptId != that.qOptId) return false;
        if (qOptQid != that.qOptQid) return false;
        if (qOptActive != that.qOptActive) return false;
        //if (qIsAnswer != that.qIsAnswer) return false;
        if (qOptText != null ? !qOptText.equals(that.qOptText) : that.qOptText != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = qOptId;
        result = 31 * result + qOptQid;
        result = 31 * result + (int) qOptActive;
        result = 31 * result + (qOptText != null ? qOptText.hashCode() : 0);
        //result = 31 * result + (int) qIsAnswer;
        return result;
    }
}
