//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package jxl.write.biff;

import jxl.biff.BuiltInName;
import jxl.biff.IntegerHelper;
import jxl.biff.StringHelper;
import jxl.biff.Type;
import jxl.biff.WritableRecordData;
import jxl.common.Logger;

class NameRecord extends WritableRecordData {
    private static Logger logger;
    private byte[] data;
    private String name;
    private BuiltInName builtInName;
    private int index;
    private int sheetRef = 0;
    private boolean modified;
    private NameRecord.NameRange[] ranges;
    private static final int cellReference = 58;
    private static final int areaReference = 59;
    private static final int subExpression = 41;
    private static final int union = 16;
    private static final NameRecord.NameRange EMPTY_RANGE;

    public NameRecord(jxl.read.biff.NameRecord sr, int ind) {
        super(Type.NAME);
        this.data = sr.getData();
        this.name = sr.getName();
        if(this.name==null)
        {
            this.name="";
        }

        this.sheetRef = sr.getSheetRef();
        this.index = ind;
        this.modified = false;
        jxl.read.biff.NameRecord.NameRange[] r = sr.getRanges();
        this.ranges = new NameRecord.NameRange[r.length];

        for(int i = 0; i < this.ranges.length; ++i) {
            this.ranges[i] = new NameRecord.NameRange(r[i]);
        }

    }

    NameRecord(String theName, int theIndex, int extSheet, int theStartRow, int theEndRow, int theStartCol, int theEndCol, boolean global) {
        super(Type.NAME);
        this.name = theName;
        this.index = theIndex;
        this.sheetRef = global?0:this.index + 1;
        this.ranges = new NameRecord.NameRange[1];
        this.ranges[0] = new NameRecord.NameRange(extSheet, theStartRow, theEndRow, theStartCol, theEndCol);
        this.modified = true;
    }

    NameRecord(BuiltInName theName, int theIndex, int extSheet, int theStartRow, int theEndRow, int theStartCol, int theEndCol, boolean global) {
        super(Type.NAME);
        this.builtInName = theName;
        this.index = theIndex;
        this.sheetRef = global?0:this.index + 1;
        this.ranges = new NameRecord.NameRange[1];
        this.ranges[0] = new NameRecord.NameRange(extSheet, theStartRow, theEndRow, theStartCol, theEndCol);
    }

    NameRecord(BuiltInName theName, int theIndex, int extSheet, int theStartRow, int theEndRow, int theStartCol, int theEndCol, int theStartRow2, int theEndRow2, int theStartCol2, int theEndCol2, boolean global) {
        super(Type.NAME);
        this.builtInName = theName;
        this.index = theIndex;
        this.sheetRef = global?0:this.index + 1;
        this.ranges = new NameRecord.NameRange[2];
        this.ranges[0] = new NameRecord.NameRange(extSheet, theStartRow, theEndRow, theStartCol, theEndCol);
        this.ranges[1] = new NameRecord.NameRange(extSheet, theStartRow2, theEndRow2, theStartCol2, theEndCol2);
    }

