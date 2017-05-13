package com.giants.hd.desktop.model;

import com.giants3.hd.utils.StringUtils;
import com.giants3.hd.utils.entity.User;
import com.giants3.hd.utils.entity.WorkFlow;
import com.giants3.hd.utils.file.ImageUtils;
import com.google.inject.Inject;

/**
 * 生产进度数据模型
 */
public class WorkFlowModel extends BaseTableModel<WorkFlow> {

    public static String[] columnNames = new String[]{"流程序号", "流程名称",};
    public static int[] columnWidth = new int[]{80, 100, };


    public static final String USER_CODE = "userCode";
    public static final String USER_NAME = "userName";
    public static final String USER_CNAME = "userCName";
    public static final String CHECKER_CODE = "checkerCode";
    public static final String CHECKER_NAME = "checkerName";
    public static final String CHECKER_CNAME = "checkerCName";
    public static String[] fieldName = new String[]{"flowStep", "name"};

    public static Class[] classes = new Class[]{Object.class, Object.class};


    @Inject
    public WorkFlowModel() {
        super(columnNames, fieldName, classes, WorkFlow.class);
    }


    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (StringUtils.index(fieldName, USER_CODE) ==columnIndex || StringUtils.index(fieldName, USER_NAME) ==columnIndex|| StringUtils.index(fieldName, USER_CNAME) ==columnIndex)
            return true;
        if (StringUtils.index(fieldName, CHECKER_CODE) ==columnIndex || StringUtils.index(fieldName, CHECKER_NAME) ==columnIndex|| StringUtils.index(fieldName, CHECKER_CNAME) ==columnIndex)
            return true;
        return false;
    }


    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        if (columnIndex == StringUtils.index(fieldName, USER_CODE) || columnIndex == StringUtils.index(fieldName, USER_NAME)||columnIndex ==StringUtils.index(fieldName, USER_CNAME) ) {
            if (aValue instanceof User) {

                User user = (User) aValue;
                WorkFlow workFlow = getItem(rowIndex);
                setManager(user, workFlow);

                fireTableRowsUpdated(rowIndex, rowIndex);

            }
        }

        if (columnIndex == StringUtils.index(fieldName, CHECKER_CODE) || columnIndex == StringUtils.index(fieldName, CHECKER_NAME)||columnIndex ==StringUtils.index(fieldName, CHECKER_CNAME) ) {
            if (aValue instanceof User) {

                User user = (User) aValue;
                WorkFlow workFlow = getItem(rowIndex);
                setChecker(workFlow,user);
                fireTableRowsUpdated(rowIndex, rowIndex);

            }
        }
    }

    private void setManager(User user, WorkFlow workFlow) {
//        workFlow.userId = user.id;
//        workFlow.userCode = user.code;
//        workFlow.userCName = user.chineseName;
//        workFlow.userName = user.name;
    }


    private void setChecker(WorkFlow workFlow,User checker)
    {
//        workFlow.checkerId = checker.id;
//        workFlow.checkerCode = checker.code;
//        workFlow.checkerCName = checker.chineseName;
//        workFlow.checkerName = checker.name;
    }



    @Override
    public int[] getColumnWidth() {
        return columnWidth;
    }

    @Override
    public int getRowHeight() {
        return ImageUtils.MAX_MATERIAL_MINIATURE_HEIGHT;
    }
}
