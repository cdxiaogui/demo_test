package cd.xiaogui.demo.tools;
import java.io.Serializable;
import java.util.Date;

//@JsonIgnoreProperties( { "index", "pageSize", "startDate", "endDate", "sort" })
public class SearchParamemter implements Serializable { 

    private static final long serialVersionUID = 1L;

    private int index = 1;

    private int pageSize = 25;

    private Date startDate;

    private Date endDate;

    private String sort = "";

    private int startRow;

    private int endRow;

    private boolean page=false;//分页 默认部分页
    /**
     * 总条数
     */
    private int totalItem;
    /**
     * 总页数
     */
    protected int totalPage;

    public int getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(int totalItem) {
        this.totalItem = totalItem;
    }

    public int getTotalPage() {
        if(this.pageSize==0||this.totalItem==0){
            return 0;
        }
        return totalItem % pageSize == 0 ? totalItem / pageSize : (totalItem / pageSize) + 1;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public boolean getPage() {
        return page;
    }

    public void setPage(boolean page) {
        this.page = page;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getEndRow() {
        return endRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

}
