package com.art.app.web.bean.response;

import com.art.app.web.bean.MemberPointsRank;
import lombok.Data;

import java.util.List;

@Data
public class PointsContributionResult {

    /**
     * hasMore : false
     * rankList : [{"nickName":"xxx","avatar":"xxxx","rank":2,"points":11111}]
     */

    private boolean hasMore = false;
    private List<MemberPointsRank> rankList;
}
