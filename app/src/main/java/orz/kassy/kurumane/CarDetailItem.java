package orz.kassy.kurumane;

/**
 * CustomListItem
 * @author kashimoto
 *
 */
public class CarDetailItem {
	private String mTitle;
	private String mDetail;

	public CarDetailItem(String title, String detail) {
        mTitle = title;
        mDetail = detail;
	}

    public String getDetail() {
        return mDetail;
    }

    public void setDetail(String detail) {
        mDetail = detail;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }
}
