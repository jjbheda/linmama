package com.linmama.dinning.data.report;

import com.linmama.dinning.bean.SaleReportBean;

/**
 * Created by jingkang on 2017/3/13
 */

public class SaleReportContract {

    public interface SaleReportView {
        void saleReportSuccess(SaleReportBean bean);

        void saleReportFail(String failMsg);
    }

    public interface SaleReportPresenter {
        void trunToReport();
    }
}
