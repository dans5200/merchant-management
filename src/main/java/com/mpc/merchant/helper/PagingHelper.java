package com.mpc.merchant.helper;

public class PagingHelper {
    public Integer countMaxPage(Integer countData, Integer viewPerPage){
        Integer maxPage = 0;

        if (countData < viewPerPage) {
            maxPage = 1;
        }else if (countData % viewPerPage == 0){
            maxPage = countData / viewPerPage;
        }else{
            maxPage = ((countData - (countData % viewPerPage)) / viewPerPage) + 1;
        }

        return maxPage;
    }
}
