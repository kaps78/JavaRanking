package com.bethexsoftware.javaranking;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Hardik on 7/6/2016.
 */
public class QuestionsEntity implements Serializable{

    private int qid;
    private byte qActive;
    private String qText;
    private int qTimeRemaining;


    private Set<com.bethexsoftware.javaranking.QuestionsOptionsEntity> questionsOptions = new HashSet<com.bethexsoftware.javaranking.QuestionsOptionsEntity>();

    public int getQid() {
        return qid;
    }

    public void setQid(int qid) {
        this.qid = qid;
    }

    public Set<QuestionsOptionsEntity> getQuestionsOptions(){
        return this.questionsOptions;
    }

    public void setQuestionsOptions(Set<QuestionsOptionsEntity> questionsOptions)
    {
        this.questionsOptions = questionsOptions;
    }

    public byte getqActive() {
        return qActive;
    }

    public void setqActive(byte qActive) {
        this.qActive = qActive;
    }

    public String getqText() {
        return qText;
    }

    public void setqText(String qText) {
        this.qText = qText;
    }

    public int getqTimeRemaining() {
        return qTimeRemaining;
    }

    public void setqTimeRemaining(int qtimeRemaining) {
        qTimeRemaining = qtimeRemaining;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QuestionsEntity that = (QuestionsEntity) o;

        if (qid != that.qid) return false;
        if (qActive != that.qActive) return false;
        if (qText != null ? !qText.equals(that.qText) : that.qText != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = qid;
        result = 31 * result + (int) qActive;
        result = 31 * result + (qText != null ? qText.hashCode() : 0);
        return result;
    }
}
