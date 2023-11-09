package com.kusithm.meetupd.domain.review.util;

import com.kusithm.meetupd.domain.review.entity.inner.ReviewChoice;

import java.util.Comparator;

public class ListComparatorKeywordCount implements Comparator<ReviewChoice> {
    @Override
    public int compare(ReviewChoice o1, ReviewChoice o2) {

        if(o1.getCount() > o2.getCount()){
            return -1;
        }else if(o1.getCount() < o2.getCount()){
            return 1;
        }else{
            return 0;
        }
    }
}