    public byte[] getData() {
        if(this.data != null && !this.modified) {
            return this.data;
        } else {
            boolean NAME_HEADER_LENGTH = true;
            boolean AREA_RANGE_LENGTH = true;
            boolean AREA_REFERENCE = true;
            int detailLength;
            if(this.ranges.length > 1) {
                detailLength = this.ranges.length * 11 + 4;
            } else {
                detailLength = 11;
            }

            int length = 15 + detailLength;
            length += this.builtInName != null?1:this.name.length();
            this.data = new byte[length];
            int options = 0;
            if(this.builtInName != null) {
                options |= 32;
            }

            IntegerHelper.getTwoBytes(options, this.data, 0);
            this.data[2] = 0;
            if(this.builtInName != null) {
                this.data[3] = 1;
            } else {
                this.data[3] = (byte)this.name.length();
            }

            IntegerHelper.getTwoBytes(detailLength, this.data, 4);
            IntegerHelper.getTwoBytes(this.sheetRef, this.data, 6);
            IntegerHelper.getTwoBytes(this.sheetRef, this.data, 8);
            if(this.builtInName != null) {
                this.data[15] = (byte)this.builtInName.getValue();
            } else {
                StringHelper.getBytes(this.name, this.data, 15);
            }

            int pos = this.builtInName != null?16:this.name.length() + 15;
            byte[] rd;
            if(this.ranges.length > 1) {
                this.data[pos++] = 41;
                IntegerHelper.getTwoBytes(detailLength - 3, this.data, pos);
                pos += 2;

                for(int i = 0; i < this.ranges.length; ++i) {
                    this.data[pos++] = 59;
                    rd = this.ranges[i].getData();
                    System.arraycopy(rd, 0, this.data, pos, rd.length);
                    pos += rd.length;
                }

                this.data[pos] = 16;
            } else {
                this.data[pos] = 59;
                rd = this.ranges[0].getData();
                System.arraycopy(rd, 0, this.data, pos + 1, rd.length);
            }

            return this.data;
        }
    }

    public String getName() {
        return this.name;
    }

    public int getIndex() {
        return this.index;
    }

    public int getSheetRef() {
        return this.sheetRef;
    }

    public void setSheetRef(int i) {
        this.sheetRef = i;
        IntegerHelper.getTwoBytes(this.sheetRef, this.data, 8);
    }

    public NameRecord.NameRange[] getRanges() {
        return this.ranges;
    }

    void rowInserted(int sheetIndex, int row) {
        for(int i = 0; i < this.ranges.length; ++i) {
            if(sheetIndex == this.ranges[i].getExternalSheet()) {
                if(row <= this.ranges[i].getFirstRow()) {
                    this.ranges[i].incrementFirstRow();
                    this.modified = true;
                }

                if(row <= this.ranges[i].getLastRow()) {
                    this.ranges[i].incrementLastRow();
                    this.modified = true;
                }
            }
        }

    }

    boolean rowRemoved(int sheetIndex, int row) {
        int emptyRanges;
        for(emptyRanges = 0; emptyRanges < this.ranges.length; ++emptyRanges) {
            if(sheetIndex == this.ranges[emptyRanges].getExternalSheet()) {
                if(row == this.ranges[emptyRanges].getFirstRow() && row == this.ranges[emptyRanges].getLastRow()) {
                    this.ranges[emptyRanges] = EMPTY_RANGE;
                }

                if(row < this.ranges[emptyRanges].getFirstRow() && row > 0) {
                    this.ranges[emptyRanges].decrementFirstRow();
                    this.modified = true;
                }

                if(row <= this.ranges[emptyRanges].getLastRow()) {
                    this.ranges[emptyRanges].decrementLastRow();
                    this.modified = true;
                }
            }
        }

        emptyRanges = 0;

        for(int newRanges = 0; newRanges < this.ranges.length; ++newRanges) {
            if(this.ranges[newRanges] == EMPTY_RANGE) {
                ++emptyRanges;
            }
        }

        if(emptyRanges == this.ranges.length) {
            return true;
        } else {
            NameRecord.NameRange[] var6 = new NameRecord.NameRange[this.ranges.length - emptyRanges];

            for(int i = 0; i < this.ranges.length; ++i) {
                if(this.ranges[i] != EMPTY_RANGE) {
                    var6[i] = this.ranges[i];
                }
            }

            this.ranges = var6;
            return false;
        }
    }

