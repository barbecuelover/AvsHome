package com.ecobee.bean;

/**
 * 作者：RedKeyset on 2018/12/17 11:46
 * 邮箱：redkeyset@aliyun.com
 */
public class CodeMessageBean {

    /**
     * status : {"code":0,"message":""}
     */

    private StatusBean status;

    public StatusBean getStatus() {
        return status;
    }

    public void setStatus(StatusBean status) {
        this.status = status;
    }

    public static class StatusBean {
        /**
         * code : 0
         * message :
         */

        private int code;
        private String message;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
