package cl.cnsv.referidosrrvv.util;

import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;

public class DataTablesParamUtility {

    public static JQueryDataTableParamModel getParam(HttpServletRequest request)
            throws UnsupportedEncodingException {
        if (request.getParameter("draw") != null && request.getParameter("draw") != "") {
            JQueryDataTableParamModel param = new JQueryDataTableParamModel();
            param.sup = request.getParameter("sup");
            param.draw = request.getParameter("draw");
            param.anio = request.getParameter("anio");
            param.mes = request.getParameter("mes");
            param.suc = request.getParameter("suc");
            String search = (request.getParameter("search[value]") == null)
                    ? ""
                    : request.getParameter("search[value]");
            param.sSearch = search;
            param.iDisplayStart = Integer.parseInt(request.getParameter("start"));
            param.iDisplayLength = Integer.parseInt(request.getParameter("length"));
            param.iSortColumnIndex = Integer.parseInt(request.getParameter("order[0][column]"));
            param.sSortDirection = request.getParameter("order[0][dir]");
            return param;
        } else {
            return null;
        }
    }
}
