package com.bethexsoftware.javaranking;
import java.util.Date;
/**
 * Created by Hardik on 8/15/2016.
 */
public class UserSummaryClientEntity {
    protected String usuid;
    protected Date usDate;
    protected int usCorrectAnswers;
    protected int usDayRank;
    protected int usDayScore;
    protected int usWeekRank;
    protected int usAllTimeRank;

    private int usSwiftReplyBonus;
    private int usDayRankBase;
    private int usWeekRankBase;
    private int usAllTimeRankBase;
    private String usDayGrade;
    private String usAllTimeGrade;

    public String getUsDayGrade() {
        return usDayGrade;
    }

    public void setUsDayGrade(String usDayGrade) {
        this.usDayGrade = usDayGrade;
    }

    public String getUsAllTimeGrade() {
        return usAllTimeGrade;
    }

    public void setUsAllTimeGrade(String usAllTimeGrade) {
        this.usAllTimeGrade = usAllTimeGrade;
    }




    public int getUsCorrectAnswers() {
        return usCorrectAnswers;
    }

    public void setUsCorrectAnswers(int usCorrectAnswers) {
        this.usCorrectAnswers = usCorrectAnswers;
    }

    public int getUsSwiftReplyBonus() {
        return usSwiftReplyBonus;
    }

    public void setUsSwiftReplyBonus(int usSwiftReplyBonus) {
        this.usSwiftReplyBonus = usSwiftReplyBonus;
    }



    public int getUsDayRankBase() {
        return usDayRankBase;
    }

    public void setUsDayRankBase(int usDayRankBase) {
        this.usDayRankBase = usDayRankBase;
    }

    public int getUsWeekRankBase() {
        return usWeekRankBase;
    }

    public void setUsWeekRankBase(int usWeekRankBase) {
        this.usWeekRankBase = usWeekRankBase;
    }

    public int getUsAllTimeRankBase() {
        return usAllTimeRankBase;
    }

    public void setUsAllTimeRankBase(int usAllTimeRankBase) {
        this.usAllTimeRankBase = usAllTimeRankBase;
    }


    public String getUsuid() {
        return usuid;
    }

    public void setUsuid(String usuid) {
        this.usuid = usuid;
    }

    public Date getUsDate() {
        return usDate;
    }

    public void setUsDate(Date usDate) {
        this.usDate = usDate;
    }

    public int getUsDayScore() {
        return usDayScore;
    }

    public void setUsDayScore(int usDayScore) {
        this.usDayScore = usDayScore;
    }

    public int getUsDayRank() {
        return usDayRank;
    }

    public void setUsDayRank(int usDayRank) {
        this.usDayRank = usDayRank;
    }

    public int getUsWeekRank() {
        return usWeekRank;
    }

    public void setUsWeekRank(int usWeekRank) {
        this.usWeekRank = usWeekRank;
    }

    public int getUsAllTimeRank() {
        return usAllTimeRank;
    }

    public void setUsAllTimeRank(int usAllTimeRank) {
        this.usAllTimeRank = usAllTimeRank;
    }



}
