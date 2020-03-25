package com.video.liveshow.bean;

import java.util.List;

public class FriendsBean {

    public String current_team_num;
    public String lower_real_num;
    public String no_lower_real_num;
    public String teams_real_num;
    public String current_team_activation;
    public String lower_activation;
    public List<Team> lower_level;
    public String lower_real_total_num;
    public String lower_no_real_num;
    public String big_activation;
    public String small_activation;
    public String lower_num;
    public String higher_account;//我的上级

    public static class Team {

        public String uid;
        public String avatar;
        public String activation;
        public String team_num;
        public String team_activation;
        public String lower_activation;
        public String mobile;

    }

}
