//package com.mars.common.base;
//
//import io.swagger.annotations.ApiModelProperty;
//
///**
// * @author
// * @Date 2019/10/18 10:24
// */
//public class BasePageVO {
//    @ApiModelProperty(value = "第几页")
//    private Integer page;
//    @ApiModelProperty(value = "每页最大显示条数")
//    private Integer pageSize;
//    /**
//     * 起始行
//     */
//    private Integer startRow;
//    /**
//     * 末行
//     */
//    private Integer endRow;
//    /**
//     * 总数
//     */
//    private long total;
//    /**
//     * 总页数
//     */
//    private int pages;
//    @ApiModelProperty(value = "排序字段")
//    private String orderKey;
//    @ApiModelProperty(value = "排序方向（1正序，0倒序）")
//    private Integer sortType;
//    /**
//     * 排序方向（值：asc、desc）
//     */
//    private String sortTypeName;
//    @ApiModelProperty(value = "模糊查询关键字")
//    private String keyCode;
//
//    public Integer getPage() {
//        if (page == null) {
//            return 1;
//        }
//        return page;
//    }
//
//    public void setPage(Integer page) {
//        this.page = page;
//    }
//
//    public Integer getPageSize() {
//        if (pageSize == null) {
//            pageSize = MessageConstants.DEFAULT_PAGE_SIZE;
//        }
//        return pageSize;
//    }
//
//    public void setPageSize(Integer pageSize) {
//        this.pageSize = pageSize;
//        this.calculateStartAndEndRow();
//    }
//
//    public String getOrderKey() {
//        return orderKey;
//    }
//
//    public void setOrderKey(String orderKey) {
//        this.orderKey = orderKey;
//    }
//
//    public Integer getSortType() {
//        return sortType;
//    }
//
//    public void setSortType(Integer sortType) {
//        this.sortType = sortType;
//
//        if (sortType == null) {
//            return;
//        }
//
//        if ("1".equals(sortType)) {
//            this.sortTypeName = "asc";
//        } else {
//            this.sortTypeName = "desc";
//        }
//    }
//
//    public String getSortTypeName() {
//        return sortTypeName;
//    }
//
//    public void setSortTypeName(String sortTypeName) {
//        this.sortTypeName = sortTypeName;
//    }
//
//    public Integer getStartRow() {
//        this.calculateStartAndEndRow();
//        return startRow;
//    }
//
//    public void setStartRow(Integer startRow) {
//        this.startRow = startRow;
//    }
//
//    public Integer getEndRow() {
//        return endRow;
//    }
//
//    public void setEndRow(int endRow) {
//        this.endRow = endRow;
//    }
//
//    private void calculateStartAndEndRow() {
//        this.startRow = this.getPage() > 0 ? (this.getPage() - 1) * this.getPageSize() : 0;
//        this.endRow = this.startRow + this.getPageSize() * (this.getPage() > 0 ? 1 : 0);
//    }
//
//    public String getKeyCode() {
//        return keyCode;
//    }
//
//    public void setKeyCode(String keyCode) {
//        this.keyCode = keyCode;
//    }
//
//    public void setEndRow(Integer endRow) {
//        this.endRow = endRow;
//    }
//
//    public int getPages() {
//        return pages;
//    }
//
//    public void setPages(int pages) {
//        this.pages = pages;
//    }
//
//    public void setTotal(long total) {
//        this.total = total;
//        if (total == -1) {
//            pages = 1;
//            return;
//        }
//        if (pageSize > 0) {
//            pages = (int) (total / pageSize + ((total % pageSize == 0) ? 0 : 1));
//        } else {
//            pages = 0;
//        }
//    }
//}
