package com.chuyu.utils.data;


import com.chuyu.utils.common.RecordMap;

/**
 * Created by Administrator on 2017-7-11.
 */
public class QueryParams {
    private Integer pageSize;
    private RecordMap whereMap;
    private RecordMap likeMap;
    private Integer offset;

    public QueryParams(Integer offset, Integer pageSize)
    {
        whereMap =  new RecordMap();
        likeMap = new RecordMap();
        this.offset = offset;
        this.pageSize = pageSize;
    }

    public QueryParams() {
        whereMap =  new RecordMap();
    }

    public static QueryParams instance(Integer offset, Integer pageSize)
    {
        return new QueryParams(offset,pageSize);
    }

    public Integer getPage()
    {
        if(pageSize>0)
        {
            return offset/pageSize + 1;
        }
       return 1;
    }

    public Integer getOffset()
    {
        return offset;
    }

    public RecordMap getWhereMap()
    {
        return whereMap;
    }

    public void setWhereMap(RecordMap whereMap)
    {
        this.whereMap = whereMap;
    }

    public Integer getPageSize()
    {
        return pageSize;
    }

    public void setPageSize(Integer pageSize)
    {
        this.pageSize = pageSize;
    }

    public RecordMap getLikeMap()
    {
        return likeMap;
    }

    public void setLikeMap(RecordMap likeMap)
    {
        this.likeMap = likeMap;
    }

}
