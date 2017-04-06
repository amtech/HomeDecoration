package com.giants.hd.desktop.model;

import com.giants.hd.desktop.utils.TableStructureUtils;
import com.giants.hd.desktop.viewImpl.Panel_WorkFlow_Config;
import com.giants3.hd.utils.entity.WorkFlowArrangeData;

import java.util.List;

/**
 * Created by davidleen29 on 2017/4/2.
 */
public class WorkFlowArrangeTableModel extends  BaseListTableModel<Panel_WorkFlow_Config.WorkFlowConfig> {




    public WorkFlowArrangeTableModel(Class   itemClass) {
        super(itemClass);

    }
    public WorkFlowArrangeTableModel(   Class itemClass,List<TableField> tableFields) {
        super(itemClass,tableFields);

    }


    public  void setWorkFlowArrangeData(WorkFlowArrangeData workFlowArrangeData)
    {


    }

    /**
     * Returns false.  This is the default implementation for all cells.
     *
     * @param rowIndex    the row being queried
     * @param columnIndex the column being queried
     * @return false
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
       String fieldName= getFieldName(columnIndex);
        if(fieldName.equals("workFlowStep")||fieldName.equals("workFlowName")) return  false;
        return true;
    }

    /**
     * This empty implementation is provided so users don't have to implement
     * this method if their data model is not editable.
     *
     * @param aValue      value to assign to cell
     * @param rowIndex    row of cell
     * @param columnIndex column of cell
     */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {



        Panel_WorkFlow_Config.WorkFlowConfig  item=getItem(rowIndex);

        boolean booleanValue=false;
        try{

            booleanValue=Boolean.valueOf(aValue.toString());
        }catch (Throwable t){t.printStackTrace();}
        switch (columnIndex)
        {
            case 2:

                item.tiejian=booleanValue;
                break;
            case 3:
                item.mujian=booleanValue;
                break;
            case 4:
                item.pu=booleanValue;
                break;

        }

        fireTableCellUpdated(rowIndex,columnIndex);


    }
}
