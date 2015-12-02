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
        this.groupId = groupId;
        this.adminId = adminId;
        this.name = name;
    }

}
