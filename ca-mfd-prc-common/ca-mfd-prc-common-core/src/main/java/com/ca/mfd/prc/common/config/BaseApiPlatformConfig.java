 package com.ca.mfd.prc.common.config;

 /**
  *
  * <p>功能描述: ApiPlatformConfig.java</p>
  * @author
  * @version  2023年7月5日
  */

 public class BaseApiPlatformConfig {

     private String  platformUrl;
     private String  apiAppKey;
     private String  decryptKey;
     private Integer validMinutes;
     private String  apiSecertKey;
     private RestfulConfig apiPlatform = new RestfulConfig();

     public String getPlatformUrl() {
         return platformUrl;
     }

     public void setPlatformUrl(String platformUrl) {
         this.platformUrl = platformUrl;
     }

     public String getApiAppKey() {
         return apiAppKey;
     }

     public void setApiAppKey(String apiAppKey) {
         this.apiAppKey = apiAppKey;
     }

     public String getDecryptKey() {
         return decryptKey;
     }

     public void setDecryptKey(String decryptKey) {
         this.decryptKey = decryptKey;
     }

     public Integer getValidMinutes() {
         return validMinutes;
     }

     public void setValidMinutes(Integer validMinutes) {
         this.validMinutes = validMinutes;
     }

     public String getApiSecertKey() {
         return apiSecertKey;
     }

     public void setApiSecertKey(String apiSecertKey) {
         this.apiSecertKey = apiSecertKey;
     }

     public RestfulConfig getApiPlatform() {
         return apiPlatform;
     }

     public void setApiPlatform(RestfulConfig apiPlatform) {
         this.apiPlatform = apiPlatform;
     }

     public class RestfulConfig {
         private Integer connectionRequestTimeout;
         private Integer connectTimeout;
         private Integer readTimeout;
         private Integer maxConnTotal;
         private Integer maxConnPerRoute;

         public Integer getConnectionRequestTimeout() {
             return connectionRequestTimeout;
         }

         public void setConnectionRequestTimeout(Integer connectionRequestTimeout) {
             this.connectionRequestTimeout = connectionRequestTimeout;
         }

         public Integer getConnectTimeout() {
             return connectTimeout;
         }

         public void setConnectTimeout(Integer connectTimeout) {
             this.connectTimeout = connectTimeout;
         }

         public Integer getReadTimeout() {
             return readTimeout;
         }

         public void setReadTimeout(Integer readTimeout) {
             this.readTimeout = readTimeout;
         }

         public Integer getMaxConnTotal() {
             return maxConnTotal;
         }

         public void setMaxConnTotal(Integer maxConnTotal) {
             this.maxConnTotal = maxConnTotal;
         }

         public Integer getMaxConnPerRoute() {
             return maxConnPerRoute;
         }

         public void setMaxConnPerRoute(Integer maxConnPerRoute) {
             this.maxConnPerRoute = maxConnPerRoute;
         }

     }

 }
