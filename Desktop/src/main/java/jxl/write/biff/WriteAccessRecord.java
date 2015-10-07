package jxl.write.biff;
import jxl.Workbook;
import jxl.biff.StringHelper;
import jxl.biff.Type;
import jxl.biff.WritableRecordData;


class WriteAccessRecord extends WritableRecordData {
        private byte[] data = new byte[1024];
        private static final String authorString = "Java Excel API";
        private String userName;

        public WriteAccessRecord(String userName) {
            super(Type.WRITEACCESS);
            String astring = userName != null?userName:"Java Excel API v" + Workbook.getVersion();
            StringHelper.getBytes(astring, this.data, 0);

            for(int i = astring.length(); i < this.data.length; ++i) {
                this.data[i] = 32;
            }

        }

        public byte[] getData() {
            return this.data;
        }
    }


