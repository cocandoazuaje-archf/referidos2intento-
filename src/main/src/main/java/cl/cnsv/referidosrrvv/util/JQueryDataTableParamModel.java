package cl.cnsv.referidosrrvv.util;

import java.lang.reflect.Field;

public class JQueryDataTableParamModel {

    /// <summary>
    /// Request sequence number sent by DataTable, same value must be returned in
    /// response
    /// </summary>
    public String draw;

    /// <summary>
    /// Text used for filtering
    /// </summary>
    public String sSearch;

    /// <summary>
    /// Number of records that should be shown in table
    /// </summary>
    public int iDisplayLength;

    /// <summary>
    /// First record that should be shown(used for paging)
    /// </summary>
    public int iDisplayStart;

    /// <summary>
    /// Number of columns in table
    /// </summary>
    public int iColumns;

    /// <summary>
    /// Number of columns that are used in sorting
    /// </summary>
    public int iSortingCols;

    /// <summary>
    /// Index of the column that is used for sorting
    /// </summary>
    public int iSortColumnIndex;

    /// <summary>
    /// Sorting direction "asc" or "desc"
    /// </summary>
    public String sSortDirection;

    /// <summary>
    /// Comma separated list of column names
    /// </summary>
    public String sColumns;

    public String sup;

    public String anio;

    public String mes;

    public String suc;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String newLine = System.getProperty("line.separator");

        result.append(this.getClass().getName());
        result.append(" Object {");
        result.append(newLine);

        // determine fields declared in this class only (no fields of superclass)
        Field[] fields = this.getClass().getDeclaredFields();

        // print field names paired with their values
        for (Field field : fields) {
            result.append("  ");
            try {
                result.append(field.getName());
                result.append(": ");
                // requires access to private field:
                result.append(field.get(this));
            } catch (IllegalAccessException ex) {
                System.out.println(ex);
            }
            result.append(newLine);
        }
        result.append("}");

        return result.toString();
    }
}
