package com.example.ndpt.chorescore;

/**
 * Created by Peter Thomson on 01/12/2015.
 *
 * Class for modeling a chore group from the parse database
 */
public class Group
{
    private String groupId;
    private String adminId;
    private String name;

    public Group(String groupId, String adminId, String name)
    {
        this.setGroupId(groupId);
        this.setAdminId(adminId);
        this.setName(name);
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
