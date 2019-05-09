package com.ecobee.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：RedKeyset on 2018/12/14 09:42
 * 邮箱：redkeyset@aliyun.com
 */
public class GetTemperatureInfoBean implements Serializable{

    /**
     * page : {"page":1,"totalPages":1,"pageSize":1,"total":1}
     * thermostatList : [{"identifier":"411913903717","name":"Living Room","thermostatRev":"181214043834","isRegistered":true,"modelNumber":"nikeSmart","brand":"ecobee","features":"Home,HomeKit","lastModified":"2018-12-14 04:38:34","thermostatTime":"2018-12-14 18:26:57","utcTime":"2018-12-14 10:26:57","remoteSensors":[{"id":"rs:100","name":"Main Floor","type":"ecobee3_remote_sensor","code":"CRXG","inUse":true,"capability":[{"id":"1","type":"temperature","value":"749"},{"id":"2","type":"occupancy","value":"true"}]},{"id":"rs:101","name":"Bedroom","type":"ecobee3_remote_sensor","code":"CNQC","inUse":true,"capability":[{"id":"1","type":"temperature","value":"756"},{"id":"2","type":"occupancy","value":"false"}]},{"id":"ei:0","name":"Living Room","type":"thermostat","inUse":true,"capability":[{"id":"1","type":"temperature","value":"746"},{"id":"2","type":"humidity","value":"19"}]}]}]
     * status : {"code":0,"message":""}
     */

    private PageBean page;
    private StatusBean status;
    private List<ThermostatListBean> thermostatList;

    public PageBean getPage() {
        return page;
    }

    public void setPage(PageBean page) {
        this.page = page;
    }

    public StatusBean getStatus() {
        return status;
    }

    public void setStatus(StatusBean status) {
        this.status = status;
    }

    public List<ThermostatListBean> getThermostatList() {
        return thermostatList;
    }

    public void setThermostatList(List<ThermostatListBean> thermostatList) {
        this.thermostatList = thermostatList;
    }

    public static class PageBean {
        /**
         * page : 1
         * totalPages : 1
         * pageSize : 1
         * total : 1
         */

        private int page;
        private int totalPages;
        private int pageSize;
        private int total;

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }
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

    public static class ThermostatListBean {
        /**
         * identifier : 411913903717
         * name : Living Room
         * thermostatRev : 181214043834
         * isRegistered : true
         * modelNumber : nikeSmart
         * brand : ecobee
         * features : Home,HomeKit
         * lastModified : 2018-12-14 04:38:34
         * thermostatTime : 2018-12-14 18:26:57
         * utcTime : 2018-12-14 10:26:57
         * remoteSensors : [{"id":"rs:100","name":"Main Floor","type":"ecobee3_remote_sensor","code":"CRXG","inUse":true,"capability":[{"id":"1","type":"temperature","value":"749"},{"id":"2","type":"occupancy","value":"true"}]},{"id":"rs:101","name":"Bedroom","type":"ecobee3_remote_sensor","code":"CNQC","inUse":true,"capability":[{"id":"1","type":"temperature","value":"756"},{"id":"2","type":"occupancy","value":"false"}]},{"id":"ei:0","name":"Living Room","type":"thermostat","inUse":true,"capability":[{"id":"1","type":"temperature","value":"746"},{"id":"2","type":"humidity","value":"19"}]}]
         */

        private String identifier;
        private String name;
        private String thermostatRev;
        private boolean isRegistered;
        private String modelNumber;
        private String brand;
        private String features;
        private String lastModified;
        private String thermostatTime;
        private String utcTime;
        private List<RemoteSensorsBean> remoteSensors;

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getThermostatRev() {
            return thermostatRev;
        }

        public void setThermostatRev(String thermostatRev) {
            this.thermostatRev = thermostatRev;
        }

        public boolean isIsRegistered() {
            return isRegistered;
        }

        public void setIsRegistered(boolean isRegistered) {
            this.isRegistered = isRegistered;
        }

        public String getModelNumber() {
            return modelNumber;
        }

        public void setModelNumber(String modelNumber) {
            this.modelNumber = modelNumber;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getFeatures() {
            return features;
        }

        public void setFeatures(String features) {
            this.features = features;
        }

        public String getLastModified() {
            return lastModified;
        }

        public void setLastModified(String lastModified) {
            this.lastModified = lastModified;
        }

        public String getThermostatTime() {
            return thermostatTime;
        }

        public void setThermostatTime(String thermostatTime) {
            this.thermostatTime = thermostatTime;
        }

        public String getUtcTime() {
            return utcTime;
        }

        public void setUtcTime(String utcTime) {
            this.utcTime = utcTime;
        }

        public List<RemoteSensorsBean> getRemoteSensors() {
            return remoteSensors;
        }

        public void setRemoteSensors(List<RemoteSensorsBean> remoteSensors) {
            this.remoteSensors = remoteSensors;
        }

        public static class RemoteSensorsBean{
            @Override
            public String toString() {
                return "RemoteSensorsBean{" +
                        "id='" + id + '\'' +
                        ", name='" + name + '\'' +
                        ", type='" + type + '\'' +
                        ", code='" + code + '\'' +
                        ", inUse=" + inUse +
                        ", capability=" + capability +
                        '}';
            }

            /**
             * id : rs:100
             * name : Main Floor
             * type : ecobee3_remote_sensor
             * code : CRXG
             * inUse : true
             * capability : [{"id":"1","type":"temperature","value":"749"},{"id":"2","type":"occupancy","value":"true"}]
             */

            private String id;
            private String name;
            private String type;
            private String code;
            private boolean inUse;
            private List<CapabilityBean> capability;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public boolean isInUse() {
                return inUse;
            }

            public void setInUse(boolean inUse) {
                this.inUse = inUse;
            }

            public List<CapabilityBean> getCapability() {
                return capability;
            }

            public void setCapability(List<CapabilityBean> capability) {
                this.capability = capability;
            }

            public static class CapabilityBean {
                /**
                 * id : 1
                 * type : temperature
                 * value : 749
                 */

                private String id;
                private String type;
                private String value;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public String getValue() {
                    return value;
                }

                public void setValue(String value) {
                    this.value = value;
                }
            }
        }
    }
}
