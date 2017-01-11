package maestrovs.androidofficiant.essences;

/**
 * Created by userd088 on 28.04.2016.
 */
public class SecMembership extends TableDB {

    int userId=0;
    int groupId=0;

    public SecMembership(int id, int grpId, int  userId, int groupId ) {
        super(id, grpId);
        this.userId=userId;
        this.groupId=groupId;
    }

    public int getUserId() {
        return userId;
    }

    public int getGroupId() {
        return groupId;
    }
}
