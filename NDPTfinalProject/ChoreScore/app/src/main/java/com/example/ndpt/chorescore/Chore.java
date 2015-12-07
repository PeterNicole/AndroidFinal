package com.example.ndpt.chorescore;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * Created by Peter Thomson on 06/12/2015.
 * Class for modeling chores from the Parse database
 */
public class Chore
{
    //Chore class properties
    private String choreId;
    private String groupId;
    private String description;
    private Date dueDate;
    private int points;
    private String  completerId;
    private Boolean isApproved;
    private Bitmap proofImage;


    /**
     * Parameterized constructor for the chore class
     * @param choreId
     * @param groupId
     * @param description
     * @param dueDate
     * @param points
     * @param completerId
     * @param isApproved
     * @param proofImage
     */
    public Chore(String choreId, String groupId, String description, Date dueDate, int points, String completerId, Boolean isApproved, Bitmap proofImage)
    {
        this.setChoreId(choreId);
        this.setGroupId(groupId);
        this.setDescription(description);
        this.setDueDate(dueDate);
        this.setPoints(points);
        this.setCompleterId(completerId);
        this.setIsApproved(isApproved);
        this.setProofImage(proofImage);
    }

    public String getChoreId() {
        return choreId;
    }

    public void setChoreId(String choreId) {
        this.choreId = choreId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getCompleterId() {
        return completerId;
    }

    public void setCompleterId(String completerId) {
        this.completerId = completerId;
    }

    public Boolean getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Boolean isApproved) {
        this.isApproved = isApproved;
    }

    public Bitmap getProofImage() {
        return proofImage;
    }

    public void setProofImage(Bitmap proofImage) {
        this.proofImage = proofImage;
    }
}