    boolean columnRemoved(int sheetIndex, int col) {
        int emptyRanges;
        for(emptyRanges = 0; emptyRanges < this.ranges.length; ++emptyRanges) {
            if(sheetIndex == this.ranges[emptyRanges].getExternalSheet()) {
                if(col == this.ranges[emptyRanges].getFirstColumn() && col == this.ranges[emptyRanges].getLastColumn()) {
                    this.ranges[emptyRanges] = EMPTY_RANGE;
                }

                if(col < this.ranges[emptyRanges].getFirstColumn() && col > 0) {
                    this.ranges[emptyRanges].decrementFirstColumn();
                    this.modified = true;
                }

                if(col <= this.ranges[emptyRanges].getLastColumn()) {
                    this.ranges[emptyRanges].decrementLastColumn();
                    this.modified = true;
                }
            }
        }

        emptyRanges = 0;

        for(int newRanges = 0; newRanges < this.ranges.length; ++newRanges) {
            if(this.ranges[newRanges] == EMPTY_RANGE) {
                ++emptyRanges;
            }
        }

        if(emptyRanges == this.ranges.length) {
            return true;
        } else {
            NameRecord.NameRange[] var6 = new NameRecord.NameRange[this.ranges.length - emptyRanges];

            for(int i = 0; i < this.ranges.length; ++i) {
                if(this.ranges[i] != EMPTY_RANGE) {
                    var6[i] = this.ranges[i];
                }
            }

            this.ranges = var6;
            return false;
        }
    }

    void columnInserted(int sheetIndex, int col) {
        for(int i = 0; i < this.ranges.length; ++i) {
            if(sheetIndex == this.ranges[i].getExternalSheet()) {
                if(col <= this.ranges[i].getFirstColumn()) {
                    this.ranges[i].incrementFirstColumn();
                    this.modified = true;
                }

                if(col <= this.ranges[i].getLastColumn()) {
                    this.ranges[i].incrementLastColumn();
                    this.modified = true;
                }
            }
        }

    }

    static {
        logger = Logger.getLogger(NameRecord.class );
        EMPTY_RANGE = new NameRecord.NameRange(0, 0, 0, 0, 0);
    }

    static class NameRange {
        private int columnFirst;
        private int rowFirst;
        private int columnLast;
        private int rowLast;
        private int externalSheet;

        NameRange(jxl.read.biff.NameRecord.NameRange nr) {
            this.columnFirst = nr.getFirstColumn();
            this.rowFirst = nr.getFirstRow();
            this.columnLast = nr.getLastColumn();
            this.rowLast = nr.getLastRow();
            this.externalSheet = nr.getExternalSheet();
        }

        NameRange(int extSheet, int theStartRow, int theEndRow, int theStartCol, int theEndCol) {
            this.columnFirst = theStartCol;
            this.rowFirst = theStartRow;
            this.columnLast = theEndCol;
            this.rowLast = theEndRow;
            this.externalSheet = extSheet;
        }

        int getFirstColumn() {
            return this.columnFirst;
        }

        int getFirstRow() {
            return this.rowFirst;
        }

        int getLastColumn() {
            return this.columnLast;
        }

        int getLastRow() {
            return this.rowLast;
        }

        int getExternalSheet() {
            return this.externalSheet;
        }

        void incrementFirstRow() {
            ++this.rowFirst;
        }

        void incrementLastRow() {
            ++this.rowLast;
        }

        void decrementFirstRow() {
            --this.rowFirst;
        }

        void decrementLastRow() {
            --this.rowLast;
        }

        void incrementFirstColumn() {
            ++this.columnFirst;
        }

        void incrementLastColumn() {
            ++this.columnLast;
        }

        void decrementFirstColumn() {
            --this.columnFirst;
        }

        void decrementLastColumn() {
            --this.columnLast;
        }

        byte[] getData() {
            byte[] d = new byte[10];
            IntegerHelper.getTwoBytes(this.externalSheet, d, 0);
            IntegerHelper.getTwoBytes(this.rowFirst, d, 2);
            IntegerHelper.getTwoBytes(this.rowLast, d, 4);
            IntegerHelper.getTwoBytes(this.columnFirst & 255, d, 6);
            IntegerHelper.getTwoBytes(this.columnLast & 255, d, 8);
            return d;
        }
    }
}
